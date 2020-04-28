package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class SaleExplain extends BaseEntity{
    @Id
    private int id;
    private Byte fixed;
    private String text;
    private Integer spuId;
    private Integer index;
    private Integer replaceId;

}
