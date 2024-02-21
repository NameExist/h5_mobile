package com.mobile.controller.admin;

import com.mobile.result.Result;
import com.mobile.service.ReportService;
import com.mobile.vo.InventoryReportVO;
import com.mobile.vo.OrderReportVO;
import com.mobile.vo.SaleReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/mobile")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/OrderStatistics")
    @ApiOperation("终端订货数量统计")
    public Result<OrderReportVO> OrderStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("订货数量统计：{},{}",begin,end);
        return Result.success(reportService.getOrderStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/inventoryStatistics")
    @ApiOperation("终端库存数量统计")
    public Result<InventoryReportVO> inventoryStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{},{}",begin,end);
        return Result.success(reportService.getInventoryStatistics(province,city,county,grid,hall,begin,end));
    }


    @GetMapping("/inventoryPrinceStatistics")
    @ApiOperation("价位库存结构统计")
    public Result<InventoryReportVO> inventoryPrinceStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{},{}",begin,end);
        return Result.success(reportService.inventoryPrinceStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/inventoryModelStatistics")
    @ApiOperation("机型库存结构统计")
    public Result<InventoryReportVO> inventoryModelStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{},{}",begin,end);
        return Result.success(reportService.inventoryModelStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/distributionStatistics")
    @ApiOperation("终端销量分布统计")
    public Result<SaleReportVO> distributionStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("销售数量统计：{},{}",begin,end);
        return Result.success(reportService.distributionStatistics(province,city,county,grid,hall,begin,end));
    }


    /**
     * 导出运营数据报表
     * @param response
     */
//    @GetMapping("/export")
//    @ApiOperation("导出运营数据报表")
//    public void export(HttpServletResponse response){
//        reportService.exportBusinessData(response);
//    }
}
