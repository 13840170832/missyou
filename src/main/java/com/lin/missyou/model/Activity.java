package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null and online = 1 ")
public class Activity extends BaseEntity{
    @Id
    private Long id;
    private String title;
    private String name;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private Byte online;
    private String entranceImg;     //活动入口图片
    private String internalTopImg;
    private String remark;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId")
    private List<Coupon> couponList;
}
