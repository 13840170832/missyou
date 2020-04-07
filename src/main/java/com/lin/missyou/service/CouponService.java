package com.lin.missyou.service;

import com.lin.missyou.core.enumeration.CouponStatus;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Activity;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.repository.ActivityRepository;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getByCategory(Long cid){
        Date now = new Date();
        return couponRepository.findByCatory(cid,now);
    }

    public List<Coupon> getByWholeStore(){
        Date now = new Date();
        return couponRepository.findByWholeStore(true,now);
    }

    public List<Coupon> getMyAvailable(Long uid){
        Date now = new Date();
        return couponRepository.findMyAvailable(uid,now);
    }

    public List<Coupon> getMyUsed(Long uid){
        Date now = new Date();
        return couponRepository.findMyUsed(uid,now);
    }

    public List<Coupon> getMyExpired(Long uid){
        Date now = new Date();
        return couponRepository.findMyExpired( uid,now);
    }

    public void collectOneCoupon(Long uid,Long couponId){
        couponRepository.findById(couponId)
                .orElseThrow(()->new NotFoundException(40003));
        Activity activity = activityRepository.findByCouponListId(couponId)
                .orElseThrow(()->new NotFoundException(40010));
        Date now = new Date();
        Boolean isIn = CommonUtil.isInTimeLine(now,activity.getStartTime(),activity.getEndTime());
        if(!isIn){
            throw new ParameterException(40006);
        }
        userCouponRepository.findFirstByUserIdAndAndCouponId(uid, couponId)
                .ifPresent((uc)->{throw new ParameterException(40006);});
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(uid)
                .couponId(couponId)
                .status(CouponStatus.AVAILABLE.getValue())
                .createTime(now)
                .build();
        userCouponRepository.save(userCoupon);

    }
}
