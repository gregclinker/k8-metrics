package com.essexboy;

import io.kubernetes.client.openapi.ApiClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;

public interface ApiClientWrapper {
    ApiClient getClient() throws IOException;

    String getNamespace() throws IOException;
}
