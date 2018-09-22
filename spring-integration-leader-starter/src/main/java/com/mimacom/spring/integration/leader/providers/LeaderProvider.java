package com.mimacom.spring.integration.leader.providers;

import org.springframework.integration.leader.Context;

public interface LeaderProvider {

    Context context();

}
