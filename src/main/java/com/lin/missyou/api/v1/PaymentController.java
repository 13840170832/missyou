package com.lin.missyou.api.v1;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.util.Map;

@RequestMapping("/payment")
@RestController
@Validated
public class PaymentController {

    @PostMapping("/pay/order/{id}")
    public Map<String,String> preWxOrder(@PathVariable(name="id") @Positive Long oid){

        return null;
    }

    @RequestMapping("/wx/notify")
    public String payCallback(HttpServletRequest request,
                              HttpServletResponse response){

        return null;
    }
}
