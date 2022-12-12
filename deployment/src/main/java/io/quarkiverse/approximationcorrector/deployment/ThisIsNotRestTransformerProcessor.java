package io.quarkiverse.approximationcorrector.deployment;

import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import javax.ws.rs.*;

import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.resteasy.reactive.ResponseHeader;

import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.arc.processor.Transformation;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;

/**
 * `StringUtils`, `DateUtils` and other `*Utils` are evil.
 * Please clearly state what your class is doing...
 */
class ThisIsNotRestTransformerProcessor {

    public static class ReactiveResteasyEnabled implements BooleanSupplier {
        @Override
        public boolean getAsBoolean() {
            return QuarkusClassLoader.isClassPresentAtRuntime("org.jboss.resteasy.reactive.ResponseHeader");
        }
    }

    @BuildStep(onlyIf = ReactiveResteasyEnabled.class)
    public void build(
            BuildProducer<AnnotationsTransformerBuildItem> transformers) {
        transformers.produce(new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {

            @Override
            public boolean appliesTo(AnnotationTarget.Kind kind) {
                return AnnotationTarget.Kind.METHOD == kind;
            }

            @Override
            public void transform(TransformationContext context) {
                MethodInfo method = context.getTarget().asMethod();
                if (isRestEndpoint.test(method)) {
                    Transformation transform = context.transform();
                    transform.add(DotName.createSimple(ResponseHeader.class),
                            AnnotationValue.createStringValue("name", "X-ApproximationCorrector"),
                            AnnotationValue.createArrayValue("value", Collections
                                    .singletonList(AnnotationValue.createStringValue("", "It's more JSON aver http really."))));
                    transform.done();
                }
            }

        }));
    }

    private static Predicate<MethodInfo> isGet = method -> method.hasAnnotation(DotName.createSimple(GET.class));
    private static Predicate<MethodInfo> isPut = method -> method.hasAnnotation(DotName.createSimple(PUT.class));
    private static Predicate<MethodInfo> isPost = method -> method.hasAnnotation(DotName.createSimple(POST.class));
    private static Predicate<MethodInfo> isDelete = method -> method.hasAnnotation(DotName.createSimple(DELETE.class));
    private static Predicate<MethodInfo> isPatch = method -> method.hasAnnotation(DotName.createSimple(PATCH.class));
    private static Predicate<MethodInfo> isRestEndpoint = isGet.or(isPut).or(isPost).or(isDelete).or(isPatch);
}
