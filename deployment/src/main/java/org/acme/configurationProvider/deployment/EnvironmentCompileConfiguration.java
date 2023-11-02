package org.acme.configurationProvider.deployment;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "acme", name = "strict", phase = ConfigPhase.BUILD_TIME)
public class EnvironmentCompileConfiguration {

    /**
     * Should the extension be strict when correcting response headers.
     * <p>
     * [WARNING]
     * ====
     * If set to <code>true</code> all response will have added headers, otherwise a simple warning will be displayed at application startup.
     * ====
     *
     * @asciidoclet
     */
    @ConfigItem(name = "rest", defaultValue = "false")
    public Boolean isRestStrict;

    /**
     * Should the extension be strict when correcting utils classes.
     * <p>
     * [WARNING]
     * ====
     * If set to <code>true</code> all targeted class will be augmented, otherwise a simple warning will be displayed at application startup.
     * ====
     *
     * @asciidoclet
     */
    @ConfigItem(name = "utils", defaultValue = "false")
    public Boolean isUtilsStrict;

}
