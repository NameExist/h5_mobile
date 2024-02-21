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
}
