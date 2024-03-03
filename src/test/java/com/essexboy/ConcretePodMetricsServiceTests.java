package com.essexboy;

import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("local")
@Disabled
class ConcretePodMetricsServiceTests {

    @Autowired
    private PodMetricsService podMetricsService;

    @Autowired
    private ApiClientWrapper apiClientWrapper;

    @BeforeAll
    public static void init() {
        System.setProperty("USE_GKE_GCLOUD_AUTH_PLUGIN", "True");
    }

    @Test
    public void test2() throws IOException, ApiException {
        final List<MetricsDto> metrics = podMetricsService.metrics();
        assertNotNull(metrics);
        assertTrue(metrics.size() > 0);
    }

    @Test
    void test1() throws IOException, ApiException {
        final String namespace = "fake-load";
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        Map<String, String> podAppNames = new HashMap<>();

        CoreV1Api api = new CoreV1Api();
        final V1PodList v1PodList = api.listNamespacedPod(namespace).execute();
        v1PodList.getItems().forEach(v1Pod -> {
                    podAppNames.put(v1Pod.getMetadata().getName(), v1Pod.getMetadata().getLabels().get("app"));
                }
        );

        Metrics metrics = new Metrics(client);
        for (PodMetrics item : metrics.getPodMetrics(namespace).getItems()) {
            final String podName = item.getMetadata().getName();
            final String app = podAppNames.get(podName);
            if (item.getContainers() == null) {
                continue;
            }
            for (ContainerMetrics container : item.getContainers()) {
                final String containerName = container.getName();
                final double cpu = container.getUsage().get("cpu").getNumber().doubleValue();
                final double memory = container.getUsage().get("memory").getNumber().doubleValue();
                //final MetricsDto metricsDto = new MetricsDto(app, podName, containerName, cpu, memory);
                //System.out.println(metricsDto);
            }
        }
    }
}
