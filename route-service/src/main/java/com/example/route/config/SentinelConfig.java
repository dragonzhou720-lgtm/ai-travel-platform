package com.example.route.config;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SentinelConfig {

    public static final String ROUTE_GENERATE_RESOURCE = "route:generate";
    public static final String AI_SERVICE_RESOURCE = "ai-service";
    public static final String ATTRACTION_SERVICE_RESOURCE = "attraction-service";
    public static final String HOTEL_SERVICE_RESOURCE = "hotel-service";

    @PostConstruct
    public void initSentinelRules() {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule routeGenerateRule = new FlowRule();
        routeGenerateRule.setResource(ROUTE_GENERATE_RESOURCE);
        routeGenerateRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        routeGenerateRule.setCount(10);
        routeGenerateRule.setLimitApp("default");
        rules.add(routeGenerateRule);

        FlowRule aiServiceRule = new FlowRule();
        aiServiceRule.setResource(AI_SERVICE_RESOURCE);
        aiServiceRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        aiServiceRule.setCount(5);
        aiServiceRule.setLimitApp("default");
        rules.add(aiServiceRule);

        FlowRule attractionServiceRule = new FlowRule();
        attractionServiceRule.setResource(ATTRACTION_SERVICE_RESOURCE);
        attractionServiceRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        attractionServiceRule.setCount(20);
        attractionServiceRule.setLimitApp("default");
        rules.add(attractionServiceRule);

        FlowRule hotelServiceRule = new FlowRule();
        hotelServiceRule.setResource(HOTEL_SERVICE_RESOURCE);
        hotelServiceRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        hotelServiceRule.setCount(20);
        hotelServiceRule.setLimitApp("default");
        rules.add(hotelServiceRule);

        FlowRuleManager.loadRules(rules);
        log.info("Sentinel flow rules initialized");
    }

    public static Entry enter(String resource) throws BlockException {
        return SphU.entry(resource, EntryType.OUT);
    }

    public static void exit(Entry entry) {
        if (entry != null) {
            entry.exit();
        }
    }

    public static void exit(Entry entry, Throwable t) {
        if (entry != null) {
            entry.exit(1, t);
        }
    }
}