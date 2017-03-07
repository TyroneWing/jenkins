package com.tinckay.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * Created by root on 12/26/16.
 */
public class AppContentUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        AppContentUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }
}
