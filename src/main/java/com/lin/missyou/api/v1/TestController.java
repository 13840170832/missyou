package com.lin.missyou.api.v1;

import com.lin.missyou.sample.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Test test;

    @GetMapping("")
    public void getDetail(){
        System.out.println(this.test);
    }
}
