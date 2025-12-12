package com.example.broker.config;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.broker.BrokerEndpoint;
import com.example.contracts.AllServicesPortType;

@Configuration
public class CxfBrokerEndpointConfig {

    @Autowired
    private Bus bus;

    @Bean
    public BrokerEndpoint brokerEndpoint(
            @Qualifier("billingServiceProxy") AllServicesPortType billingServiceProxy,
            @Qualifier("inventoryServiceProxy") AllServicesPortType inventoryServiceProxy,
            @Qualifier("customerServiceProxy") AllServicesPortType customerServiceProxy) {
        return new BrokerEndpoint(billingServiceProxy, inventoryServiceProxy, customerServiceProxy);
    }

    @Bean
    public Endpoint brokerEndpointMapping(BrokerEndpoint brokerEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, brokerEndpoint);
        endpoint.publish("/soap/broker");
        return endpoint;
    }
}
