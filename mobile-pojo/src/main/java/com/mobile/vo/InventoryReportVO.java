package com.mobile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportVO implements Serializable {

    private Integer datatotal;

    private String dateList;

    private String inventoryList;

    private String pricelist;

    private String proportionlist;

    private String modellist;

}
