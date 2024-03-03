package com.essexboy;

import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ResourceRequirements;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Setter
@Profile("test")
public class DummyPodMetricsService implements PodMetricsService {

    public List<MetricsDto> metrics() throws ApiException, IOException {
        List<MetricsDto> metricsDtos = new ArrayList<>();
        metricsDtos.add(new MetricsDto("pod1", "app1", makeContainerMetrics(0.1, 1000.0), makeV1Container("container1", 0.2, 2000.0, 0.4, 8000.0)));
        metricsDtos.add(new MetricsDto("pod2", "app2", makeContainerMetrics(0.1, 1000.0), makeV1Container("container1", 0.2, 2000.0, 0.4, 8000.0)));
        metricsDtos.add(new MetricsDto("pod3", "app3", makeContainerMetrics(0.1, 1000.0), makeV1Container("container1", 0.2, 2000.0, 0.4, 8000.0)));
        return metricsDtos;
    }

    private ContainerMetrics makeContainerMetrics(double cpu, double memory) {
        final ContainerMetrics containerMetrics = new ContainerMetrics();
        Map<String, Quantity> usage = new HashMap<>();
        usage.put("cpu", new Quantity(BigDecimal.valueOf(cpu), Quantity.Format.DECIMAL_SI));
        usage.put("memory", new Quantity(BigDecimal.valueOf(memory), Quantity.Format.BINARY_SI));
        containerMetrics.setUsage(usage);
        return containerMetrics;
    }

    private V1Container makeV1Container(String name, double cpuRequests, double memoryRequests, double cpuLimits, double memoryLimits) {
        final V1Container v1Container = new V1Container();
        v1Container.setName(name);
        Map<String, Quantity> requests = new HashMap<>();
        requests.put("cpu", new Quantity(BigDecimal.valueOf(cpuRequests), Quantity.Format.DECIMAL_SI));
        requests.put("memory", new Quantity(BigDecimal.valueOf(memoryRequests), Quantity.Format.BINARY_SI));
        Map<String, Quantity> limits = new HashMap<>();
        limits.put("cpu", new Quantity(BigDecimal.valueOf(cpuLimits), Quantity.Format.DECIMAL_SI));
        limits.put("memory", new Quantity(BigDecimal.valueOf(memoryLimits), Quantity.Format.BINARY_SI));
        final V1ResourceRequirements v1ResourceRequirements = new V1ResourceRequirements();
        v1ResourceRequirements.setRequests(requests);
        v1ResourceRequirements.setLimits(limits);
        v1Container.setResources(v1ResourceRequirements);
        return v1Container;
    }
}
