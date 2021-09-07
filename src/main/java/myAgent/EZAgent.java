package myAgent;

import java.lang.instrument.*;

public class EZAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("static agent,,,");
        init(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("dynamic agent,,,");
        init(agentArgs, inst);
    }

    public static void init(String agentArgs, Instrumentation inst) {
        System.out.println("agentArgs : " + agentArgs);
        DefineTransformer defineTransformer = new DefineTransformer();
        inst.addTransformer(defineTransformer, true);
        defineTransformer.retransform(inst);//添加DefineTransformer后retransform重新加载类会经过DefineTransformer
    }

}
