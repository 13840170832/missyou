package com.lin.missyou.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SchoolDTO {
    @Length(min=4,max=6,message = "SchoolName长度范围4~6" )
    private String name;
}
