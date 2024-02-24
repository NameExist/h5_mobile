package com.mobile.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private Long userid;

    private String name;

    private String number;

    private String password;
}
