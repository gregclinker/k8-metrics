package com.essexboy;

import io.micrometer.core.instrument.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class MetricsService {

    final static Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

    @Autowired
    MeterRegistry registry;

    private static final Map<String, Double> METRICS_MAP = new HashMap<>();

    public void currentKeys(List<String> currentKeys) {
        final List<String> staleKeys = METRICS_MAP.keySet().stream().filter(key -> !currentKeys.contains(key)).collect(Collectors.toList());
        if (!staleKeys.isEmpty()) {
            LOGGER.debug("purging stale keys {}", staleKeys);
            METRICS_MAP.entrySet().removeIf(e -> staleKeys.contains(e.getKey()));
        }
    }

    public String setMetric(String metricName, String help, Map<String, String> tagMap, Double value) {
        // the key of the metric used to manage the map of metrics data
        final String metricsKey = metricName.replace(".", "_") + "_" + tagMap.keySet().stream().map(k -> k + "_" + tagMap.get(k)).collect(Collectors.joining("_"));
        if (!METRICS_MAP.containsKey(metricsKey)) {
            final List<Tag> tags = tagMap.keySet().stream().map(k -> Tag.of(k, tagMap.get(k))).collect(Collectors.toList());
            Gauge.builder(metricName, METRICS_MAP, map -> map.get(metricsKey))
                    .tags(tags)
                    .description(help)
                    .register(registry);
        }
        METRICS_MAP.put(metricsKey, value);
        return metricsKey;
    }
}
