package com.mobile.service.impl;

import com.mobile.dto.EmployeeLoginDTO;
import com.mobile.entity.Employee;
import com.mobile.exception.PasswordErrorException;
import com.mobile.mapper.EmployeeMapper;
import com.mobile.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    public static final String PASSWORD_ERROR = "密码错误";

    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getName();
        String password = employeeLoginDTO.getPassword();

        //根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        int authority = Character.getNumericValue(employee.getNumber().charAt(0));

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "河北");
        map.put(2, "石家庄");
        map.put(3, "石家庄晋州");
        map.put(4, "晋州县城北网格");

        employee.setAuthority(map.get(authority));
        //密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(PASSWORD_ERROR);
        }

        //返回实体对象
        return employee;
    }
}
