package io.quarkiverse.approximationcorrector.deployment;

import java.util.List;

import org.jboss.logging.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.gizmo.Gizmo;

/**
 * `StringUtils`, `DateUtils` and other `*Utils` are evil.
 * Please clearly state what your class is doing...
 */
class NoUtilsClassTransformerProcessor {

    @BuildStep
    public void build(ApplicationIndexBuildItem applicationIndex,
            BuildProducer<BytecodeTransformerBuildItem> transformers) {
        List<String> utilsClasses = applicationIndex.getIndex()
                .getKnownClasses()
                .stream()
                .filter(info -> info.name().toString().endsWith("Utils"))
                .map(info -> info.name().toString())
                .toList();
        for (String className : utilsClasses) {
            transformers.produce(new BytecodeTransformerBuildItem(className,
                    (name, classVisitor) -> new NoUtilsClassVisitor(Gizmo.ASM_API_VERSION, classVisitor)));
        }
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
            logger.warnf("💀 I told you not do this!", name);
            logger.warnf("💀 Your code will panic at runtime -> %s", name);
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
