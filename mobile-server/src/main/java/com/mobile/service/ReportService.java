package com.mobile.service;

import com.mobile.vo.InventoryReportVO;
import com.mobile.vo.OrderReportVO;
import com.mobile.vo.SaleReportVO;

import java.time.LocalDate;

public interface ReportService {

    OrderReportVO getOrderStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO getInventoryStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO inventoryPrinceStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    InventoryReportVO inventoryModelStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);

    SaleReportVO distributionStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end);
    /**
     * 导出运营数据报表
     * @param response
     */
//    void exportBusinessData(HttpServletResponse response);
}
