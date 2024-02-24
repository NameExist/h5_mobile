//package com.mobile.controller.admin;
//
//import com.mobile.dto.EmployeeLoginDTO;
//import com.mobile.entity.Employee;
//import com.mobile.result.Result;
//import com.mobile.service.EmployeeService;
//import com.mobile.vo.EmployeeLoginVO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 员工管理
// */
//@RestController
//@RequestMapping("/mobile")
//@Slf4j
//@Api(tags = "员工相关接口")
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @Autowired
//    private JwtProperties jwtProperties;
//
//    @PostMapping("/login")
//    @ApiOperation(value = "员工登录")
//    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
//        log.info("员工登录：{}", employeeLoginDTO);
//
//        Employee employee = employeeService.login(employeeLoginDTO);
//
//        //登录成功后，生成jwt令牌
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
//        String token = JwtUtil.createJWT(
//                jwtProperties.getAdminSecretKey(),
//                jwtProperties.getAdminTtl(),
//                claims);
//
//        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
//                .id(employee.getId())
//                .userName(employee.getUsername())
//                .name(employee.getName())
//                .token(token)
//                .build();
//
//        return Result.success(employeeLoginVO);
//    }
//
//    @PostMapping("/logout")
//    @ApiOperation("员工退出")
//    public Result<String> logout() {
//        return Result.success();
//    }
//
//}
