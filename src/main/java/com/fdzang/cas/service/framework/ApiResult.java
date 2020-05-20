package com.fdzang.cas.service.framework;

import lombok.Data;

@Data
public class ApiResult<T> {
    private Long code;
    private String msg;
    private T data;
}
