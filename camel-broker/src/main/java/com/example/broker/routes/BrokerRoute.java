package com.example.broker.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel route not needed - using direct CXF endpoint routing instead
 * The BrokerEndpoint handles the routing directly to the microservices
 */
@Component
public class BrokerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Routes disabled - using direct CXF proxy routing
    }
}

