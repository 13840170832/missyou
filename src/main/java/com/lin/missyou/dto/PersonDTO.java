package com.lin.missyou.dto;

import com.lin.missyou.dto.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;

@Getter
@Setter
@PasswordEqual(message = "两次密码不一致")
public class PersonDTO {
//    @NonNull
    @Length(min=3,max = 10,message = "字符串长度3~10")
    private String name;
    private Integer age;
    private String password1;
    private String password2;
    @Valid
    private SchoolDTO schoolDTO;
}
