package myAssist;

import javassist.ClassPool;
import javassist.CtClass;

import java.util.Date;

public class demo2 {
    public static void main(String[] args) throws Exception {
//        Date date = new Date();
//        System.out.println(date.toString());;
//        String path = "java.util.Date";
//        System.out.println(path.replace('.','/'));
////        deleteClass("myAssist.demo3");
////        System.out.println();
        Thread thread = new java.lang.Thread();
        System.out.println(thread.toString());
//        System.out.println(thread.currentThread());
//        java.lang.Runtime.getRuntime().exec("calc");
    }

    public static void deleteClass(String className) throws Exception {
        CtClass clazz = ClassPool.getDefault().get(className);
//        clazz.setName("newClassName");
        clazz.removeMethod(clazz.getDeclaredMethod("main1"));
//        clazz.writeFile();
        System.out.println();
    }
}
