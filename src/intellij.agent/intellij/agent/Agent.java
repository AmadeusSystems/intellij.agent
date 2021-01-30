package intellij.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.AccessibleObject;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Agent implements ClassFileTransformer {
    
    public static void premain(final String agentArgs, final Instrumentation instrumentation) throws UnmodifiableClassException {
        if (!instrumentation.isRetransformClassesSupported())
            throw new UnsupportedOperationException("Retransform Class");
        AccessibleObject.class.getName();
        instrumentation.addTransformer(new Agent(), true);
        instrumentation.retransformClasses(AccessibleObject.class);
        ModuleHelper.openAllBootModule();
    }
    
    @Override
    public byte[] transform(final ClassLoader loader, final String name, final Class<?> target, final ProtectionDomain domain, final byte data[]) throws IllegalClassFormatException {
        try {
            if ("com/intellij/ide/plugins/PluginManagerCore".equals(name)) {
                System.err.println("Transform -> com.intellij.ide.plugins.PluginManagerCore");
                final ClassWriter writer = new ClassWriter(0);
                final ClassReader reader = new ClassReader(data);
                reader.accept(new ClassVisitor(ASM8, writer) {
                    @Override
                    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String exceptions[]) {
                        if ("checkBuildNumberCompatibility".equals(name)
                            && "(Lcom/intellij/ide/plugins/IdeaPluginDescriptor;Lcom/intellij/openapi/util/BuildNumber;Ljava/lang/Runnable;)Lcom/intellij/ide/plugins/PluginLoadingError;".equals(descriptor)) {
                            System.err.println("Transform -> com.intellij.ide.plugins.PluginManagerCore#checkBuildNumberCompatibility");
                            final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                            visitor.visitCode();
                            visitor.visitInsn(ACONST_NULL);
                            visitor.visitInsn(ARETURN);
                            visitor.visitMaxs(1, 3);
                            visitor.visitEnd();
                            return null;
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, 0);
                return writer.toByteArray();
            } else if ("java/lang/reflect/AccessibleObject".equals(name)) {
                System.err.println("Transform -> java.lang.reflect.AccessibleObject");
                final ClassWriter writer = new ClassWriter(0);
                final ClassReader reader = new ClassReader(data);
                reader.accept(new ClassVisitor(ASM8, writer) {
                    
                    @Override
                    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String exceptions[]) {
                        if ("checkCanSetAccessible".equals(name) && "(Ljava/lang/Class;Ljava/lang/Class;Z)Z".equals(descriptor)) {
                            System.err.println("Transform -> java.lang.reflect.AccessibleObject#checkCanSetAccessible");
                            final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                            visitor.visitCode();
                            visitor.visitInsn(ICONST_1);
                            visitor.visitInsn(IRETURN);
                            visitor.visitMaxs(2, 4);
                            visitor.visitEnd();
                            return null;
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, 0);
                return writer.toByteArray();
            } else if ("jdk/internal/reflect/Reflection".equals(name)) {
                System.err.println("Transform -> jdk.internal.reflect.Reflection");
                final ClassWriter writer = new ClassWriter(0);
                final ClassReader reader = new ClassReader(data);
                reader.accept(new ClassVisitor(ASM8, writer) {
                    
                    @Override
                    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String exceptions[]) {
                        if ("verifyMemberAccess".equals(name) && "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/Class;I)Z".equals(descriptor)) {
                            System.err.println("Transform -> jdk.internal.reflect.Reflection#verifyMemberAccess");
                            final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                            visitor.visitCode();
                            visitor.visitInsn(ICONST_1);
                            visitor.visitInsn(IRETURN);
                            visitor.visitMaxs(1, 4);
                            visitor.visitEnd();
                            return null;
                        } else if ("verifyModuleAccess".equals(name) && "(Ljava/lang/Module;Ljava/lang/Class;)Z".equals(descriptor)) {
                            System.err.println("Transform -> jdk.internal.reflect.Reflection#verifyModuleAccess");
                            final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                            visitor.visitCode();
                            visitor.visitInsn(ICONST_1);
                            visitor.visitInsn(IRETURN);
                            visitor.visitMaxs(1, 2);
                            visitor.visitEnd();
                            return null;
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, 0);
                return writer.toByteArray();
            }
        } catch (Throwable t) { t.printStackTrace(); }
        return null;
    }
    
}
