package org.example.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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

    @GetMapping("/kenTest/{aaa}")
    public String justTest(@PathVariable("aaa") String aaa) {
        // 通过RestTemplate调用其他component的接口...
        return aaa + "-" + port;
    }

    @GetMapping("/testHot")
    @SentinelResource("hot")
    public String justTestB(@RequestParam(required = false) String param1,
                            @RequestParam(required = false) String param2) {
        return param1 + "-" + param2;
    }
}
