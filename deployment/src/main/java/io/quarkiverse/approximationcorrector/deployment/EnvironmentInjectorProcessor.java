package io.quarkiverse.approximationcorrector.deployment;

import jakarta.inject.Singleton;

import org.jboss.jandex.DotName;

import io.quarkiverse.approximationcorrector.runtime.Environment;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;

public class EnvironmentInjectorProcessor {

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    public void environmentInjectorProducer(
            ApplicationIndexBuildItem applicationIndex,
            EnvironmentRecorder recorder,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
            BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildProducer) {
        var needsEnvProducer = applicationIndex.getIndex()
                .getKnownClasses()
                .stream()
                .flatMap(info -> info.fields().stream())
                .anyMatch(info -> info.hasAnnotation(DotName.createSimple(Environment.class)));

        if (!needsEnvProducer) {
            return;
        }

        additionalBeanBuildItemBuildProducer.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(Environment.class).build());
        SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                .configure(String.class)
                .scope(Singleton.class)
                .setRuntimeInit()
                .unremovable()
                // Pass runtime configuration to ensure initialization order
                .supplier(recorder.environmentSupplier())
                .addQualifier().annotation(Environment.class)
                .done();
        syntheticBeanBuildItemBuildProducer.produce(configurator.done());
    }

}
