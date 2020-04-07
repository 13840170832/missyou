package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.service.OrderService;
import com.lin.missyou.vo.OrderIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @ScopeLevel
//    @PostMapping
//    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO){
//        Long uid = LocalUser.getUser().getId();
//        OrderChecker orderChecker = orderService.isOk(uid,orderDTO);
//
//    }
}
