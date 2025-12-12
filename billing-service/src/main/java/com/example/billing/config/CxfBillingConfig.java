package com.example.billing.config;

import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.billing.BillingEndpoint;

@Configuration
public class CxfBillingConfig {

    @Autowired
    private Bus bus;

    @Bean
    public BillingEndpoint billingEndpoint() {
        return new BillingEndpoint();
    }

    @Bean
    public Endpoint billingEndpointMapping(BillingEndpoint billingEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, billingEndpoint);
        endpoint.publish("/soap/payment");
        return endpoint;
    }
}
