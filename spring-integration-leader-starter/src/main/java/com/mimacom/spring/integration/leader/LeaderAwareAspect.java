package com.mimacom.spring.integration.leader;


import java.lang.reflect.Method;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderRegistrar;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Aspect
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class LeaderAwareAspect implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderAwareAspect.class);

    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.mimacom.spring.integration.leader.LeaderAware)")
    public void leaderAwareAnnotationPointcut() {
    }

    @Around("leaderAwareAnnotationPointcut()")
    public Object leaderAware(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String role = method.getAnnotation(LeaderAware.class).value();
        LeaderProvider leaderProvider = getLeaderProvider(joinPoint, role);
        if (leaderProvider.context().isLeader()) {
            return joinPoint.proceed();
        }
        LOGGER.debug("Advice: {} role: '{}' is not the leader", joinPoint, role);
        return null;
    }

    private LeaderProvider getLeaderProvider(ProceedingJoinPoint joinPoint, String role) {
        final String leaderProviderBeanName = role + LeaderProviderRegistrar.LEADER_PROVIDER_BEAN_NAME_POSTFIX;
        try {
            return applicationContext.getBean(leaderProviderBeanName, LeaderProvider.class);
        } catch (BeansException e) {
            LOGGER.warn("Advice: {} no LeaderProvider bean named '{}' exists for role: '{}' ", joinPoint, leaderProviderBeanName, role);
            throw e;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
