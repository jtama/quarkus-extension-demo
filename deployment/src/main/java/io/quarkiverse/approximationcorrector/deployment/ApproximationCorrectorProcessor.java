package io.quarkiverse.approximationcorrector.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class ApproximationCorrectorProcessor {

    private static final String FEATURE = "approximation-corrector";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
