package com.fdzang.cas.service.service;

import com.fdzang.cas.service.framework.ApiResult;

/**
 * @author tanghu
 * @Date: 2020/5/21 11:07
 */
public interface UserService {
    ApiResult userLogin(String username,String password,String appCode);
}
