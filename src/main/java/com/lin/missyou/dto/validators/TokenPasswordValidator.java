package com.lin.missyou.dto.validators;

import com.lin.missyou.dto.TokenGetDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> {
    private int min;
    private int max;


    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isEmpty(s)){
            return true;    //小程序登录时不需要密码
        }
        return s.length() >= this.min && s.length() <= this.max;
    }
}
