package org.example.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import org.example.domain.PublishEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Properties;

@RestController
@RequestMapping("/nacosPublish")
public class NacosPublish {

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostMapping("/publishConfig")
    public boolean publishConfig(@RequestBody PublishEntity publishEntity) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
        ConfigService configService = NacosFactory.createConfigService(properties);
        FlowRule rule = new FlowRule(publishEntity.getResource());
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        return configService.publishConfig("nacos-provider-flow-rules", nacosDiscoveryProperties.getGroup(),
                JSON.toJSONString(Collections.singletonList(rule)), ConfigType.JSON.getType());
    }
}
