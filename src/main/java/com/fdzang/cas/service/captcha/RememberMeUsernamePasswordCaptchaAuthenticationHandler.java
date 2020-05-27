package com.fdzang.cas.service.captcha;

import com.fdzang.cas.service.framework.ApiResult;
import com.fdzang.cas.service.service.UserService;
import com.fdzang.cas.service.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;

@Slf4j
public class RememberMeUsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    private UserService userService;

    public RememberMeUsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        RequestContext requestContext = RequestContextHolder.getRequestContext();
        HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext();

        RememberMeUsernamePasswordCaptchaCredential captchaCredential = (RememberMeUsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = captchaCredential.getCaptcha();
        String username = captchaCredential.getUsername();
        String password = captchaCredential.getPassword();

        // 校验验证码
        Object attribute = request.getSession().getAttribute(Constant.CAPTCHA_SESSION_KEY);
        String realCaptcha = attribute == null ? null : attribute.toString();
        if(StringUtils.isBlank(requestCaptcha) || !requestCaptcha.equalsIgnoreCase(realCaptcha)){
            throw new FailedLoginException("验证码错误");
        }

        // 获取Service信息
        RegisteredService service = WebUtils.getRegisteredService(requestContext);
        String appCode = service.getName();

        // 登录校验
        ApiResult result = userService.userLogin(username,password,appCode);
        if(!result.getCode().equals(0L)){
            throw new FailedLoginException(result.getMsg());
        }

        return createHandlerResult(credential, this.principalFactory.createPrincipal(username));
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof RememberMeUsernamePasswordCaptchaCredential;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}