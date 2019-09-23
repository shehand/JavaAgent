package com.balcov.agent;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MetricsTransformer implements ClassFileTransformer {

    private final ClassPool classPool = ClassPool.getDefault();

    @Override
    public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException
    {
        if(className == null){
            return null;
        }

        final String classNameDots = className.replaceAll("/",".");
        final CtClass ctClass = classPool.getOrNull(classNameDots);

        if(ctClass == null){
            return null;
        }

        if(ctClass.isFrozen()){
            ctClass.detach();
            return null;
        }

        try{
            boolean methodInstrumented = false;

            for(final CtBehavior behavior: ctClass.getDeclaredBehaviors()){
                System.out.printf("%s - will collect matrix\n", behavior.getLongName());
                instrument(behavior);
                methodInstrumented = true;

            }
            if(methodInstrumented){
                return ctClass.toBytecode();
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            ctClass.detach();
        }

        return null;
    }

    private void instrument(final CtBehavior behavior) throws CannotCompileException, NotFoundException {

        behavior.addLocalVariable("$_traceTimeStart", CtClass.longType);
        behavior.insertBefore("$_traceTimeStart = System.nanoTime();");

        final String reportCode = MetricsCollector.class.getName() +
                ".report(" +
                "\"" + behavior.getLongName() + "\", " +
                "System.nanoTime() - $_traceTimeStart" +
                ");";
        behavior.insertAfter(reportCode);
    }
}
