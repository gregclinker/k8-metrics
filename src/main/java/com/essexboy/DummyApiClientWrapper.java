package com.essexboy;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("test")
public class DummyApiClientWrapper implements ApiClientWrapper {
    private String namespace = "fake-load";

    @Override
    public ApiClient getClient() throws IOException {
        return null;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }
}
