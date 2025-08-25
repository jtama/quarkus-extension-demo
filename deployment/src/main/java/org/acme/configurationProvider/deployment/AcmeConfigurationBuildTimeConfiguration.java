package org.acme.configurationProvider.deployment;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import org.acme.configurationProvider.deployment.devservice.DevServicesConfig;

@ConfigMapping(prefix = "acme")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface AcmeConfigurationBuildTimeConfiguration {

    /**
     * Configuration for DevServices. DevServices allows Quarkus to automatically start acme env provider in dev and test mode.
     */
    DevServicesConfig devservices();

    /**
     * Configuration for strictness. Decides whether the extension should only warn, or corrects things.
     */
    StrictBuildTimeConfiguration strict();

}
