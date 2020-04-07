package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.UnifyResponse;
import com.lin.missyou.core.enumeration.CouponStatus;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.exception.CreateSuccess;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.User;
import com.lin.missyou.service.CouponService;
import com.lin.missyou.vo.CouponCategoryVO;
import com.lin.missyou.vo.CouponPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    public CouponService couponService;

    @GetMapping("/by/category/{cid}")
    public List<CouponPureVO> getCouponListByCategory(@PathVariable Long cid){
        List<Coupon> couponList = couponService.getByCategory(cid);
        if(couponList.isEmpty()){
            return Collections.emptyList();
        }
        List<CouponPureVO> vos = CouponPureVO.getList(couponList);
        return vos;
    }

    @GetMapping("/whole_store")
    public List<CouponPureVO> getWholeStoreCouponList(){
        List<Coupon> couponList = couponService.getByWholeStore();
        if(couponList.isEmpty()){
            return Collections.emptyList();
        }
        List<CouponPureVO> vos = CouponPureVO.getList(couponList);
        return vos;
    }

    @ScopeLevel
    @PostMapping("/collect/{id}")
    public void collectCoupon(@PathVariable Long id){
        Long uid = LocalUser.getUser().getId();
        couponService.collectOneCoupon(uid,id);
        UnifyResponse.createSuccess(0);
    }

    @ScopeLevel
    @GetMapping("/myself/by/status/{status}")
    public List<CouponPureVO> getMyCouponByStatus(@PathVariable Integer status){
        Long uid = LocalUser.getUser().getId();
        List<Coupon> couponList;
        switch (CouponStatus.toType(status)){
            case AVAILABLE:
                couponList = couponService.getMyAvailable(uid);
                break;
            case USED:
                couponList = couponService.getMyUsed(uid);
                break;
            case EXPIRED:
                couponList = couponService.getMyExpired(uid);
                break;
            default:
                throw new ParameterException(40001);
        }
        return CouponPureVO.getList(couponList);
    }

    @ScopeLevel()
    @GetMapping("/myself/available/with_category")
    public List<CouponCategoryVO> getUserCouponWithCategory(){
        User user = LocalUser.getUser();
        List<Coupon> coupons = couponService.getMyAvailable(user.getId());
        if(coupons.isEmpty()){
            return Collections.emptyList();
        }
        return coupons.stream().map(coupon -> {
            CouponCategoryVO vo = new CouponCategoryVO(coupon);
            return vo;
        })
        .collect(Collectors.toList());
    }
}
