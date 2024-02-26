package com.mobile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface SaleMapper {

    Integer sumByMap(Map map);

    List<Map<String, Object>> querySalesByDateAndShopIds(
            @Param("date") LocalDate date,
            @Param("shopIds") List<Long> shopIds
    );

    List<Map<String, Object>> querySaleStatistics(Map<String, Object> params);

    List<Map<String, Object>> querySaleStatistics2(Map<String, Object> params);

    List<Map<String, Object>> querySalesByShopIdsAndYear(@Param("shopIds") List<Long> shopIds, @Param("year") int year);

}
