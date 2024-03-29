package com.mobile.service;

import com.mobile.vo.InventoryReportVO;
import com.mobile.vo.OrderReportVO;
import com.mobile.vo.SaleReportVO;
import com.mobile.vo.ShopReportVO;

import java.time.LocalDate;

public interface ReportService {

    OrderReportVO getOrderStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO getInventoryStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO inventoryPrinceStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO inventoryModelStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO distributionStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO contractStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO qualityStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO priceStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO modelStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO channelStatistics(String province, String city, String county, String grid, String hall, int year);

    ShopReportVO GetProvinces();

    ShopReportVO GetCities(String province);

    ShopReportVO GetCounties(String city);

    ShopReportVO GetGrids(String county);

    ShopReportVO GetHalls(String grid);

    ShopReportVO QueryProvinces(String province);

    ShopReportVO QueryCities(String city);

    ShopReportVO QueryCounties(String county);

    ShopReportVO QueryGrids(String grid);

    ShopReportVO QueryHalls(String hall);

    /**
     * 导出运营数据报表
     * @param response
     */
//    void exportBusinessData(HttpServletResponse response);
}
