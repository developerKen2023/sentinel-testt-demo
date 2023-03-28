package org.example.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.csp.sentinel.slots.block.ClusterRuleConstant;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import org.example.domain.SourceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/paramFlowRule")
public class ParamFlowRuleController {

    private static final String RESOURCE_KEY = "hot";

    public static final String MR_PLUS = "MRPlus";
    public static final String OTHER = "other";

    public static final int totalQPS = 50;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostMapping("/add")
    public boolean addParamFlowRule() throws NacosException {

        ParamFlowClusterConfig clusterConfig = new ParamFlowClusterConfig()
                .setFlowId(33333l)
                .setFallbackToLocalWhenFail(true)
                .setThresholdType(ClusterRuleConstant.FLOW_THRESHOLD_GLOBAL);

        ParamFlowRule rule = new ParamFlowRule(RESOURCE_KEY)
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setDurationInSec(1)
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
                .setCount(totalQPS)
                .setClusterMode(true)
                .setClusterConfig(clusterConfig);

        //TODO start 经过计算后 省略计算过程。。。获取MR++的当前QPS
        //TODO start 经过计算后 省略计算过程。。。获取OTHER的当前QPS
        //TODO how to find two services interaction

        double num = Math.random();

        List<SourceCategory> sourceCategories = new ArrayList<>();
        //order 1
        sourceCategories.add(SourceCategory.builder()
                .source(MR_PLUS)
                .currentPercent(1 - num)
                .build());
        //order 2
        sourceCategories.add(SourceCategory.builder()
                .source(OTHER)
                .currentPercent(num)
                .build());

        List<ParamFlowItem> paramFlowItems = new ArrayList<>();

        for (SourceCategory sourceCategory : sourceCategories) {
            ParamFlowItem item = new ParamFlowItem()
                    .setObject(sourceCategory.getSource())
                    .setClassType(String.class.getName())
                    .setCount((int) Math.round(totalQPS * sourceCategory.getCurrentPercent()));
            paramFlowItems.add(item);
        }

        rule.setParamFlowItemList(paramFlowItems);
        //ParamFlowRuleManager.loadRules(Collections.singletonList(rule));

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
        ConfigService configService = NacosFactory.createConfigService(properties);

        return configService.publishConfig("nacos-provider-param-rules", nacosDiscoveryProperties.getGroup(),
                JSON.toJSONString(Collections.singleton(rule)), ConfigType.JSON.getType());
    }
}
