package com.essexboy;

import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.openapi.models.V1Container;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MetricsDto {
    private String app;
    private String podName;
    private String containerName;
    private double cpuUsage;
    private double memoryUsage;
    private double cpuRequests;
    private double memoryRequests;
    private double cpuLimits;
    private double memoryLimits;

    public MetricsDto(String podName, String app, ContainerMetrics containerMetrics, V1Container v1Container) {
        this.podName = podName;
        this.app = app;
        this.containerName = v1Container.getName();
        this.cpuUsage = containerMetrics.getUsage().get("cpu").getNumber().doubleValue();
        this.memoryUsage = containerMetrics.getUsage().get("memory").getNumber().doubleValue();
        this.cpuRequests = v1Container.getResources().getRequests().get("cpu").getNumber().doubleValue();
        this.memoryRequests = v1Container.getResources().getRequests().get("memory").getNumber().doubleValue();
        this.cpuLimits = v1Container.getResources().getLimits().get("cpu").getNumber().doubleValue();
        this.memoryLimits = v1Container.getResources().getLimits().get("memory").getNumber().doubleValue();
    }

    public double getCpuUsagePercentageOfLimits() {
        return (cpuUsage / cpuLimits) * 100.0;
    }

    public double getMemoryUsagePercentageOfLimits() {
        return (memoryUsage / memoryLimits) * 100.0;
    }
}
