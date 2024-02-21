package com.mobile.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopMapper {

    List<Long> queryShopIds(Map params);

}
