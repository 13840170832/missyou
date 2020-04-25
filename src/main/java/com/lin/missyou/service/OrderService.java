package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.*;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.OrderUtil;
import com.lin.missyou.vo.OrderSimplifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${missyou.order.max-sku-limit}")
    private Integer maxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Integer payTimeLimit;

    @Transactional
    public Long placeOrder(Long uid,OrderDTO orderDTO,OrderChecker orderChecker){
        Date now = new Date();
        Calendar expiredTime = Calendar.getInstance();
        expiredTime.add(Calendar.SECOND,payTimeLimit);
        String orderNo = OrderUtil.makeOrderNo();
        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(uid)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .totalCount(orderChecker.getTotalCount().longValue())
                .snapImg(orderChecker.getLeaderImg())
                .snapTitle(orderChecker.getLeaderTitle())
                .status(OrderStatus.UNPAID.value())
                .placedTime(now)
                .expiredTime(expiredTime.getTime())
                .build();

        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        this.orderRepository.save(order);
        this.reduceStock(orderChecker);
        //reduceStock
        //核销优惠券
        //加入到延迟消息队列
        Long couponId = -1L;
        if(null != orderDTO.getCouponId()){
            couponId = orderDTO.getCouponId();
            this.writeOffCoupon(orderDTO.getCouponId(),order.getId(),uid);
        }
        sendToRedis(order.getId(),uid,couponId);
        return order.getId();
    }

    private void sendToRedis(Long oid,Long uid,Long couponId){
        String key = uid.toString() + "," + oid.toString() + "," + couponId.toString();
        try{
            stringRedisTemplate.opsForValue().set(key,"1",payTimeLimit, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Page<Order> getUnpaid(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createTime").descending());
        Date now = new Date();
        Long uid = LocalUser.getUser().getId();
        return orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(now,OrderStatus.UNPAID.value(),uid,pageable);
    }

    public Page<Order> getByStatus(Integer status,Integer page,Integer size){
        Pageable pageable = PageRequest.of(page,size,Sort.by("createTime").descending());
        Date now = new Date();
        Long uid = LocalUser.getUser().getId();
        if(status == OrderStatus.ALL.value()){
            return orderRepository.findByUserId(uid,pageable);
        }
        return orderRepository.findByUserIdAndStatus(uid,status,pageable);
    }

    public Optional<Order> getOrderDetail(Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findFirstByUserIdAndId(uid,oid);
    }

    public void updateOrderPrepayId(Long orderId,String prePayId){
        Optional<Order> order = this.orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.setPrepayId(prePayId);
            orderRepository.save(o);
        });
        order.orElseThrow(()-> new ParameterException(10007));
    }

    private void writeOffCoupon(Long couponId,Long oid,Long uid){
        int result = userCouponRepository.writeOffCoupon(couponId,oid,uid);
        if(1 != result){
            throw new ForbiddenException(40012);
        }
    }

    private void reduceStock(OrderChecker orderChecker){
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for(OrderSku orderSku : orderSkuList){
            System.out.println(orderSku.getId());
            System.out.println(orderSku.getCount().longValue());
            int result = skuRepository.reduceStock(orderSku.getId(),orderSku.getCount().longValue());
            if(1 != result){
                throw new ParameterException(50003);
            }
        }
    }

    public OrderChecker isOk(Long uid, OrderDTO orderDTO){
        if(orderDTO.getFinalTotalPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ParameterException(50011);
        }
        List<Long> skuIdList = orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());

        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;
        if(null != couponId){
            Coupon coupon =couponRepository.findById(couponId)
                    .orElseThrow(()->new NotFoundException(40004));

            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndAndCouponIdAndStatus(uid,couponId,1)
                    .orElseThrow(()->new NotFoundException(50006));

            couponChecker = new CouponChecker(coupon,iMoneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(orderDTO,skuList,couponChecker,this.maxSkuLimit);
        orderChecker.isOk();
        return orderChecker;
    }
}
