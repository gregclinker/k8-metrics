package com.essexboy;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("local")
public class LocalApiClientWrapper implements ApiClientWrapper {
    private String namespace = "fake-load";

    @Override
    public ApiClient getClient() throws IOException {
        return Config.defaultClient();
    }

    @Override
    public String getNamespace() {
        return namespace;
    }
}
