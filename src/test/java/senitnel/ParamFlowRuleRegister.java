package senitnel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.example.SentinelTestDemoApplication;
import org.example.domain.SourceCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParamFlowRuleRegister {

    private static final String RESOURCE_KEY = "hot";

    public static final String MR_PLUS = "MRPlus";
    public static final String OTHER = "other";

    public static final int totalQPS = 50;

    public static void main(String[] args) {
        ParamFlowRule rule = new ParamFlowRule(RESOURCE_KEY)
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setDurationInSec(1)
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
                .setCount(2);

        //start 经过计算后 省略计算过程。。。获取MR++的当前QPS
        //start 经过计算后 省略计算过程。。。获取OTHER的当前QPS

        List<SourceCategory> sourceCategories = new ArrayList<>();
        //order 1
        sourceCategories.add(new SourceCategory(MR_PLUS));
        //order 2
        sourceCategories.add(new SourceCategory(OTHER));

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
    }
}
