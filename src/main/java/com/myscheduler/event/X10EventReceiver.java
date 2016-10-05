package com.myscheduler.event;

import com.myscheduler.ahscript.events._DIActiveHomeEvents;
import com.myscheduler.service.ModuleService;

import static com.myscheduler.MyX10SchedulerApplication.getCtx;

/**
 * Created by Pierre on 2016-04-27.
 */
public class X10EventReceiver extends _DIActiveHomeEvents {

    public void recvAction(
            java.lang.Object bszAction,
            java.lang.Object bszParm1,
            java.lang.Object bszParm2,
            java.lang.Object bszParm3,
            java.lang.Object bszParm4,
            java.lang.Object bszParm5,
            java.lang.Object bszReserved) {

        if (bszAction != null && !bszAction.toString().equals("RecvPlc")) return;
        String x10Address = bszParm1.toString();
        String plcCommand = bszParm2.toString();
        String additionalData = bszParm3.toString();

//        IActiveHome ah = (IActiveHome) getCtx().getBean("x10Container");
//        String[] beanNames = getCtx().getBeanDefinitionNames();
        ModuleService moduleService = (ModuleService) getCtx().getBean("moduleServiceImpl");
//        System.out.println("Called");
//        System.out.println(x10Address);
//        System.out.println(plcCommand);
//        System.out.println(additionalData);
//        for (int i = 1; i < 10; i++) {
//            System.out.println("QueryPLC de A" + i + " " + ah.sendAction("queryplc", "A" + i + " on", "", "").toString());
//        }
        switch (plcCommand) {
            case "On":
            case "Dim":
            case "Bright":
                moduleService.updateModulesStatus("on", x10Address);
                break;
            case "Off":
                moduleService.updateModulesStatus("off", x10Address);
                break;
            case "AllUnitsOff":
                moduleService.updateModulesStatusAllOff();
                break;
            case "AllLightsOn":
                moduleService.updateModulesStatusAllLightsOn();
                break;
        }

//        String[] beanNames = getCtx().getBeanDefinitionNames();
////        IActiveHome obj2 = (IActiveHome)context.getBean("x10Container");
//        System.out.println("QueryPLC de A2 " + obj.sendAction("queryplc", "A2 on", "", "").toString());
//        System.out.println();
////        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
        // if action is recvplc, then the param
        // types are as follows:
        //1: String  -- X10 Address
        //2: String  -- X10 command
        //3: String  -- X10 additional data
        //4: String  -- not used ?
        //5: String  -- not used ?
        // if action is recvrf, then the param types
        // are as follows:
        //1: java.lang.String  -- X10 Address
        //2: java.lang.String  -- RF Command
        //3: java.lang.Integer -- key data
        //4: java.util.Date    -- timestamp
        //5: java.lang.String  -- not used ?
    }
}
