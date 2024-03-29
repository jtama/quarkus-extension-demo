package org.acme.configurationProvider.runtime;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

class EnvironmentProviderClient {

    private final HttpClient restClient;
    private final URI baseUri;
    private final UncheckedObjectMapper objectMapper;

    EnvironmentProviderClient(URI baseUrl) {
        restClient = HttpClient.newBuilder().build();
        baseUri = baseUrl.resolve("/conferences/");
        objectMapper = new UncheckedObjectMapper();
    }

    public Map<String, String> getEnvironment(String prefix) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(baseUri.resolve(prefix))
                .build();
        try {
            return restClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(objectMapper::readValue)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    static class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
        /**
         * Parses the given JSON string into a Map.
         */
        Map<String, String> readValue(String content) {
            try {
                return this.readValue(content, new TypeReference<>() {
                });
            } catch (IOException ioe) {
                throw new CompletionException(ioe);
            }
        }
    }
}
