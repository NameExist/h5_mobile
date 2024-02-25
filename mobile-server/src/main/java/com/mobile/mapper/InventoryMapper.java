package com.mobile.mapper;

import com.mobile.vo.InventoryReportVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface InventoryMapper {
    Integer sumByMap(Map map);

    List<Map<String, Object>> queryStockStatistics(Map<String, Object> params);

    List<Map<String, Object>> queryStockStatistics2(Map<String, Object> params);
}

