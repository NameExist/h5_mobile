package com.mobile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录返回的数据格式")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主键值")
    private Long userid;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("工号")
    private String number;

    @ApiModelProperty("jwt令牌")
    private String token;

    @ApiModelProperty("权限")
    private String authority;

}
