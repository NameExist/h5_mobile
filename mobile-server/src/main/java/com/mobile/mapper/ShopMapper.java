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

}
