package org.acme.configurationProvider.deployment;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import org.acme.configurationProvider.deployment.devservice.DevServicesConfig;

@ConfigRoot(prefix = "acme", name = "", phase = ConfigPhase.BUILD_TIME)
public class AcmeConfigurationBuildTimeConfiguration {

    /**
     * Configuration for DevServices. DevServices allows Quarkus to automatically start acme env provider in dev and test mode.
     */
    @ConfigItem
    public DevServicesConfig devservices;

    /**
     * Configuration for strictness. Decides whether the extension should only warn, or corrects things.
     */
    @ConfigItem
    public StrictBuildTimeConfiguration strict;

}
