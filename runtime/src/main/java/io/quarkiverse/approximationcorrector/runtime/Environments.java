package io.quarkiverse.approximationcorrector.runtime;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import jakarta.inject.Singleton;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.configuration.ConfigurationException;

@Singleton
public class Environments {

    private final EnvironmentRuntimeConfiguration environmentConfiguration;
    private final EnvironmentProviderClient environmentProviderClient;

    public Environments(EnvironmentRuntimeConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
        this.environmentProviderClient = getEnvironmentClient();
    }

    public static Map<String, String> get(String prefix) {
        return Arc.container().instance(Environments.class).get().environmentProviderClient.getEnvironment(prefix);
    }

    private EnvironmentProviderClient getEnvironmentClient() {
        try {
            return RestClientBuilder.newBuilder()
                    .baseUri(new URI(environmentConfiguration.getUrl()))
                    .build(EnvironmentProviderClient.class);
        } catch (URISyntaxException e) {
            throw new ConfigurationException("quarkus.environment.url is not a valid URL", e);
        }
    }

}
