package com.mobile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TerminalMapper {
    @Select("SELECT type FROM terminal WHERE terminal_id = #{terminalId}")
    String getTypeById(@Param("terminalId") String terminalId);

    @Select("SELECT price FROM terminal WHERE terminal_id = #{terminalId}")
    String getPriceById(@Param("terminalId") String terminalId);
}