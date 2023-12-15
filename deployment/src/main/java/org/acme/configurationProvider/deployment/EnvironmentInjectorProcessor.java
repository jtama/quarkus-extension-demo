package org.acme.configurationProvider.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigBuilderBuildItem;
import org.acme.configurationProvider.runtime.AcmeConfigSourceFactoryBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.logging.Logger;

import java.util.List;

public class EnvironmentInjectorProcessor {

    private static final Logger logger = Logger.getLogger(EnvironmentInjectorProcessor.class);

    @BuildStep
    void askForAppllicationScan(ApplicationIndexBuildItem index,
                                 BuildProducer<AcmeEnvironmentBuildItem> buildProducer) {
        index.getIndex().getAnnotations(ConfigProperty.class)
                .stream()
                .map(AnnotationInstance::values)
                .flatMap(List::stream)
                .filter(value -> value.asString().startsWith("env."))
                .findFirst()
                .ifPresent(annotationInstance -> buildProducer.produce(new AcmeEnvironmentBuildItem()));
    }

    @BuildStep
    void envConfigSourceFactory(AcmeEnvironmentBuildItem acmeEnvironmentBuildItem,
                                BuildProducer<RunTimeConfigBuilderBuildItem> runTimeConfigBuilder) {
        if (acmeEnvironmentBuildItem != null) {
            runTimeConfigBuilder.produce(new RunTimeConfigBuilderBuildItem(AcmeConfigSourceFactoryBuilder.class.getName()));
            return;
        }
        logger.warn("You shoud not use this extension if you don't need it.");


    }

}
