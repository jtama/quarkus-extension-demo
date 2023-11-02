package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.configuration.ConfigBuilder;
import io.smallrye.config.SmallRyeConfigBuilder;

public class AcmeConfigSourceFactoryBuilder implements ConfigBuilder {

    @Override
    public SmallRyeConfigBuilder configBuilder(final SmallRyeConfigBuilder builder) {
        return builder.withSources(new AcmeConfigSourceFactory());
    }
}
