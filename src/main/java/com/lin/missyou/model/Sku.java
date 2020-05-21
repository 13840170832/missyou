package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.util.GenericAndJson;
import com.lin.missyou.util.ListAndJson;
import com.lin.missyou.util.MapAndJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
    @Where(clause = "delete_time is null and online = 1 and stock > 0 ")
public class Sku extends BaseEntity{
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private byte online;
    private String img;
    private String title;
    private Long spuId;

    private String specs;

    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

    public BigDecimal getActualPrice(){
        return discountPrice==null ? this.price : this.discountPrice;
    }

    public List<Spec> getSpecs() {
        if(null == this.specs){
            return Collections.emptyList();
        }
        return GenericAndJson.jsonToObject(this.specs,new TypeReference<List<Spec>>(){});
    }

    public void setSpecs(List<Spec> specs) {
        if(specs.isEmpty()){
            return;
        }
        this.specs = GenericAndJson.objectToJson(specs);
    }


    @JsonIgnore
    public List<String> getSpecValueList(){
        return this.getSpecs().stream().map(s->s.getValue()).collect(Collectors.toList());
    }
}
