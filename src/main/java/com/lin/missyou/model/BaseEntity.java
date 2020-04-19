package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @JsonIgnore
    @Column(insertable = false,updatable = false)
    private Timestamp createTime;
    @JsonIgnore
    @Column(insertable = false,updatable = false)
    private Timestamp updateTime;
    @JsonIgnore
    @Column(insertable = false,updatable = false)
    private Timestamp deleteTime;
}
