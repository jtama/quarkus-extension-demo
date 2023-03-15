package io.quarkiverse.approximationcorrector.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.configuration.ConfigurationException;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class EnvironmentRuntimeConfiguration {

    /**
     * The environment provider server URL.
     *
     * [NOTE]
     * ====
     * Value must be a valid <code>URL</code>
     * ====
     *
     * @asciidoclet
     */
    @ConfigItem
    Optional<String> url;

    public String getUrl() {
        return url.orElseThrow(() -> new ConfigurationException("quarkus.environment.url is a mandatory property."));
    }

}
