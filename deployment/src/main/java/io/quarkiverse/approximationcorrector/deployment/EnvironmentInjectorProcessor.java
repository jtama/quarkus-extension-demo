package io.quarkiverse.approximationcorrector.deployment;

import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.jaxrs.client.reactive.runtime.JaxrsClientReactiveRecorder;
import io.quarkus.resteasy.reactive.common.deployment.ResourceScanningResultBuildItem;
import io.quarkus.resteasy.reactive.spi.AdditionalResourceClassBuildItem;
import jakarta.inject.Singleton;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.CompositeIndex;
import org.jboss.jandex.DotName;

import io.quarkiverse.approximationcorrector.runtime.Environment;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentProviderClient;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentRecorder;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentRuntimeConfiguration;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentValues;
import io.quarkiverse.approximationcorrector.runtime.Environments;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import org.jboss.resteasy.reactive.common.processor.scanning.ResourceScanningResult;

public class EnvironmentInjectorProcessor {

    public static final DotName ENVIRONMENT_DOT_NAME = DotName.createSimple(Environment.class);

    @BuildStep
    public void addAdditionalResourceClass(
            CombinedIndexBuildItem index,
            BuildProducer<AdditionalResourceClassBuildItem> additionalResourceClassBuildItemBuildProducer) {
        ClassInfo classInfo = index.getIndex().getClassByName(EnvironmentProviderClient.class);
        if (classInfo != null) {
            additionalResourceClassBuildItemBuildProducer
                    .produce(new AdditionalResourceClassBuildItem(classInfo, "/environment"));
        }
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    public void environmentInjectorProducer(
            ApplicationIndexBuildItem applicationIndex,
            EnvironmentRuntimeConfiguration environmentConfiguration,
            EnvironmentRecorder recorder,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
            BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildProducer,
            BuildProducer<AdditionalIndexedClassesBuildItem> additionalIndexedClassesBuildItemBuildProducer) {
        var needsEnvProducer = applicationIndex.getIndex().getAnnotations(ENVIRONMENT_DOT_NAME);

        if (needsEnvProducer.isEmpty()) {
            return;
        }

        additionalBeanBuildItemBuildProducer.produce(AdditionalBeanBuildItem.builder()
                .addBeanClasses(Environment.class).build());
        additionalIndexedClassesBuildItemBuildProducer
                .produce(new AdditionalIndexedClassesBuildItem(EnvironmentProviderClient.class.getName()));
        additionalBeanBuildItemBuildProducer.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(Environments.class)
                .setUnremovable()
                .setDefaultScope(DotNames.SINGLETON)
                .build());
        needsEnvProducer.forEach(annotationInstance -> {
            String prefix = annotationInstance.value() != null ? annotationInstance.value().asString() : "acme";
            // Pass runtime configuration to ensure initialization order
            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                    .configure(EnvironmentValues.class)
                    .scope(Singleton.class)
                    .setRuntimeInit()
                    .unremovable()
                    // Pass runtime configuration to ensure initialization order
                    .supplier(recorder.environmentSupplier(prefix, environmentConfiguration))
                    .addQualifier().annotation(Environment.class).addValue("value", prefix).done();
            syntheticBeanBuildItemBuildProducer.produce(configurator.done());
        });
    }

}
