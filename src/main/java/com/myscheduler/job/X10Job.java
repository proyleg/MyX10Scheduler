package com.myscheduler.job;

/**
 * Created by Pierre on 2016-04-10.
 */

import com.myscheduler.ahscript.IActiveHome;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.myscheduler.MyX10SchedulerApplication.*;

public class X10Job implements Job {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String x10Address = dataMap.getString("x10address");
        if (x10Address.trim().equals("")) x10Address = "a1";
        String command = dataMap.getString("command");
        boolean multiSend = dataMap.getBoolean("multiSend");
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        logger.info("Executing X10Job " + jobKey + " " + x10Address + " " + command + " ");
        if (isProduction()) {
            IActiveHome ah = (IActiveHome) getCtx().getBean("x10Container");
            for (int i = 0; i < 1 + getMultiSendNumberOfRetry(); i++) {
                ah.sendAction("sendplc", x10Address + " " + command, "", "");
                if (!multiSend) break;
                try {
                    Thread.sleep(getMultiSendNumberOfSecond() * 1000L);       // x seconds
                } catch (Exception ignore) {
                }
            }
        }
    }
}
