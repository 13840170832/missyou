package com.lin.missyou.service;

import com.lin.missyou.bo.OrderMessageBO;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.exception.http.ServerErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.UserCouponRepository;
import javafx.scene.layout.BorderRepeat;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CouponBackService {

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @EventListener
    @Transactional
    public void returnBack(OrderMessageBO messageBO){
        Long uid = messageBO.getUid();
        Long oid = messageBO.getOid();
        Long couponId = messageBO.getCouponId();
        if(-1 == couponId){
            return;
        }
        Optional<Order> orderOption = orderRepository.findFirstByUserIdAndId(uid,oid);
        Order order = orderOption.orElseThrow(()->
                new ServerErrorException(9999)
        );
        if(order.getStatusEnum().equals(OrderStatus.UNPAID)
            || order.getStatusEnum().equals(OrderStatus.CANCELED)){
            userCouponRepository.returnBack(couponId,uid);
        }
    }
}
