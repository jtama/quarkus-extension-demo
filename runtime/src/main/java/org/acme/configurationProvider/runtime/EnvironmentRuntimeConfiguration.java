package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.net.URI;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "acme")
public interface EnvironmentRuntimeConfiguration {

    /**
     * The environment provider server URL.
     *
     * [NOTE]
     * ====
     * Value must be a valid `URI`
     * ====
     *
     * @asciidoclet
     */
    @WithName("environment.url")
    URI url();
}