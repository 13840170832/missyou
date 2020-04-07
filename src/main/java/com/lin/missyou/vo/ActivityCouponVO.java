package com.lin.missyou.vo;

import com.lin.missyou.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActivityCouponVO extends ActivityPureVO{

    private List<CouponPureVO> couponList;

    public ActivityCouponVO(Activity activity){
        super(activity);
        couponList = activity.getCouponList()
                .stream().map(c ->{
                    return new CouponPureVO(c);
                }).collect(Collectors.toList());
    }
}
