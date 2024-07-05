package org.acme.configurationProvider.deployment;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;

import org.acme.configurationProvider.runtime.ThisIsNotRestLogger;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationTransformation;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Logger;

import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem;

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
            transformers.produce(new AnnotationsTransformerBuildItem(getAnnotationTransformer()));
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
        return "You think you method \"%s#%s\" is doing rest but it's more JSON over HTTP actually."
                .formatted(methodInfo.declaringClass().toString(), methodInfo.toString());
    }

    private AnnotationTransformation getAnnotationTransformer() {
        return AnnotationTransformation.builder()
                .whenDeclaration(declaration -> declaration.kind() == AnnotationTarget.Kind.METHOD)
                .whenDeclaration(declaration -> isRestEndpoint.test(declaration.asMethod()))
                .transform(context -> context.add(AnnotationInstance.builder(RESPONSE_HEADER)
                        .add(HEADER_NAME)
                        .add(HEADER_VALUES)
                        .buildWithTarget(context.declaration())));
    }

    private static final DotName RESPONSE_HEADER = DotName.createSimple(org.jboss.resteasy.reactive.ResponseHeader.class);
    private static final AnnotationValue HEADER_NAME = AnnotationValue.createStringValue("name", "X-ApproximationCorrector");
    private static final AnnotationValue HEADER_VALUE = AnnotationValue.createStringValue("ignored",
            "It's more JSON over http really.");
    private static final AnnotationValue HEADER_VALUES = AnnotationValue.createArrayValue("value", List.of(HEADER_VALUE));
}
