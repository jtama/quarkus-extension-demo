package org.acme.configurationProvider.runtime;

import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory;
import io.smallrye.config.ConfigSourceFactory.ConfigurableConfigSourceFactory;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

public class AcmeConfigSourceFactory implements ConfigSourceFactory {

    private EnvironmentProviderClient getEnvironmentClient(EnvironmentRuntimeConfiguration runtimeConfiguration) {
        return new EnvironmentProviderClient(runtimeConfiguration.url());
    }

    private Iterable<ConfigSource> getConfigSources(final EnvironmentRuntimeConfiguration config) {
        return List.of(new AcmeConfigSource(getEnvironmentClient(config)));
    }

    @Override
    public Iterable<ConfigSource> getConfigSources(ConfigSourceContext context) {

        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withSources(new ConfigSourceContext.ConfigSourceContextConfigSource(context))
                .withMapping(EnvironmentRuntimeConfiguration.class)
                .withValidateUnknown(false)
                .withMappingIgnore("acme.strict.rest")
                .withMappingIgnore("acme.strict.utils")
                .build();

        EnvironmentRuntimeConfiguration mapping = config.getConfigMapping(EnvironmentRuntimeConfiguration.class);

        return getConfigSources(mapping);
    }
}
