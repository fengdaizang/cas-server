package com.fdzang.cas.service.domain;

/**
 * @author tanghu
 * @Date: 2020/5/19 15:58
 */
public class ServiceDO {
    /**
     * (必填)serviceId 对服务进行描述的表达式，可用于匹配一个或多个 URL 地址
     * 这里传入的serviceId为客户端的host
     * 存库的serviceId为："^(https|imaps|http)://"+serviceId+".*"
     */
    private String serviceId;
    /**
     * (必填)name 服务名称
     */
    private String name;
    /**
     * (不填)id 全局唯一标志，数据库自增
     */
    private Long id;
    /**
     * (选填)description 服务描述，会显示在默认登录页
     */
    private String description;
    /**
     * (必填)evaluationOrder 定义多个服务的执行顺序
     */
    private Integer evaluationOrder;
    /**
     * (选填)theme 服务登录的主题
     */
    private String theme;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEvaluationOrder() {
        return evaluationOrder;
    }

    public void setEvaluationOrder(Integer evaluationOrder) {
        this.evaluationOrder = evaluationOrder;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
