package io.quarkiverse.approximationcorrector.deployment.devservice;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class EnvironmentDevServicesBuildTimeConfig {

    /**
     * Enable or disable Dev Services explicitly. Dev Services are automatically enabled unless {@code quarkus.minio.url} is
     * set.
     */
    @ConfigItem
    public Optional<Boolean> enabled = Optional.empty();

    /**
     * The Minio container image to use.
     */
    @ConfigItem(defaultValue = "quay.io/jtama/acme-env")
    public String imageName;
}
