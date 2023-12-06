package org.acme.configurationProvider.deployment.devservice;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;

@ConfigGroup
public class DevServicesConfig {

    /**
     * Enable or disable Dev Services explicitly. Dev Services are automatically enabled unless {@code acme.environment.url} is
     * set.
     */
    @ConfigItem
    public Optional<Boolean> enabled = Optional.empty();

    /**
     * The Acme configuration value provider container image to use.
     */
    @ConfigItem(defaultValue = "quay.io/jtama/acme-provider")
    public String imageName;
}
