package com.lin.missyou.core.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HalfUpRound implements IMoneyDiscount {

    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal finalMoney = original.multiply(discount).setScale(2,RoundingMode.HALF_UP);
        return finalMoney;
    }
}
