package com.mobile.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface OrderMapper {

    Integer sumByMap(Map map);

}
