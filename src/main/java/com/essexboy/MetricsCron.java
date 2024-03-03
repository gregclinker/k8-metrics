package com.essexboy;

import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MetricsCron {
    final static Logger LOGGER = LoggerFactory.getLogger(MetricsCron.class);
    @Autowired
    private MetricsService metricsService;
    @Autowired
    private PodMetricsService podMetricsService;

    @Scheduled(fixedRate = 30000L)
    public void cron() throws ApiException, IOException {
        List<String> validMetricKeys = new ArrayList<>();
        final List<MetricsDto> metrics = podMetricsService.metrics();
        LOGGER.debug("found {} metrics", metrics);
        for (MetricsDto metricsDto : metrics) {
            if (metricsDto.getApp() != null) {
                final Map<String, String> tags = Map.of("app", metricsDto.getApp(), "pod", metricsDto.getPodName(), "container", metricsDto.getContainerName());
                validMetricKeys.add(metricsService.setMetric("cpu_usage", "cpu usage in CPU", tags, metricsDto.getCpuUsage()));
                validMetricKeys.add(metricsService.setMetric("memory_usage", "memory usage in BI bytes", tags, metricsDto.getMemoryUsage()));
                validMetricKeys.add(metricsService.setMetric("cpu_percentage_usage", "cpu percentage usage of limits", tags, metricsDto.getCpuUsagePercentageOfLimits()));
                validMetricKeys.add(metricsService.setMetric("memory_percentage_usage", "memory percentage usage of limits", tags, metricsDto.getMemoryUsagePercentageOfLimits()));
            }
        }
        metricsService.currentKeys(validMetricKeys);
    }
}
