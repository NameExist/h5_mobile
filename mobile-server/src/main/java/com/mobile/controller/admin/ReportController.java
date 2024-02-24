package com.mobile.controller.admin;

import com.mobile.result.Result;
import com.mobile.service.ReportService;
import com.mobile.vo.InventoryReportVO;
import com.mobile.vo.OrderReportVO;
import com.mobile.vo.SaleReportVO;
import com.mobile.vo.ShopReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/GetProvinces")
    @ApiOperation("省级地区统计")
    public Result<ShopReportVO> GetProvinces(){
        return Result.success(reportService.GetProvinces());
    }

    @GetMapping("/GetCities")
    @ApiOperation("市级地区统计")
    public Result<ShopReportVO> GetCities(String province){
        return Result.success(reportService.GetCities(province));
    }

    @GetMapping("/GetCounties")
    @ApiOperation("区县级地区统计")
    public Result<ShopReportVO> GetCounties(String city){
        return Result.success(reportService.GetCounties(city));
    }


    @GetMapping("/GetGrids")
    @ApiOperation("网格级地区统计")
    public Result<ShopReportVO> GetGrids(String county){
        return Result.success(reportService.GetGrids(county));
    }

    @GetMapping("/GetHalls")
    @ApiOperation("厅店级地区统计")
    public Result<ShopReportVO> GetHalls(String grid){
        return Result.success(reportService.GetHalls(grid));
    }

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

    @GetMapping("/contractStatistics")
    @ApiOperation("合约销售结构统计")
    public Result<SaleReportVO> contractStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("销售数量统计：{},{}",begin,end);
        return Result.success(reportService.contractStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/priceStatistics")
    @ApiOperation("价位销售结构统计")
    public Result<SaleReportVO> priceStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{},{}",begin,end);
        return Result.success(reportService.priceStatistics(province,city,county,grid,hall,begin,end));
    }


    @GetMapping("/qualityStatistics")
    @ApiOperation("终端销售质量统计")
    public Result<SaleReportVO> qualityStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("销售数量统计：{},{}",begin,end);
        return Result.success(reportService.qualityStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/modelStatistics")
    @ApiOperation("机型销售结构统计")
    public Result<SaleReportVO> modelStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{},{}",begin,end);
        return Result.success(reportService.modelStatistics(province,city,county,grid,hall,begin,end));
    }

    @GetMapping("/channelStatistics")
    @ApiOperation("渠道销售情况统计")
    public Result<SaleReportVO> channelStatistics(
            String province,
            String city,
            String county,
            String grid,
            String hall,
            @RequestParam("year") int year){
        log.info("单位：{},{},{},{},{}",province,city,county,grid,hall);
        log.info("库存数量统计：{}",year);
//        LocalDate localDate = LocalDate.of(year, 1, 1);
        return Result.success(reportService.channelStatistics(province,city,county,grid,hall,year));
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
