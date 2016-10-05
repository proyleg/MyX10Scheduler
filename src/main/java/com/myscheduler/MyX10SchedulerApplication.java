package com.myscheduler;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyX10SchedulerApplication {
    private static ApplicationContext ctx;
    private static boolean production;
    private static int multiSendNumberOfRetry;
    private static int multiSendNumberOfSecond;

    @Value("${tomcat.ajp.port:0}")
    private int ajpPort;

    @Value("${tomcat.ajp.remoteauthentication:false}")
    private String remoteAuthentication;

    @Value("${tomcat.ajp.enabled:false}")
    private boolean tomcatAjpEnabled;

    public static void main(String[] args) {
        ctx = SpringApplication.run(MyX10SchedulerApplication.class, args);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        if (tomcatAjpEnabled) {
            Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setProtocol("AJP/1.3");
            ajpConnector.setPort(ajpPort);
            ajpConnector.setSecure(false);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setScheme("http");
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }
        return tomcat;
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }

    @Value("${scheduler.production:false}")
    public void setProduction(boolean production) {
        MyX10SchedulerApplication.production = production;
    }

    public static boolean isProduction() {
        return production;
    }

    public static int getMultiSendNumberOfRetry() {
        return multiSendNumberOfRetry;
    }

    @Value("${scheduler.multisend.numberOfRetry:0}")
    public void setMultiSendNumberOfRetry(int multiSendNumberOfRetry) {
        MyX10SchedulerApplication.multiSendNumberOfRetry = multiSendNumberOfRetry;
    }

    public static int getMultiSendNumberOfSecond() {
        return multiSendNumberOfSecond;
    }

    @Value("${scheduler.multisend.numberOfSecond:}")
    public void setMultiSendNumberOfSecond(int multiSendNumberOfSecond) {
        MyX10SchedulerApplication.multiSendNumberOfSecond = multiSendNumberOfSecond;
    }
}
