package com.lin.missyou.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Component
public class HalfEvenRound implements IMoneyDiscount {

    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal finalMoney = original.multiply(discount).setScale(2, RoundingMode.HALF_EVEN);
        return finalMoney;
    }
}
