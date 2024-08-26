package org.acme.configurationProvider.deployment.devservice;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface DevServicesConfig {

    /**
     * Enable or disable Dev Services explicitly. Dev Services are automatically enabled unless {@code acme.environment.url} is
     * set.
     */
    @WithDefault("true")
    Boolean enabled();

    /**
     * The Acme configuration value provider container image to use.
     * <p>
     * [INFO]
     * ====
     * Details about default image on https://quay.io/repository/jtama/acme-provider/manifest/sha256:3eca3fe7aedf4bfe8b3213b67c3449c87bc2ab10ae264f681d429ca6c274471f?tab=layers[Docker Repository on Quay]
     * ====
     *
     * @asciidoclet
     */
    @WithDefault("quay.io/jtama/acme-provider:native")
    String imageName();
}
