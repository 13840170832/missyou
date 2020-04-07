package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.core.enumeration.CouponType;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CouponChecker {

    private Coupon coupon;
    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon, IMoneyDiscount iMoneyDiscount) {
        this.coupon = coupon;
        this.iMoneyDiscount = iMoneyDiscount;
    }

    /***/
    public void isOk(){
        Date now = new Date();
        Boolean isInTime = CommonUtil.isInTimeLine(now,coupon.getStartTime(),coupon.getEndTime());
        if(!isInTime){
            throw new ForbiddenException(40007);
        }
    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice,
                                    BigDecimal serverTotalPrice){
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_MINUS:
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                if(serverFinalTotalPrice.compareTo(BigDecimal.ZERO) <= 0){
                    throw new ForbiddenException(50008);
                }
                break;
            case FULL_OFF:
                serverFinalTotalPrice = this.iMoneyDiscount.discount(serverTotalPrice,this.coupon.getRate());
                break;
            default:
                throw new ParameterException(40009);
        }
        if(serverFinalTotalPrice.compareTo(orderFinalTotalPrice) !=0){
            throw new ForbiddenException(50008);
        }
    }

    public void canBeUsed(List<SkuOrderBO> skuOrderBOList,BigDecimal serverTotalPrice){
        BigDecimal orderCategoryPrice;
        if(this.coupon.getWholeStore()){
            orderCategoryPrice = serverTotalPrice;
        }else{
            List<Long> cids = this.coupon.getCategoryList().stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList());
            orderCategoryPrice = this.getSumByCategoryList(skuOrderBOList,cids);
        }
        this.couponCanBeUsed(orderCategoryPrice);
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice){
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_MINUS:
            case FULL_OFF:
                if(this.coupon.getFullMoney().compareTo(orderCategoryPrice)>0){
                    throw new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }

    private BigDecimal getSumByCategoryList(List<SkuOrderBO> skuOrderBOList,List<Long> cids){
        BigDecimal sum = cids.stream()
                .map(cid ->  getSumByCategory(skuOrderBOList,cid))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return sum;
    }

    private BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList,Long cid){
        BigDecimal sum = skuOrderBOList.stream()
                .filter(sku -> sku.getCategoryId().equals(cid))
                .map(bo -> bo.getTotalPrice())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return sum;
    }
}
