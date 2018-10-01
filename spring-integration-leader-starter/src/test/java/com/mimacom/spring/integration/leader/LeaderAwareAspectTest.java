package com.mimacom.spring.integration.leader;

import static com.mimacom.spring.integration.leader.providers.LeaderProviderRegistrar.LEADER_PROVIDER_BEAN_NAME_POSTFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.integration.leader.Context;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LeaderAwareAspectTest {

    private static final String ROLE_NAME = "roleOne";

    private static final String LEADER_PROVIDER_BEAN_NAME = ROLE_NAME + LEADER_PROVIDER_BEAN_NAME_POSTFIX;

    @Autowired
    @Qualifier(LEADER_PROVIDER_BEAN_NAME)
    private LeaderProvider leaderProvider;

    @Autowired
    private TestBean testBean;

    @Before
    public void setUp() {
        this.testBean.reset();
    }

    @Test
    public void testLeaderAwareAnnotatedMethodIsInvokedWhenIsLeader() {
        // given
        Context context = mock(Context.class);
        when(context.isLeader()).thenReturn(true);
        when(leaderProvider.context()).thenReturn(context);

        // when
        testBean.testMethod();

        // then
        assertThat(testBean.isInvoked()).isTrue();
    }

    @Test
    public void testLeaderAwareAnnotatedMethodIsNotInvokedWhenNotLeader() {
        // given
        Context context = mock(Context.class);
        when(context.isLeader()).thenReturn(false);
        when(leaderProvider.context()).thenReturn(context);

        // when
        testBean.testMethod();

        // then
        assertThat(testBean.isInvoked()).isFalse();
    }

    static class TestBean {

        private boolean invoked = false;

        @LeaderAware(ROLE_NAME)
        void testMethod() {
            this.invoked = true;
        }

        void reset() {
            this.invoked = false;
        }

        boolean isInvoked() {
            return invoked;
        }
    }

    @Configuration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @MockBean(name = LEADER_PROVIDER_BEAN_NAME)
        public LeaderProvider roleOneLeaderProvider;

        @Bean
        public LeaderAwareAspect leaderAwareAspect() {
            return new LeaderAwareAspect();
        }

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }

    }

}
