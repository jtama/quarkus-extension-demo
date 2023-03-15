package io.quarkiverse.approximationcorrector.deployment.devservice;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "environment", phase = ConfigPhase.BUILD_TIME)
public class EnvironmentBuildTimeConfig {

    /**
     * Configuration for DevServices.
     * They allow Quarkus to automatically start environment provider in dev and test mode.
     */
    @ConfigItem
    public EnvironmentDevServicesBuildTimeConfig devservices;
}
