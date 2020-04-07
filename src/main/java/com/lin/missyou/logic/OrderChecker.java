package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.OrderSku;
import com.lin.missyou.model.Sku;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderChecker {

    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;

    @Getter
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuList, CouponChecker couponChecker,Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;
    }

    public String getLeaderImg(){
        return this.serverSkuList.get(0).getImg();
    }

    public String getLeaderTitle(){
        return this.serverSkuList.get(0).getTitle();
    }

    public Integer getTotalCount(){
        return this.orderDTO.getSkuInfoList().stream().map(s -> s.getCount())
                .reduce(Integer::sum)
                .orElse(0);
    }
    public void isOk(){
        BigDecimal serverTotalPrice = BigDecimal.ZERO;
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        this.skuNotOnSale(orderDTO.getSkuInfoList().size(),serverSkuList.size());
        for(int i=0;i<this.serverSkuList.size();i++){
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            this.containsSoldOutSku(sku);
            this.beyondSkuStock(sku,skuInfoDTO);
            this.beyondMaxSkuLimit(skuInfoDTO);

            serverTotalPrice = serverTotalPrice.add(this.caculateSkuOrderPrice(sku,skuInfoDTO));
            skuOrderBOList.add(new SkuOrderBO(sku,skuInfoDTO));
            orderSkuList.add(new OrderSku(skuInfoDTO,sku));
        }
        this.totalPriceIsOk(orderDTO.getTotalPrice(),serverTotalPrice);
        if(null != this.couponChecker){
            this.couponChecker.isOk();
            this.couponChecker.canBeUsed(skuOrderBOList,serverTotalPrice);
            this.couponChecker.finalTotalPriceIsOk(orderDTO.getFinalTotalPrice(),serverTotalPrice);
        }
    }

    private void totalPriceIsOk(BigDecimal orderTotalPrice,BigDecimal serverTotalPrice){
        if(orderTotalPrice.compareTo(serverTotalPrice) != 0){
            throw new ParameterException(50005);
        }
    }

    private BigDecimal caculateSkuOrderPrice(Sku sku,SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() <= 0){
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    private void skuNotOnSale(int count1,int count2){
        if(count1 != count2){
            throw new ParameterException(50002);
        }
    }

    private void containsSoldOutSku(Sku sku){
        if(sku.getStock() == 0){
            throw new ParameterException(50001);
        }
    }

    private void beyondSkuStock(Sku sku,SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() > sku.getStock()){
            throw new ParameterException(50003);
        }
    }

    private void beyondMaxSkuLimit(SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() > this.maxSkuLimit){
            throw new ParameterException(50004);
        }
    }

}
