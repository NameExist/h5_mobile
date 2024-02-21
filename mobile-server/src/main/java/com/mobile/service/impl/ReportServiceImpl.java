package com.mobile.service.impl;

import com.mobile.mapper.InventoryMapper;
import com.mobile.mapper.SaleMapper;
import com.mobile.mapper.ShopMapper;
import com.mobile.service.ReportService;
//import com.mobile.dto.GoodsSalesDTO;
import com.mobile.mapper.OrderMapper;
import com.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private SaleMapper saleMapper;

//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private WorkspaceService workspaceService;



    public OrderReportVO getOrderStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("day", date);
            map.put("shopIds", shopIds);

            Integer turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0 : turnover;
            orderList.add(turnover);
        }

        //封装返回结果
        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderList(StringUtils.join(orderList, ","))
                .build();
    }

    public InventoryReportVO getInventoryStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> inventoryList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("day", date);
            map.put("shopIds", shopIds);

            Integer turnover = inventoryMapper.sumByMap(map);
            turnover = turnover == null ? 0 : turnover;
            inventoryList.add(turnover);
        }

        int sum = inventoryList.stream()
                .mapToInt(Integer::intValue) // 将Integer转换为int
                .sum();

        //封装返回结果
        return InventoryReportVO
                .builder()
                .datatotal(sum)
                .dateList(StringUtils.join(dateList, ","))
                .inventoryList(StringUtils.join(inventoryList, ","))
                .build();
    }

    public InventoryReportVO inventoryPrinceStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

        params.clear();
        params.put("shopIds", shopIds);
        params.put("begin", begin);
        params.put("end", end);
        List<Map<String, Object>> stockStatistics = inventoryMapper.queryStockStatistics(params);

        // 处理查询结果并封装返回结果
        List<String> pricelist = new ArrayList<>();
        List<String> inventoryList = new ArrayList<>();
        List<String> proportionlist = new ArrayList<>();

        int totalInventory = 0;
        for (Map<String, Object> r : stockStatistics) {
            totalInventory += ((BigDecimal) r.get("total_quantity")).intValue();
        }
        for (Map<String, Object> row : stockStatistics) {
            // 处理查询结果并填充 pricelist、inventoryList 和 proportionlist
            // 根据 type 和 price 获取对应的价格段
            String price = row.get("price").toString();
            String type = row.get("type").toString();
            String priceSegment = type + "-" + price;

            // 获取库存数量
            int inventory = ((BigDecimal) row.get("total_quantity")).intValue();

            pricelist.add(priceSegment);
            inventoryList.add(String.valueOf(inventory));

            // 计算当前价格段占比
            double proportion = (double) inventory / totalInventory;
            proportionlist.add(String.format("%.2f%%", proportion * 100));
        }

        //封装返回结果
        return InventoryReportVO
                .builder()
                .pricelist(StringUtils.join(pricelist, ","))
                .inventoryList(StringUtils.join(inventoryList, ","))
                .proportionlist(StringUtils.join(proportionlist, ","))
                .build();
    }

    public InventoryReportVO inventoryModelStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

        params.clear();
        params.put("shopIds", shopIds);
        params.put("begin", begin);
        params.put("end", end);
        List<Map<String, Object>> stockStatistics = inventoryMapper.queryStockStatistics(params);

        // 统计每种机型的库存数量
        Map<String, Integer> modelInventoryMap = new HashMap<>();
        for (Map<String, Object> row : stockStatistics) {
            String model = (String) row.get("type");
            int inventory = ((BigDecimal) row.get("total_quantity")).intValue();
            modelInventoryMap.put(model, modelInventoryMap.getOrDefault(model, 0) + inventory);
        }

        // 按库存数量降序排序机型
        List<String> topModels = modelInventoryMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 处理查询结果并封装返回结果
        List<String> modellist = new ArrayList<>();
        List<String> inventoryList = new ArrayList<>();
        List<String> proportionlist = new ArrayList<>();
        List<String> pricelist = new ArrayList<>();

        int totalInventory = 0;
        for (Map<String, Object> r : stockStatistics) {
            totalInventory += ((BigDecimal) r.get("total_quantity")).intValue();
        }

        // 按照topModels的顺序填充结果列表
        for (String model : topModels) {
            for (Map<String, Object> row : stockStatistics) {
                if (model.equals(row.get("type"))) {
                    int inventory = ((BigDecimal) row.get("total_quantity")).intValue();

                    // 填充 modellist
                    modellist.add(model);

                    // 填充 inventoryList
                    inventoryList.add(String.valueOf(inventory));

                    // 计算当前机型占比
                    double proportion = (double) inventory / totalInventory * 100;
                    proportionlist.add(String.format("%.2f%%", proportion));

                    // 根据 price 获取对应的价格段
                    String price = row.get("price").toString();

                    // 填充 price
                    pricelist.add(price);
                }
            }
        }

        // 封装返回结果
        return InventoryReportVO
                .builder()
                .modellist(StringUtils.join(modellist, ","))
                .inventoryList(StringUtils.join(inventoryList, ","))
                .proportionlist(StringUtils.join(proportionlist, ","))
                .pricelist(StringUtils.join(pricelist, ","))
                .build();
    }

    public SaleReportVO distributionStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> saleslist = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("day", date);
            map.put("shopIds", shopIds);

            Integer turnover = saleMapper.sumByMap(map);
            turnover = turnover == null ? 0 : turnover;
            saleslist.add(turnover);
        }

        //封装返回结果
        return SaleReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .saleslist(StringUtils.join(saleslist, ","))
                .build();
    }

    public SaleReportVO contractStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {


        BigDecimal goldCoinContract = BigDecimal.valueOf(12.3);
        BigDecimal surplusYield = BigDecimal.valueOf(64.1);
        BigDecimal installmentContract = BigDecimal.valueOf(83.1);

        //封装返回结果
        return SaleReportVO
                .builder()
                .goldCoinContract(goldCoinContract)
                .surplusYield(surplusYield)
                .installmentContract(installmentContract)
                .build();
    }


    /**
     * 导出运营数据报表
     * @param response
     */
//    public void exportBusinessData(HttpServletResponse response) {
//        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
//        LocalDate dateBegin = LocalDate.now().minusDays(30);
//        LocalDate dateEnd = LocalDate.now().minusDays(1);
//
//        //查询概览数据
//        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));
//
//        //2. 通过POI将数据写入到Excel文件中
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
//
//        try {
//            //基于模板文件创建一个新的Excel文件
//            XSSFWorkbook excel = new XSSFWorkbook(in);
//
//            //获取表格文件的Sheet页
//            XSSFSheet sheet = excel.getSheet("Sheet1");
//
//            //填充数据--时间
//            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);
//
//            //获得第4行
//            XSSFRow row = sheet.getRow(3);
//            row.getCell(2).setCellValue(businessDataVO.getTurnover());
//            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
//            row.getCell(6).setCellValue(businessDataVO.getNewUsers());
//
//            //获得第5行
//            row = sheet.getRow(4);
//            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
//            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
//
//            //填充明细数据
//            for (int i = 0; i < 30; i++) {
//                LocalDate date = dateBegin.plusDays(i);
//                //查询某一天的营业数据
//                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
//
//                //获得某一行
//                row = sheet.getRow(7 + i);
//                row.getCell(1).setCellValue(date.toString());
//                row.getCell(2).setCellValue(businessData.getTurnover());
//                row.getCell(3).setCellValue(businessData.getValidOrderCount());
//                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
//                row.getCell(5).setCellValue(businessData.getUnitPrice());
//                row.getCell(6).setCellValue(businessData.getNewUsers());
//            }
//
//            //3. 通过输出流将Excel文件下载到客户端浏览器
//            ServletOutputStream out = response.getOutputStream();
//            excel.write(out);
//
//            //关闭资源
//            out.close();
//            excel.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
