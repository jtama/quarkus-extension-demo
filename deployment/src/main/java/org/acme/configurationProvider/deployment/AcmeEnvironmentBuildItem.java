package org.acme.configurationProvider.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.List;

public final class AcmeEnvironmentBuildItem extends SimpleBuildItem {
    private final List<String> expectedEnvProps;

    public AcmeEnvironmentBuildItem(List<String> expectedEnvProps) {

        this.expectedEnvProps = expectedEnvProps;
    }

    public List<String> getExpectedEnvProps() {
        return expectedEnvProps;
    }
}
