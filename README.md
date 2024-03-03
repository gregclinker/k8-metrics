# spring-boot-metrics-example

## Create Promethesu Metrics

Will produce the following metrics on http://localhost:8080/metrics
```
# HELP pod_memory_usage pod memory usage in bytes
# TYPE pod_memory_usage gauge
pod_memory_usage{pod_name="pod2",} 28768.0
pod_memory_usage{pod_name="pod3",} 8935.0
pod_memory_usage{pod_name="pod1",} 85327.0
# HELP memory_usage pod or container memory usage in bytes
# TYPE memory_usage gauge
memory_usage{container_name="container1",pod_name="pod1",type="container",} 63051.0
memory_usage{container_name="container2",pod_name="pod2",type="container",} 70032.0
memory_usage{container_name="container3",pod_name="pod3",type="container",} 6600.0
memory_usage{container_name="N/A",pod_name="pod2",type="pod",} 9400.0
memory_usage{container_name="N/A",pod_name="pod3",type="pod",} 93472.0
memory_usage{container_name="N/A",pod_name="pod1",type="pod",} 67810.0
# HELP container_memory_usage container memory usage in bytes
# TYPE container_memory_usage gauge
container_memory_usage{container_name="container1",pod_name="pod1",} 21982.0
container_memory_usage{container_name="container2",pod_name="pod2",} 80936.0
container_memory_usage{container_name="container3",pod_name="pod3",} 20074.0
# HELP pod_cpu_usage pod cpu usage in mCPU
# TYPE pod_cpu_usage gauge
pod_cpu_usage{pod_name="pod2",} 20641.0
pod_cpu_usage{pod_name="pod3",} 85001.0
pod_cpu_usage{pod_name="pod1",} 4770.0```
