package com.essexboy;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Namespaces;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("default")
public class ClusterApiClientWrapper implements ApiClientWrapper {

    @Override
    public ApiClient getClient() throws IOException {
        return ClientBuilder.cluster().build();
    }

    @Override
    public String getNamespace() throws IOException {
        return Namespaces.getPodNamespace();
    }
}
