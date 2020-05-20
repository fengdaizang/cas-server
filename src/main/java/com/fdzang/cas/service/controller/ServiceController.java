package com.fdzang.cas.service.controller;

import com.fdzang.cas.service.domain.ServiceDO;
import com.fdzang.cas.service.framework.ApiResult;
import com.fdzang.cas.service.framework.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.services.RegexRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ReturnAllAttributeReleasePolicy;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author tanghu
 * @Date: 2020/5/19 15:54
 */
@Slf4j
@RestController
@RequestMapping("/service")
public class ServiceController extends BaseController {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @PostMapping
    public ApiResult addService(@RequestBody ServiceDO service) throws Exception {
        RegisteredService registeredService = findByServiceId(service.getServiceId());
        if (registeredService != null) {
            return fail("serviceId:" + service.getServiceId() + " 已存在");
        }

        RegexRegisteredService regexRegisteredService = covertService(service);
        servicesManager.save(regexRegisteredService, true);
        servicesManager.load();

        registeredService = findByServiceId(service.getServiceId());

        return ok(covertRegisteredService(registeredService));
    }

    @DeleteMapping
    public ApiResult delService(@RequestParam("serviceId") String serviceId) {
        boolean flag = false;
        RegisteredService registeredService = findByServiceId(serviceId);
        if (registeredService != null) {
            try {
                servicesManager.delete(registeredService);
            } catch (Exception e) {
                //这里会报审计错误，直接进行捕获即可，不影响删除逻辑
                log.error(e.getMessage());
            }
            if (null == findByServiceId(serviceId)) {
                servicesManager.load();
                flag = true;
            }
        }else{
            return fail("serviceId:" + serviceId + " 不存在");
        }

        if (flag){
            return ok("删除成功");
        }else{
            return fail("删除失败");
        }
    }

    @GetMapping("/all")
    public ApiResult getAllService() {
        Collection<RegisteredService> allServices = servicesManager.getAllServices();

        return ok(covertRegisteredServiceList(allServices));
    }

    @GetMapping
    public ApiResult getByServiceId(@RequestParam("serviceId") String serviceId) {
        RegisteredService service = findByServiceId(serviceId);
        return ok(covertRegisteredService(service));
    }

    private ServiceDO covertRegisteredService(RegisteredService registeredService) {
        ServiceDO service = new ServiceDO();

        service.setServiceId(registeredService.getServiceId());
        service.setDescription(registeredService.getDescription());
        service.setEvaluationOrder(registeredService.getEvaluationOrder());
        service.setId(registeredService.getId());
        service.setName(registeredService.getName());
        service.setTheme(registeredService.getTheme());

        return service;
    }

    private List<ServiceDO> covertRegisteredServiceList(Collection<RegisteredService> registeredServices) {
        if (CollectionUtils.isEmpty(registeredServices)) {
            return null;
        }
        List<ServiceDO> services = new ArrayList<>();
        for (RegisteredService registeredService : registeredServices) {
            services.add(covertRegisteredService(registeredService));
        }

        return services;
    }

    private RegexRegisteredService covertService(ServiceDO service) throws Exception {
        RegexRegisteredService regexRegisteredService = new RegexRegisteredService();

        String serviceId = "^(https|imaps|http)://" + service.getServiceId() + ".*";
        ReturnAllAttributeReleasePolicy returnAllAttributeReleasePolicy = new ReturnAllAttributeReleasePolicy();

        regexRegisteredService.setServiceId(serviceId);
        regexRegisteredService.setId(service.getId());
        regexRegisteredService.setDescription(service.getDescription());
        regexRegisteredService.setEvaluationOrder(service.getEvaluationOrder());
        if (StringUtils.isNotBlank(service.getTheme())) {
            regexRegisteredService.setTheme(service.getTheme());
        }
        regexRegisteredService.setAttributeReleasePolicy(returnAllAttributeReleasePolicy);
        regexRegisteredService.setName(service.getName());
        regexRegisteredService.setLogoutUrl(new URL("http://" + service.getServiceId()));

        return regexRegisteredService;
    }

    public RegisteredService findByServiceId(String serviceId){
        RegisteredService service = null;
        serviceId = "http://" + serviceId;
        try {
            service = servicesManager.findServiceBy(serviceId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return service;
    }
}
