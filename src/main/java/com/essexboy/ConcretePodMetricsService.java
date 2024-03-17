package com.essexboy;

import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Setter
@Profile({"default", "local"})
public class ConcretePodMetricsService implements PodMetricsService {

    final static Logger LOGGER = LoggerFactory.getLogger(ConcretePodMetricsService.class);

    @Autowired
    private ApiClientWrapper apiClientWrapper;

    @Override
    public List<MetricsDto> metrics() throws ApiException, IOException {
        Configuration.setDefaultApiClient(apiClientWrapper.getClient());
        CoreV1Api api = new CoreV1Api();
        Map<String, V1Pod> podAppNames = new HashMap<>();
        final V1PodList v1PodList = api.listNamespacedPod(apiClientWrapper.getNamespace()).execute();
        v1PodList.getItems().forEach(v1Pod -> {
                    podAppNames.put(v1Pod.getMetadata().getName(), v1Pod);
                }
        );

        List<MetricsDto> metricsDtos = new ArrayList<>();
        Metrics metrics = new Metrics(apiClientWrapper.getClient());
        for (PodMetrics item : metrics.getPodMetrics(apiClientWrapper.getNamespace()).getItems()) {
            final String podName = item.getMetadata().getName();
            final V1Pod v1Pod = podAppNames.get(podName);
            final String app = v1Pod.getMetadata().getLabels().get("app");
            if (item.getContainers() == null) {
                continue;
            }
            for (ContainerMetrics containerMetrics : item.getContainers()) {
                final String containerName = containerMetrics.getName();
                v1Pod.getSpec().getContainers().forEach(v1Container -> {
                    if (containerName.equals(v1Container.getName()) && app != null && v1Container.getResources() != null
                            && !v1Container.getResources().getRequests().isEmpty()
                            && !v1Container.getResources().getLimits().isEmpty()
                    ) {
                        metricsDtos.add(new MetricsDto(podName, app, containerMetrics, v1Container));
                    }
                });
            }
        }
        return metricsDtos;
    }
}
