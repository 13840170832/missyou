package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
public class GridCategory extends BaseEntity{
    @Id
    private int id;
    private String title;
    private String img;
    private String name;
    private Integer categoryId;
    private Integer rootCategoryId;

}
