package org.acme.configurationProvider.deployment;

import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import org.acme.configurationProvider.runtime.ThisIsNotRestLogger;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.common.processor.transformation.AnnotationsTransformer;
import org.jboss.resteasy.reactive.common.processor.transformation.Transformation;

import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * You are NOT doing REST
 */
class ThisIsNotRestTransformerProcessor {

    private static final Logger logger = Logger.getLogger(ThisIsNotRestTransformerProcessor.class);
    private static final Predicate<MethodInfo> isGet = method -> method.hasAnnotation(DotName.createSimple(GET.class));
    private static final Predicate<MethodInfo> isPut = method -> method.hasAnnotation(DotName.createSimple(PUT.class));
    private static final Predicate<MethodInfo> isPost = method -> method.hasAnnotation(DotName.createSimple(POST.class));
    private static final Predicate<MethodInfo> isDelete = method -> method.hasAnnotation(DotName.createSimple(DELETE.class));
    private static final Predicate<MethodInfo> isPatch = method -> method.hasAnnotation(DotName.createSimple(PATCH.class));
    private static final Predicate<MethodInfo> isRestEndpoint = isGet.or(isPut).or(isPost).or(isDelete).or(isPatch);

    public static class ReactiveResteasyEnabled implements BooleanSupplier {
        @Override
        public boolean getAsBoolean() {
            return QuarkusClassLoader.isClassPresentAtRuntime("org.jboss.resteasy.reactive.ResponseHeader");
        }
    }

    @BuildStep(onlyIf = ReactiveResteasyEnabled.class)
    @Record(ExecutionTime.RUNTIME_INIT)
    public void warn(
            AcmeConfigurationBuildTimeConfiguration compileConfiguration,
            ApplicationIndexBuildItem applicationIndexBuildItem,
            BuildProducer<AnnotationsTransformerBuildItem> transformers,
            ThisIsNotRestLogger thisIsNotRestLogger) {
        if (compileConfiguration.strict.isRestStrict) {
            logger.infof("Correcting your approximations if any. We'll see at runtime !");
            transformers.produce(new AnnotationsTransformerBuildItem(new RestMethodCorrector(compileConfiguration)));
            return;
        }
        Stream<MethodInfo> restEndpoints = applicationIndexBuildItem.getIndex().getKnownClasses().stream()
                .flatMap(classInfo -> classInfo.methods().stream())
                .filter(isRestEndpoint);
        thisIsNotRestLogger.youAreNotDoingREST(restEndpoints
                .map(methodInfo -> "You think you method \"%s#%s\" is doing rest but it's more JSON over HTTP actually.".formatted(methodInfo.declaringClass().toString(), methodInfo.toString()))
                .toList());
    }

    private class RestMethodCorrector implements AnnotationsTransformer {

        private final boolean mustRecordWarning = true;
        private AcmeConfigurationBuildTimeConfiguration compileConfiguration;

        private RestMethodCorrector(AcmeConfigurationBuildTimeConfiguration compileConfiguration) {
            this.compileConfiguration = compileConfiguration;
        }

        @Override
        public boolean appliesTo(AnnotationTarget.Kind kind) {
            return AnnotationTarget.Kind.METHOD == kind;
        }

        @Override
        public void transform(AnnotationsTransformer.TransformationContext context) {
            MethodInfo method = context.getTarget().asMethod();
            if (isRestEndpoint.test(method)) {
                if (!compileConfiguration.strict.isRestStrict) {

                }
                Transformation transform = context.transform();
                transform.add(DotName.createSimple(org.jboss.resteasy.reactive.ResponseHeader.class),
                        AnnotationValue.createStringValue("name", "X-ApproximationCorrector"),
                        AnnotationValue.createArrayValue("value", Collections
                                .singletonList(AnnotationValue.createStringValue("", "It's more JSON over http really."))));
                transform.done();
                if (mustRecordWarning) {

                }
            }
        }

    }
}
