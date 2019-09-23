package com.balcov.agent;

import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Spy implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {


        if(className != null){
            if (!className.startsWith("java") &&
                    !className.startsWith("sun") &&
                    !className.startsWith("javax") &&
                    !className.startsWith ("com") &&
                    !className.startsWith("jdk") &&
                    !className.startsWith("org")&&
                    !className.startsWith("ballerina"))
            {
                System.out.println("Dumping: " + className);

                String newName = className.replaceAll("/", "#") + ".class";

                try
                {
                    FileOutputStream fos = new FileOutputStream("file/"+newName);
                    fos.write(classfileBuffer);
                    fos.close();
                }
                catch (Exception ex)
                {
                    System.out.println("Exception while writing: " + newName);
                }
            }
        }
        return classfileBuffer;
    }
}
