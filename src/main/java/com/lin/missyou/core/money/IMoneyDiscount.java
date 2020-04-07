package com.lin.missyou.core.money;

import java.math.BigDecimal;

public interface IMoneyDiscount {

    public BigDecimal discount(BigDecimal original,BigDecimal discount);
}
