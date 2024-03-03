package com.essexboy;

import io.kubernetes.client.openapi.ApiException;

import java.io.IOException;
import java.util.List;

public interface PodMetricsService {
    List<MetricsDto> metrics() throws ApiException, IOException;
}
