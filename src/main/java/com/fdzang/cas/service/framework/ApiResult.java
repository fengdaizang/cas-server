package com.fdzang.cas.service.framework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    private Long code;
    private String msg;
    private T data;
}
