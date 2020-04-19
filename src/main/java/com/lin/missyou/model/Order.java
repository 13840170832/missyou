package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.dto.OrderAddressDTO;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
@Table(name = "`Order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalPrice;  //商品原始价格
    private Long totalCount;
    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;
    private String prepayId;        //微信支付
    private BigDecimal finalTotalPrice; //订单最终价格
    private Integer status;
    private Date placedTime;
    private Date expiredTime;

    @JsonIgnore
    public OrderStatus getStatusEnum() {
        return OrderStatus.toType(this.status);
    }

    public Boolean needCancel(){
        if(!this.getStatusEnum().equals(OrderStatus.UNPAID)){
            return true;
        }
        boolean isOutOfDate = CommonUtil.isOutOfDate(this.getExpiredTime());
        if(isOutOfDate){
            return true;
        }
        return false;
    }

    public void setSnapItems(List<OrderSku> orderSkuList){
        if(orderSkuList.isEmpty()){
            return;
        }
        this.snapItems = GenericAndJson.objectToJson(orderSkuList);
    }

    public List<OrderSku> getSnapItems(){
        List<OrderSku> list = GenericAndJson.jsonToObject(this.snapItems,
                new TypeReference<List<OrderSku>>() {
                });
        return list;
    }

    public void setSnapAddress(OrderAddressDTO orderAddressDTO){
        this.snapAddress = GenericAndJson.objectToJson(orderAddressDTO);
    }

    public OrderAddressDTO getSnapAddress(){
        if(null == this.snapAddress){
            return null;
        }
        return GenericAndJson.jsonToObject(this.snapAddress, new TypeReference<OrderAddressDTO>() {
        });
    }

}
