package com.lin.missyou.util;

import com.lin.missyou.bo.PageCounter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

    public static PageCounter convertToPageParameter(Integer start, Integer count){
        int pageNum = start / count;
        PageCounter pageCounter = PageCounter.builder()
                .page(pageNum)
                .count(count)
                .build();
        return pageCounter;
    }

    public static Boolean isInTimeLine(Date date, Date start, Date end){
        Long dateTime = date.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        if(dateTime>startTime && dateTime<endTime){
            return true;
        }
        return false;
    }

    public static Boolean isOutOfDate(Date expiredTime){
        Long now = Calendar.getInstance().getTimeInMillis();
        Long expiredTimeStamp = expiredTime.getTime();
        if(now > expiredTimeStamp){
            return true;
        }
        return false;
    }

    public static String yuanToFenPlainString(BigDecimal p){
        p = p.multiply(new BigDecimal("100"));
        return CommonUtil.toPlain(p);
    }

    public static String toPlain(BigDecimal p){
        return p.stripTrailingZeros().toPlainString();
    }
}
