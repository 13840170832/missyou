package com.lin.missyou.service;

import com.lin.missyou.bo.OrderMessageBO;
import com.lin.missyou.exception.http.ServerErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderCancelService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    public void cancel(OrderMessageBO messageBO){
        if(messageBO.getOid()<=0){
            throw new ServerErrorException(9999);
        }
        this.cancel(messageBO.getOid());
    }

    @Transactional
    public void cancel(Long oid){
        Optional<Order> orderOptional = orderRepository.findById(oid);
        Order order = orderOptional.orElseThrow(()->
                new ServerErrorException(9999)
        );

//        int res = orderRepository.cancelOrder(oid);
//        if(1 != res){
//            return;
//        }
//        order.getSnapItems().forEach(i->{
//            skuRepository.recoverStock(i.getId(),i.getCount().longValue());
//        });
    }
}
