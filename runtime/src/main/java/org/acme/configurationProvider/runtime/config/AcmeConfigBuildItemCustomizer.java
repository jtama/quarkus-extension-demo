package org.acme.configurationProvider.runtime.config;

import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.SmallRyeConfigBuilderCustomizer;

public class AcmeConfigBuildItemCustomizer implements SmallRyeConfigBuilderCustomizer {
    @Override
    public void configBuilder(SmallRyeConfigBuilder builder) {
        builder.withMappingIgnore("acme.**");
    }
}
