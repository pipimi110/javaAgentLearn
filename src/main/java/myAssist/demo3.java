package myAssist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import org.apache.coyote.Request;
import  javax.servlet.jsp.tagext.TagVariableInfo;
import  org.apache.catalina.util.LifecycleBase;
import  org.apache.catalina.core.StandardService;

public class demo3 {
    //    public static void main1() {
//        System.out.println("demo3");
//    }
    public static String finaltest() {
//        try{
//            return "try";
//        }catch (Exception e){
//        }finally {
//            return "finally";
//        }
        try {
            throw new Exception("aaa");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fff";
    }

    public static void main(String[] args) throws Exception {
        System.out.println(finaltest());
        System.out.println(new File("target/classes").getAbsolutePath());
        String[] list = "abc".split(";");
        for(String l:list){
            System.out.println("a:"+l);
        }
//        String className = "myAssist.shellbx";
//        System.out.println(ClassPool.getDefault().get(className).getSuperclass().getName());//clazz.getName()//clazz.getSuperclass().getName()
    }
}
