package org.acme.configurationProvider.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.gizmo.Gizmo;
import org.acme.configurationProvider.runtime.UtilsAreBadLogger;
import org.jboss.logging.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * `StringUtils`, `DateUtils` and other `*Utils` are evil.
 * Please clearly state what your class is doing...
 */
class NoUtilsClassTransformerProcessor {
    private static final Logger logger = Logger.getLogger(NoUtilsClassTransformerProcessor.class);

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public void build(ApplicationIndexBuildItem applicationIndex,
                      AcmeConfigurationBuildTimeConfiguration compileConfiguration,
                      BuildProducer<BytecodeTransformerBuildItem> transformers,
                      UtilsAreBadLogger utilsAreBadLogger) {
        List<String> utilsClasses = applicationIndex.getIndex()
                .getKnownClasses()
                .stream()
                .filter(info -> info.name().toString().endsWith("Utils"))
                .map(info -> info.name().toString())
                .toList();
        if (compileConfiguration.strict().isUtilsStrict()) {
            logger.infof("Someone may not be happy in the future, but you were warned...");
            for (String className : utilsClasses) {
                transformers.produce(new BytecodeTransformerBuildItem(className,
                        (name, classVisitor) -> new NoUtilsClassVisitor(Gizmo.ASM_API_VERSION, classVisitor)));
            }
            return;
        }
        utilsAreBadLogger.nope(utilsClasses);
    }

    private static class NoUtilsClassVisitor extends ClassVisitor {

        Logger logger = Logger.getLogger(NoUtilsClassVisitor.class);

        protected NoUtilsClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        public void visit(
                final int version,
                final int access,
                final String name,
                final String signature,
                final String superName,
                final String[] interfaces) {

            super.visit(version, access, name, signature, superName, interfaces);
            MethodVisitor mv = visitMethod(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC, "<clinit>",
                    "()V", null,
                    null);
            mv.visitCode();
            mv.visitTypeInsn(Opcodes.NEW,
                    ExceptionInInitializerError.class.getName().replace('.', '/'));
            mv.visitInsn(Opcodes.DUP);
            mv.visitLdcInsn("Thou shall not do this !!!!");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "java/lang/ExceptionInInitializerError", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitInsn(Opcodes.ATHROW);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
    }
}
