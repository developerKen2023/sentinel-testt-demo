package org.example.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Value("${server.port}")
    private String port;

    @Value("${origin.source}")
    private String originSource;

    @GetMapping("/justTestA")
    public String justTestA(){
        Entry entry = null;
        try {
            String resourceName = "/kenTest/{aaa}";
            ContextUtil.enter(resourceName, originSource);
            entry = SphU.entry(resourceName);
            //被保护的逻辑
            return originSource + "-" + port;
        } catch (BlockException e) {
            log.info("限流或者降级了...", e);
            return "限流或者降级了...";
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/kenTest/{aaa}")
    public String justTest(@PathVariable("aaa") String aaa) {
        return aaa + "-" + port;
    }

    @GetMapping("/kenTest")
    public String justTestB(@RequestParam("param") String param) {
        return param + "-" + port;
    }
}
