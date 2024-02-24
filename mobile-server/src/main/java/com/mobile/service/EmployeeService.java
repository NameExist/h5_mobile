package com.mobile.service;

import com.mobile.dto.EmployeeLoginDTO;
import com.mobile.entity.Employee;

public interface EmployeeService {

    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
