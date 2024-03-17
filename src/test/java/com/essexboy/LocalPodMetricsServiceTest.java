package com.essexboy;

import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
@Disabled
class LocalPodMetricsServiceTest {

    @Autowired
    private PodMetricsService podMetricsService;

    @Test
    public void metrics() throws IOException, ApiException {
        final List<MetricsDto> metrics = podMetricsService.metrics();
        System.out.println(metrics);
    }
}