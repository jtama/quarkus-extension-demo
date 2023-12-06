package org.acme.configurationProvider.runtime;

import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory.ConfigurableConfigSourceFactory;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.List;

public class AcmeConfigSourceFactory implements ConfigurableConfigSourceFactory<EnvironmentRuntimeConfiguration> {
    @Override
    public Iterable<ConfigSource> getConfigSources(final ConfigSourceContext context, final EnvironmentRuntimeConfiguration config) {
            return List.of(new AcmeConfigSource(getEnvironmentClient(config)));
    }

    private EnvironmentProviderClient getEnvironmentClient(EnvironmentRuntimeConfiguration runtimeConfiguration) {
        return new EnvironmentProviderClient(runtimeConfiguration.url());
    }
}
