package org.example.controller;

import com.alibaba.csp.sentinel.slots.block.ClusterRuleConstant;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.example.domain.SourceCategory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/paramFlowRule")
public class ParamFlowRuleController {

    private static final String RESOURCE_KEY = "hot";

    public static final String MR_PLUS = "MRPlus";
    public static final String OTHER = "other";

    public static final int totalQPS = 50;

    @PostMapping("/add")
    public String addParamFlowRule() {

        ParamFlowClusterConfig clusterConfig = new ParamFlowClusterConfig()
                .setFlowId(Long.valueOf(22222))
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

        //start 经过计算后 省略计算过程。。。获取MR++的当前QPS
        //start 经过计算后 省略计算过程。。。获取OTHER的当前QPS

        List<SourceCategory> sourceCategories = new ArrayList<>();
        //order 1
        sourceCategories.add(SourceCategory.builder()
                .source(MR_PLUS)
                .currentPercent(0.6)
                .build());
        //order 2
        sourceCategories.add(SourceCategory.builder()
                .source(OTHER)
                .currentPercent(0.4)
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
        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));

        return "add success";
    }
}
