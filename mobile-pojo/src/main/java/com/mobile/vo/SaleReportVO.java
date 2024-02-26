package com.mobile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleReportVO implements Serializable {

    private String dateList;

    private String saleslist;

    private BigDecimal goldCoinContract;

    private BigDecimal surplusYield;

    private BigDecimal installmentContract;

    private String pricelist;

    private String proportionlist;

    private String modellist;

    private BigDecimal rate1;

    private BigDecimal rate2;

    private BigDecimal rate3;

    private String channellist;

    private String channelnumber;

    private String availablenumber;

    private String availablerate;

    private String effectivenumber;

    private String effectiverate;

    private double growthRate;
}
