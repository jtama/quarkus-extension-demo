package org.acme.configurationProvider.deployment;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigGroup
public class StrictBuildTimeConfiguration {

    /**
     * Should the extension be strict when correcting response headers.
     *
     * [WARNING]
     * ====
     * If set to `true` all response will have added headers, otherwise a simple warning will be displayed at application startup.
     * ====
     *
     * @asciidoclet
     */
    @ConfigItem(name = "rest", defaultValue = "false")
    public Boolean isRestStrict;

    /**
     * Should the extension be strict when correcting utils classes.
     *
     * [WARNING]
     * ====
     * If set to `true` all targeted class will be augmented, otherwise a simple warning will be displayed at application startup.
     * ====
     *
     * @asciidoclet
     */
    @ConfigItem(name = "utils", defaultValue = "false")
    public Boolean isUtilsStrict;

}
