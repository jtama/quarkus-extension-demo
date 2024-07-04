package org.acme.configurationProvider.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigBuilderBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigurationDefaultBuildItem;
import org.acme.configurationProvider.runtime.AcmeConfigSourceFactoryBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.logging.Logger;

import java.util.List;

public class EnvironmentInjectorProcessor {

    private static final Logger logger = Logger.getLogger(EnvironmentInjectorProcessor.class);

    @BuildStep
    void askForApplicationScan(ApplicationIndexBuildItem index,
                                 BuildProducer<AcmeEnvironmentBuildItem> buildProducer) {
        List<String> expectedEnvProps = index.getIndex().getAnnotations(ConfigProperty.class)
                .stream()
                .map(AnnotationInstance::values)
                .flatMap(List::stream)
                .filter(value -> value.asString().startsWith("env."))
                .map(AnnotationValue::asString)
                .toList();
        if (!expectedEnvProps.isEmpty()) {
            buildProducer.produce(new AcmeEnvironmentBuildItem(expectedEnvProps));
        }
    }

    @BuildStep
    void envConfigSourceFactory(AcmeEnvironmentBuildItem acmeEnvironmentBuildItem,
                                BuildProducer<RunTimeConfigBuilderBuildItem> runTimeConfigBuilder,
                                BuildProducer<RunTimeConfigurationDefaultBuildItem> runTimeConfigurationDefaultBuildItemBuildProducer) {
        if (acmeEnvironmentBuildItem != null) {
            runTimeConfigBuilder.produce(new RunTimeConfigBuilderBuildItem(AcmeConfigSourceFactoryBuilder.class.getName()));
            return;
        }
        logger.warn("You should not use this extension if you don't need it.");
        produceDefaultExpected(runTimeConfigurationDefaultBuildItemBuildProducer);
    }

    private void produceDefaultExpected(BuildProducer<RunTimeConfigurationDefaultBuildItem> runTimeConfigurationDefaultBuildItemBuildProducer) {
        runTimeConfigurationDefaultBuildItemBuildProducer.produce(new RunTimeConfigurationDefaultBuildItem("acme.environment.url","nope"));
    }

}
