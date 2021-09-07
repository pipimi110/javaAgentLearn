package myAgent;

import com.sun.tools.attach.*;

import java.util.List;

public class attachVM {
    public static void main(String[] args) throws Exception {
//获取当前系统中所有 运行中的 虚拟机
        System.out.println("running JVM start ");
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {

            System.out.println(vmd.displayName());
            String aim = "myAssist.demo1";
            if (vmd.displayName().equalsIgnoreCase(aim)) {
                System.out.println(String.format("find %s, process id %s", vmd.displayName(), vmd.id()));
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                String agent = args[0];
                virtualMachine.loadAgent(agent);
                virtualMachine.detach();
            }
        }
    }
}
