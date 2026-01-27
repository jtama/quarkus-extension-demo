package org.acme.configurationProvider.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class AcmeConfigurationProviderProcessor {

    public static final String FEATURE = "configuration-provider";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
