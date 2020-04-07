package com.lin.missyou.sample;

import org.springframework.context.annotation.Conditional;
import com.lin.missyou.sample.condition.DianaCondition;
import com.lin.missyou.sample.hero.Diana;
import com.lin.missyou.sample.hero.Irelia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeroConfiguration {

//    @Bean
//    @Conditional(DianaCondition.class)
    public ISkill diana(){
        return new Diana();
    }

    @Bean
    public ISkill irelia(){
        return new Irelia();
    }
}
