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
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
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

    public ShopReportVO GetRegion() {
        List<String> provinces = shopMapper.selectAllProvinces();
        List<String> cities = new ArrayList<>();
        List<String> counties = new ArrayList<>();
        List<String> grids = new ArrayList<>();
        List<String> halls = new ArrayList<>();

        for (String province : provinces) {
            cities.addAll(shopMapper.selectCitiesByProvince(province));
        }

        for (String city : cities) {
            counties.addAll(shopMapper.selectCountiesByCity(city));
        }

        for (String county : counties) {
            grids.addAll(shopMapper.selectGridsByCounty(county));
        }

        for (String grid : grids) {
            halls.addAll(shopMapper.selectHallsByGrid(grid));
        }

        //封装返回结果
        return ShopReportVO
                .builder()
                .province(StringUtils.join(provinces, ","))
                .city(StringUtils.join(cities, ","))
                .county(StringUtils.join(counties, ","))
                .grid(StringUtils.join(grids, ","))
                .hall(StringUtils.join(halls, ","))
                .build();
    }

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

        BigDecimal goldCoinContract = BigDecimal.ZERO;
        BigDecimal surplusYield = BigDecimal.ZERO;
        BigDecimal installmentContract = BigDecimal.ZERO;

        for (LocalDate date : dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("shopIds", shopIds);

            List<Map<String, Object>> sales = saleMapper.querySalesByDateAndShopIds(date, shopIds);

            for (Map<String, Object> sale : sales) {
                String contractType = (String) sale.get("sale_contract");
                int count = ((Number) sale.get("count")).intValue();

                if ("金币合约".equals(contractType)) {
                    goldCoinContract = goldCoinContract.add(BigDecimal.valueOf(count));
                } else if ("顺差让利".equals(contractType)) {
                    surplusYield = surplusYield.add(BigDecimal.valueOf(count));
                } else if ("分期合约".equals(contractType)) {
                    installmentContract = installmentContract.add(BigDecimal.valueOf(count));
                }
            }
        }

        BigDecimal total = goldCoinContract.add(surplusYield).add(installmentContract);

        if (total.compareTo(BigDecimal.ZERO) != 0) {
            goldCoinContract = goldCoinContract.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            surplusYield = surplusYield.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            installmentContract = installmentContract.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        //封装返回结果
        return SaleReportVO
                .builder()
                .goldCoinContract(goldCoinContract)
                .surplusYield(surplusYield)
                .installmentContract(installmentContract)
                .build();
    }


    public SaleReportVO priceStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
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
        List<Map<String, Object>> saleStatistics = saleMapper.querySaleStatistics(params);

        // 处理查询结果并封装返回结果
        List<String> pricelist = new ArrayList<>();
        List<String> saleslist = new ArrayList<>();
        List<String> proportionlist = new ArrayList<>();

        int totalInventory = 0;
        for (Map<String, Object> r : saleStatistics) {
            totalInventory += ((Long) r.get("total_quantity")).intValue();
        }
        for (Map<String, Object> row : saleStatistics) {
            // 处理查询结果并填充 pricelist、inventoryList 和 proportionlist
            // 根据 type 和 price 获取对应的价格段
            String price = row.get("price").toString();
            String type = row.get("type").toString();
            String priceSegment = type + "-" + price;

            // 获取库存数量
            int inventory = ((Long) row.get("total_quantity")).intValue();

            pricelist.add(priceSegment);
            saleslist.add(String.valueOf(inventory));

            // 计算当前价格段占比
            double proportion = (double) inventory / totalInventory;
            proportionlist.add(String.format("%.2f%%", proportion * 100));
        }

        //封装返回结果
        return SaleReportVO
                .builder()
                .pricelist(StringUtils.join(pricelist, ","))
                .saleslist(StringUtils.join(saleslist, ","))
                .proportionlist(StringUtils.join(proportionlist, ","))
                .build();
    }

    public SaleReportVO modelStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {
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
        List<Map<String, Object>> stockStatistics = saleMapper.querySaleStatistics(params);

        // 统计每种机型的库存数量
        Map<String, Integer> modelInventoryMap = new HashMap<>();
        for (Map<String, Object> row : stockStatistics) {
            String model = (String) row.get("type");
            int inventory = ((Long) row.get("total_quantity")).intValue();
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
        List<String> saleslist = new ArrayList<>();
        List<String> proportionlist = new ArrayList<>();
        List<String> pricelist = new ArrayList<>();

        int totalInventory = 0;
        for (Map<String, Object> r : stockStatistics) {
            totalInventory += ((Long) r.get("total_quantity")).intValue();
        }

        // 按照topModels的顺序填充结果列表
        for (String model : topModels) {
            for (Map<String, Object> row : stockStatistics) {
                if (model.equals(row.get("type"))) {
                    int inventory = ((Long) row.get("total_quantity")).intValue();

                    // 填充 modellist
                    modellist.add(model);

                    // 填充 inventoryList
                    saleslist.add(String.valueOf(inventory));

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
        return SaleReportVO
                .builder()
                .modellist(StringUtils.join(modellist, ","))
                .saleslist(StringUtils.join(saleslist, ","))
                .proportionlist(StringUtils.join(proportionlist, ","))
                .pricelist(StringUtils.join(pricelist, ","))
                .build();
    }

    public SaleReportVO qualityStatistics(String province, String city, String county, String grid, String hall, LocalDate begin, LocalDate end) {

        BigDecimal rate1 = BigDecimal.valueOf(56.6);
        BigDecimal rate2 = BigDecimal.valueOf(21.1);
        BigDecimal rate3 = BigDecimal.valueOf(5.6);

        //封装返回结果
        return SaleReportVO
                .builder()
                .rate1(rate1)
                .rate2(rate2)
                .rate3(rate3)
                .build();
    }


    public SaleReportVO channelStatistics(String province, String city, String county, String grid, String hall, int year) {
        Map params = new HashMap();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("grid", grid);
        params.put("hall", hall);
        List<Long> shopIds = shopMapper.queryShopIds(params);

//        params.clear();
//        params.put("shopIds", shopIds);
//        params.put("year", year.getYear());

        List<Map<String, Object>> sales = saleMapper.querySalesByShopIdsAndYear(shopIds, year);

        Map<Long, String> channelMap = new HashMap<>();
        for (Long shopId : shopIds) {
            String channel = shopMapper.queryChannelByShopId(shopId);
            channelMap.put(shopId, channel);
        }

        int totalChannelCount = shopIds.size(); // 渠道总数量

        Map<String, Integer> channelTypeCountMap1 = new HashMap<>();
        Map<String, Integer> channelTypeCountMap2 = new HashMap<>();
        Map<String, Integer> channelTypeCountMap3 = new HashMap<>();

        for (Map<String, Object> sale : sales) {
            Long shopId = (Long) sale.get("shop_id");
            Long saleCount = (Long) sale.get("sale_count");
            String channel = channelMap.get(shopId);

            if (channelTypeCountMap1.containsKey(channel)) {
                int count = channelTypeCountMap1.get(channel);
                channelTypeCountMap1.put(channel, count + 1);
            } else {
                channelTypeCountMap1.put(channel, 1);
            }

            // 判断是否有销渠道
            if (saleCount >= 1) {
                if (channelTypeCountMap2.containsKey(channel)) {
                    int count = channelTypeCountMap2.get(channel);
                    channelTypeCountMap2.put(channel, count + 1);
                } else {
                    channelTypeCountMap2.put(channel, 1);
                }
            }

            // 判断是否有效渠道
            long averageSaleCount = saleCount / Month.values().length; // 终端月均销量
            if (averageSaleCount >= 5) {
                if (channelTypeCountMap3.containsKey(channel)) {
                    int count = channelTypeCountMap3.get(channel);
                    channelTypeCountMap3.put(channel, count + 1);
                } else {
                    channelTypeCountMap3.put(channel, 1);
                }
            }

        }

        // 遍历channelTypeCountMap1，并将值添加到channellist和channelnumber列表中
        List<String> channellist = new ArrayList<>();
        List<String> channelnumber = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : channelTypeCountMap1.entrySet()) {
            channellist.add(entry.getKey());
            channelnumber.add(String.valueOf(entry.getValue()));
        }

        // 遍历channelTypeCountMap2，并将值添加到availablenumber列表中
        List<String> availablenumber = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : channelTypeCountMap2.entrySet()) {
            availablenumber.add(String.valueOf(entry.getValue()));
        }

        // 遍历channelTypeCountMap3，并将值添加到effectivenumber列表中
        List<String> effectivenumber = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            effectivenumber.add("0");
        }
        int cnt3=0;
        for (Map.Entry<String, Integer> entry : channelTypeCountMap3.entrySet()) {
            effectivenumber.set(cnt3,String.valueOf(entry.getValue()));
            cnt3++;
        }

        List<String> availablerate = new ArrayList<>();
        List<String> effectiverate = new ArrayList<>();

        if (channelnumber.size() == availablenumber.size()) {
            for (int i = 0; i < channelnumber.size(); i++) {
                try {
                    double aValue = Double.parseDouble(channelnumber.get(i));
                    double bValue = Double.parseDouble(availablenumber.get(i));
                    if (bValue != 0) {
                        double result = bValue / aValue;
                        availablerate.add(String.valueOf(result));
                    } else {
                        // 处理除数为0的情况
                        availablerate.add("NaN");
                    }
                } catch (NumberFormatException e) {
                    // 处理非法输入的情况
                    availablerate.add("Invalid input");
                }
            }
        } else {
            // 如果 a 和 b 的长度不一致，进行相应的处理
            System.out.println("a 和 b 的长度不一致");
        }

        if (channelnumber.size() == effectivenumber.size()) {
            for (int i = 0; i < channelnumber.size(); i++) {
                try {
                    double aValue = Double.parseDouble(channelnumber.get(i));
                    double bValue = Double.parseDouble(effectivenumber.get(i));
                    if (bValue != 0) {
                        double result = bValue / aValue;
                        effectiverate.add(String.valueOf(result));
                    } else {
                        // 处理除数为0的情况
                        effectiverate.add("NaN");
                    }
                } catch (NumberFormatException e) {
                    // 处理非法输入的情况
                    effectiverate.add("Invalid input");
                }
            }
        } else {
            // 如果 a 和 b 的长度不一致，进行相应的处理
            System.out.println("a 和 b 的长度不一致");
        }


        // 封装返回结果
        return SaleReportVO.builder()
                .channellist(StringUtils.join(channellist, ","))
                .channelnumber(StringUtils.join(channelnumber, ","))
                .availablenumber(StringUtils.join(availablenumber, ","))
                .availablerate(StringUtils.join(availablerate, ","))
                .effectivenumber(StringUtils.join(effectivenumber, ","))
                .effectiverate(StringUtils.join(effectiverate, ","))
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
