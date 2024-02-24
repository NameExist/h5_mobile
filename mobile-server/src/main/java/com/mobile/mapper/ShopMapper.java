package com.mobile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ShopMapper {

    List<Long> queryShopIds(Map params);

    String queryChannelByShopId(@Param("shopId") long shopId);

    // 查询所有省份
    List<String> selectAllProvinces();

    // 根据省份查询所有城市
    List<String> selectCitiesByProvince(String province);

    // 根据城市查询所有区县
    List<String> selectCountiesByCity(String city);

    // 根据区县查询所有网格
    List<String> selectGridsByCounty(String county);

    // 根据网格查询所有门店
    List<String> selectHallsByGrid(String grid);

}
