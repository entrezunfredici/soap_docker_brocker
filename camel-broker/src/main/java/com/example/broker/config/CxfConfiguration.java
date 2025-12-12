package com.example.broker.config;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.contracts.AllServicesPortType;

@Configuration
public class CxfConfiguration {

    @Bean("brokerServiceProxy")
    public AllServicesPortType brokerServiceProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AllServicesPortType.class);
        factory.setAddress("http://0.0.0.0:8080/soap/broker");
        return (AllServicesPortType) factory.create();
    }

    @Bean("billingServiceProxy")
    public AllServicesPortType billingServiceProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AllServicesPortType.class);
        factory.setAddress("http://billing-service:8080/soap/payment");
        return (AllServicesPortType) factory.create();
    }

    @Bean("inventoryServiceProxy")
    public AllServicesPortType inventoryServiceProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AllServicesPortType.class);
        factory.setAddress("http://inventory-service:8080/soap/shipping");
        return (AllServicesPortType) factory.create();
    }

    @Bean("customerServiceProxy")
    public AllServicesPortType customerServiceProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AllServicesPortType.class);
        factory.setAddress("http://customer-service:8080/soap/invoice");
        return (AllServicesPortType) factory.create();
    }
}

