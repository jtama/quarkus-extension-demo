package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.configuration.ConfigurationException;
import io.smallrye.config.ConfigMapping;

import java.net.URI;
import java.util.Optional;

@ConfigMapping(prefix = "acme.environment")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface EnvironmentRuntimeConfiguration {

    /**
     * The environment provider server URL.
     * <p>
     * [NOTE]
     * ====
     * Value must be a valid <code>URI</code>
     * ====
     *
     * @asciidoclet
     */
    Optional<URI> url();

    default URI getUrl() {
        return url().orElseThrow(() -> new ConfigurationException("acme.environment.url is a mandatory property."));
    }

}
