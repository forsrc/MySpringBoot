package com.forsrc.boot.hystrix.config;

import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTurbine
@EnableHystrixDashboard
public class HystrixConfig {

}
