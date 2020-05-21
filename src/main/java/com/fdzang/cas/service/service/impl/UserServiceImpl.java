package com.fdzang.cas.service.service.impl;

import com.fdzang.cas.service.framework.ApiResult;
import com.fdzang.cas.service.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author tanghu
 * @Date: 2020/5/21 11:07
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public ApiResult userLogin(String username, String password, String appCode) {
        if (username.equals("admin")){
            return new ApiResult(0L,"成功","asasdsdadsd");
        }
        return new ApiResult(500L,"成功","asasdsdadsd");
    }
}
