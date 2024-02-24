package com.mobile.mapper;

import com.mobile.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface EmployeeMapper {
    @Select("select * from user where name = #{username}")
    Employee getByUsername(String username);

}

