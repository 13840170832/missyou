package com.lin.missyou.model;

        import lombok.Getter;
        import lombok.Setter;
        import org.hibernate.annotations.Where;

        import javax.persistence.*;
        import java.math.BigDecimal;
        import java.sql.Timestamp;
        import java.util.List;
        import java.util.Objects;

@Getter
@Setter
@Entity
@Where(clause = "delete_time is null")
public class  Coupon extends BaseEntity{
    @Id
    private Long id;
    private Long activityId;
    private String title;
    private Timestamp startTime;    //优惠券开始时间
    private Timestamp endTime;      //优惠券结束时间
    private String description;
    private BigDecimal fullMoney;   //满减券   满的金额
    private BigDecimal minus;       //满减券   减的金额
    private BigDecimal rate;        //折扣券   折扣
    private String remark;
    private Boolean wholeStore;     //是否为全场券
    private Integer type;           //优惠券类型 1. 满减券 2.折扣券 3.无门槛券 4.满金额折扣券

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "couponList")
    private List<Category> categoryList;


}
