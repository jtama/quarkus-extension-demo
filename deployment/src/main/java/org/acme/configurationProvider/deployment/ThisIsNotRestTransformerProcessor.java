package org.acme.configurationProvider.deployment;

import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem;
import jakarta.ws.rs.*;
import org.acme.configurationProvider.runtime.ThisIsNotRestLogger;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.common.processor.transformation.AnnotationsTransformer;
import org.jboss.resteasy.reactive.common.processor.transformation.Transformation;

import java.util.List;
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
    public void correctApproximations(
            AcmeConfigurationBuildTimeConfiguration compileConfiguration,
            ApplicationIndexBuildItem applicationIndexBuildItem,
            BuildProducer<AnnotationsTransformerBuildItem> transformers,
            ThisIsNotRestLogger thisIsNotRestLogger) {
        if (compileConfiguration.strict.isRestStrict) {
            logger.infof("Correcting your approximations if any. We'll see at runtime !");
            transformers.produce(new AnnotationsTransformerBuildItem(new RestMethodCorrector()));
            return;
        }
        Stream<MethodInfo> restEndpoints = applicationIndexBuildItem.getIndex().getKnownClasses().stream()
                .flatMap(classInfo -> classInfo.methods().stream())
                .filter(isRestEndpoint);
        thisIsNotRestLogger.youAreNotDoingREST(restEndpoints
                .map(this::getMessage)
                .toList());
    }

    private String getMessage(MethodInfo methodInfo) {
        return "You think you method \"%s#%s\" is doing rest but it's more JSON over HTTP actually.".formatted(methodInfo.declaringClass().toString(), methodInfo.toString());
    }

    private static class RestMethodCorrector implements AnnotationsTransformer {

        public static final DotName RESPONSE_HEADER = DotName.createSimple(org.jboss.resteasy.reactive.ResponseHeader.class);
        public static final AnnotationValue HEADER_NAME = AnnotationValue.createStringValue("name","X-ApproximationCorrector");
        public static final AnnotationValue HEADER_VALUE = AnnotationValue.createStringValue("ignored","It's more JSON over http really.");
        public static final AnnotationValue HEADER_VALUES = AnnotationValue.createArrayValue("value", List.of(HEADER_VALUE));

        @Override
        public boolean appliesTo(AnnotationTarget.Kind kind) {
            return AnnotationTarget.Kind.METHOD == kind;
        }

        @Override
        public void transform(AnnotationsTransformer.TransformationContext context) {
            MethodInfo method = context.getTarget().asMethod();
            if (isRestEndpoint.test(method)) {
                Transformation transform = context.transform();
                transform.add(RESPONSE_HEADER,HEADER_NAME, HEADER_VALUES);
                transform.done();
            }
        }

    }
}
