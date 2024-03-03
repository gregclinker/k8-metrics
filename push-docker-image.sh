mvn clean install -DskipTests
docker build . -t gregclinker/k8-metrics:0.1
docker push gregclinker/k8-metrics:0.1