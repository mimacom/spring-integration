package com.mimacom.spring.integration.leader.providers;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

public class LeaderProviderTypeCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        ConditionMessage.Builder message = ConditionMessage.forCondition("LeaderProvider",
                sourceClass);
        Environment environment = context.getEnvironment();
        try {
            BindResult<LeaderProviderType> specified = Binder.get(environment)
                    .bind("spring-integration.leader.type", LeaderProviderType.class);
            if (!specified.isBound()) {
                return ConditionOutcome.match(message.because("automatic leader type"));
            }
            LeaderProviderType required = LeaderProviderConfiguration
                    .getType(((AnnotationMetadata) metadata).getClassName());
            if (specified.get() == required) {
                return ConditionOutcome
                        .match(message.because(specified.get() + " leader provider type"));
            }
        } catch (BindException ex) {
        }
        return ConditionOutcome.noMatch(message.because("unknown leader provider type"));
    }

}