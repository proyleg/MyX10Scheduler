package com.myscheduler.config;

import com.myscheduler.ahscript.ClassFactory;
import com.myscheduler.ahscript.IActiveHome;
import com.myscheduler.ahscript.events._DIActiveHomeEvents;
import com.myscheduler.event.X10EventReceiver;
import com4j.EventCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.myscheduler.MyX10SchedulerApplication.isProduction;

/**
 * Created by Pierre on 2016-04-28.
 */
@Component
public class ActiveHomeConfiguration {

    @Bean
    public IActiveHome x10Container() {
        IActiveHome ah = null;
        if (isProduction()) {
            ah = ClassFactory.createActiveHome();
            EventCookie eventSubscription = ah.advise(_DIActiveHomeEvents.class, new X10EventReceiver());
        }
        return ah;
    }
}
