package org.acme.configurationProvider.runtime;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Map;

class EnvironmentProviderClient {

    private final Client restClient;
    private final UriBuilder uriBuilder;

    EnvironmentProviderClient(URI baseUrl) {
        restClient = ClientBuilder.newClient();
        uriBuilder = UriBuilder.fromUri(baseUrl).path("/conferences/{prefix}");
    }

    Map<String, String> getEnvironment(String prefix) {
        return restClient.target(uriBuilder.build(prefix)).request().get(Map.class);
    }
}
