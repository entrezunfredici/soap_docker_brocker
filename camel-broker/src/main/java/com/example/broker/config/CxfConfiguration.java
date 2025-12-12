package com.example.broker.config;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.contracts.AllServicesPortType;

@Configuration
public class CxfConfiguration {

    @Bean("brokerService")
    public CxfEndpoint brokerService() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setAddress("http://0.0.0.0:8080/soap/broker");
        endpoint.setServiceClass(AllServicesPortType.class);
        endpoint.setServiceName("{http://example.com/contracts}AllServices");
        endpoint.setPortName("{http://example.com/contracts}AllServicesPort");
        return endpoint;
    }

    @Bean("billingService")
    public CxfEndpoint billingService() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setAddress("http://billing-service:8080/soap/payment");
        endpoint.setServiceClass(AllServicesPortType.class);
        return endpoint;
    }

    @Bean("inventoryService")
    public CxfEndpoint inventoryService() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setAddress("http://inventory-service:8080/soap/shipping");
        endpoint.setServiceClass(AllServicesPortType.class);
        return endpoint;
    }

    @Bean("customerService")
    public CxfEndpoint customerService() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setAddress("http://customer-service:8080/soap/invoice");
        endpoint.setServiceClass(AllServicesPortType.class);
        return endpoint;
    }
}
