package com.myscheduler.config;

/**
 * Created by Pierre on 2016-04-10.
 */
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfiguration {
    @Bean
    public MethodInvokingJobDetailFactoryBean methodJobSchedulerScheduleX10Jobs() {
        MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
        obj.setTargetBeanName("JobScheduler");
        obj.setTargetMethod("scheduleX10Jobs");
        return obj;
    }
    @Bean
    public MethodInvokingJobDetailFactoryBean methodJobSchedulerResumeX10Jobs() {
        MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
        obj.setTargetBeanName("JobScheduler");
        obj.setTargetMethod("resumeX10Jobs");
        return obj;
    }
    //Job  is scheduled when Spring boot start
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(){
        SimpleTriggerFactoryBean stFactory = new SimpleTriggerFactoryBean();
        stFactory.setJobDetail(methodJobSchedulerScheduleX10Jobs().getObject());
        stFactory.setStartDelay(3000);
        stFactory.setRepeatCount(0);
        return stFactory;
    }
//    @Bean
//    public JobDetailFactoryBean jobDetailFactoryBean(){
//        JobDetailFactoryBean factory = new JobDetailFactoryBean();
//        factory.setJobClass(X10Job.class);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("name", "RAM");
//        map.put(X10Job.COUNT, 1);
//        factory.setJobDataAsMap(map);
//        factory.setGroup("mygroup");
//        factory.setName("myjob");
//        return factory;
//    }
    //Job is scheduled after every 10 minute
    // Use http://www.cronmaker.com/
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
//        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setJobDetail(methodJobSchedulerScheduleX10Jobs().getObject());
        stFactory.setStartDelay(6000);
        stFactory.setName("contrigger");
//        stFactory.setCronExpression("0 0/1 * 1/1 * ? *");
//          Every 10 minutes
//        stFactory.setCronExpression("0 0/10 * 1/1 * ? *");
        stFactory.setCronExpression("0 0 0 1/1 * ? *");
        return stFactory;
    }
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean2(){
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(methodJobSchedulerResumeX10Jobs().getObject());
        stFactory.setStartDelay(6000);
        stFactory.setName("contrigger2");
        stFactory.setCronExpression("0 0/30 * 1/1 * ? *");
//        stFactory.setCronExpression("0 0/1 * 1/1 * ? *");
        return stFactory;
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(simpleTriggerFactoryBean().getObject(),
                cronTriggerFactoryBean().getObject(),
                cronTriggerFactoryBean2().getObject());
        scheduler.setQuartzProperties(quartzProperties());
        return scheduler;
    }
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}