package com.essexboy;

import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DummyPodMetricsServiceTest {

    @Autowired
    private PodMetricsService podMetricsService;

    @Test
    public void metrics() throws IOException, ApiException {
        final List<MetricsDto> metrics = podMetricsService.metrics();
        final MetricsDto metricsDto = metrics.get(0);
        assertEquals("app1", metricsDto.getApp());
        assertEquals("pod1", metricsDto.getPodName());
        assertEquals("container1", metricsDto.getContainerName());
        assertEquals(0.1, metricsDto.getCpuUsage());
        assertEquals(1000.0, metricsDto.getMemoryUsage());
        assertEquals(0.2, metricsDto.getCpuRequests());
        assertEquals(2000.0, metricsDto.getMemoryRequests());
        assertEquals(0.4, metricsDto.getCpuLimits());
        assertEquals(8000.0, metricsDto.getMemoryLimits());
        assertEquals(25.0, metricsDto.getCpuUsagePercentageOfLimits());
        assertEquals(12.5, metricsDto.getMemoryUsagePercentageOfLimits());
    }
}