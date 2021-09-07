package myAgent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;

public class DefineTransformer implements ClassFileTransformer {
    final private Config config;

    DefineTransformer() {
        this.config = new Config();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        if (!LoadCache.check(className)) {
        className = className.replace('/', '.');
        System.out.println(String.format("[TEST] 检测到加载器 %s 新加载类: %s", loader == null ? "null" : loader.getClass().getName(), className));
//            return PluginManager.check(className, classfileBuffer);
        Iterator it = config.getBlackClassMap().keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (className.equals(key)) {
                byte[] bytess = cookClass(className, config.getBlackClassMap().get(className));
                return bytess == null ? classfileBuffer : bytess;
            }
        }
//        }
        byte[] bytes = cookMemShell(className);
        return bytes == null ? classfileBuffer : bytes;
    }

    public void retransform(Instrumentation inst) {
        Class[] classes = inst.getAllLoadedClasses();
        for (Class aClass : classes) {
            if (inst.isModifiableClass(aClass)) {
                System.out.println(String.format("[TEST] 重新加载"));
                try {
                    inst.retransformClasses(aClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] cookMemShell(String className) {
        try {
            CtClass clazz = ClassPool.getDefault().get(className);
            String superClassName = clazz.getSuperclass().getName();
            String body = "{System.out.println(\"delete memshell\");}";
            if (superClassName.equals("javax.servlet.http.HttpServlet") && !className.equals("shellbx") && !className.equals("org.apache.catalina.servlets.DefaultServlet") && !className.equals("org.apache.jasper.servlet.JspServlet")) {
                System.err.println(String.format("[TEST] 检测到内存马加载: %s; 开始处理...", className));
                String[] httpMethods = new String[]{"doGet", "doHead", "doPost", "doPut", "doDelete", "doOptions", "doTrace", "service", "sendMethodNotAllowed"};
                for (CtMethod method : clazz.getDeclaredMethods()) {
                    for (String httpMethod : httpMethods) {
                        if (method.getName().equals(httpMethod)) {
                            method.setBody(body);
                        }
                    }
                }
                byte[] byteCode = clazz.toBytecode();
                clazz.detach();
                return byteCode;
            } else if (clazz.getInterfaces()[0].getName().equals("javax.servlet.Filter")) {
                System.err.println(String.format("[TEST] 检测到内存马加载: %s; 开始处理...", className));
                clazz.getDeclaredMethod("doFilter").setBody(body);
//                clazz.getDeclaredMethod("doFilter").setBody("{System.out.println(\"success\");chain.doFilter(request,response);}");
//                clazz.getDeclaredMethod("doFilter").insertBefore("{System.out.println(\"success\");");
                byte[] byteCode = clazz.toBytecode();
                clazz.detach();
                return byteCode;
            } else if (clazz.getInterfaces()[0].getName().equals("javax.servlet.ServletRequestListener")) {
                System.err.println(String.format("[TEST] 检测到内存马加载: %s; 开始处理...", className));
                clazz.getDeclaredMethod("requestInitialized").setBody(body);
                clazz.getDeclaredMethod("requestDestroyed").setBody(body);
                byte[] byteCode = clazz.toBytecode();
                clazz.detach();
                return byteCode;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public byte[] cookClass(String className, HashMap methodMap) {
        System.err.println(String.format("[TEST] 检测到危险类加载: %s; 开始处理...", className));
        try {
            CtClass clazz = ClassPool.getDefault().get(className);
            Iterator it = methodMap.keySet().iterator();
            while(it.hasNext()){
                String methodName = (String) it.next();
                String methodBody = (String) methodMap.get(methodName);
                CtMethod method = clazz.getDeclaredMethod(methodName);//getMethod需要desc参数
                method.setBody(methodBody);
            }
            // 返回字节码，并且detachCtClass对象
            byte[] byteCode = clazz.toBytecode();
            //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
            clazz.detach();
            return byteCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}