package com.dubbo.hystrix;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 针对不同方法创建hystrix配置
 *
 * @author liujiawei
 * @create 2017-12-18 下午1:30
 */
public abstract class AbstractHystrixConfigBuilder implements ApplicationContextAware {
    protected static final String PROPERTY_FORMAT = "pandora.hystrix.%s.%s.%s";

    protected static ApplicationContext context;

    public HystrixCommandProperties.Setter buildHystrixCommandProperties(String classSimpleName, String methodName) {
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter().withExecutionTimeoutEnabled(false);
        Integer circuitBreakerErrorThresholdPercentage = getProperty(classSimpleName, methodName, "circuitBreakerErrorThresholdPercentage", Integer.class);
        if (circuitBreakerErrorThresholdPercentage != null) {
            setter.withCircuitBreakerErrorThresholdPercentage(circuitBreakerErrorThresholdPercentage);
        }
        Integer executionTimeoutInMilliseconds = getProperty(classSimpleName, methodName, "executionTimeoutInMilliseconds", Integer.class);
        if (executionTimeoutInMilliseconds != null) {
            setter.withExecutionTimeoutInMilliseconds(executionTimeoutInMilliseconds);
        }
        Integer circuitBreakerSleepWindowInMilliseconds = getProperty(classSimpleName, methodName, "circuitBreakerSleepWindowInMilliseconds", Integer.class);
        if (circuitBreakerSleepWindowInMilliseconds != null) {
            setter.withCircuitBreakerSleepWindowInMilliseconds(circuitBreakerSleepWindowInMilliseconds);
        }
        Integer circuitBreakerRequestVolumeThreshold = getProperty(classSimpleName, methodName, "circuitBreakerRequestVolumeThreshold", Integer.class);
        if (circuitBreakerRequestVolumeThreshold != null) {
            setter.withCircuitBreakerRequestVolumeThreshold(circuitBreakerRequestVolumeThreshold);
        }
        Boolean fallbackEnabled = getProperty(classSimpleName, methodName, "fallbackEnabled", Boolean.class);
        if (fallbackEnabled != null) {
            setter.withFallbackEnabled(fallbackEnabled);
        }
        Boolean circuitBreakerEnabled = getProperty(classSimpleName, methodName, "circuitBreakerEnabled", Boolean.class);
        if (circuitBreakerEnabled != null) {
            setter.withCircuitBreakerEnabled(circuitBreakerEnabled);
        }
        return setter;
    }

    /**
     * @param methodName      方法名
     * @param classSimpleName dubbo接口类名
     * @return
     */
    public HystrixThreadPoolProperties.Setter buildHystrixThreadPoolProperties(String classSimpleName, String methodName) {
        HystrixThreadPoolProperties.Setter setter = HystrixThreadPoolProperties.Setter();
        Integer coreSize = getProperty(classSimpleName, methodName, "coreSize", Integer.class);
        if (coreSize != null) {
            setter.withCoreSize(coreSize);
        }
        Integer keepAliveTimeMinutes = getProperty(classSimpleName, methodName, "keepAliveTimeMinutes", Integer.class);
        if (keepAliveTimeMinutes != null) {
            setter.withKeepAliveTimeMinutes(keepAliveTimeMinutes);
        }
        Integer queueSizeRejectionThreshold = getProperty(classSimpleName, methodName, "queueSizeRejectionThreshold", Integer.class);
        if (queueSizeRejectionThreshold != null) {
            setter.withQueueSizeRejectionThreshold(queueSizeRejectionThreshold);
        }
        return setter;
    }

    public HystrixMethodConfig buildHystrixMethodConfig(String classSimpleName, String methodName) {
        HystrixMethodConfig methodConfig = new HystrixMethodConfig();
        methodConfig.setGroupKey(classSimpleName);
        methodConfig.setCommandKey(methodName);
        methodConfig.setFallbackClass(getProperty(classSimpleName, "default", "fallbackClass", String.class));
        return methodConfig;
    }


    protected abstract <T> T getProperty(String classSimpleName, String methodName, String name, Class<T> type);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}