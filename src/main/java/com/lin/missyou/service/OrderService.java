package com.lin.missyou.service;

import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.Sku;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
    private IMoneyDiscount iMoneyDiscount;

    @Value("${missyou.order.max-sku-limit}")
    private Integer maxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Integer payTimeLimit;


    public void placeOrder(Long uid,OrderDTO orderDTO,CouponChecker couponChecker){



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

            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndAndCouponId(uid,couponId)
                    .orElseThrow(()->new NotFoundException(50006));

            couponChecker = new CouponChecker(coupon,iMoneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(orderDTO,skuList,couponChecker,this.maxSkuLimit);
        orderChecker.isOk();
        return orderChecker;
    }
}
