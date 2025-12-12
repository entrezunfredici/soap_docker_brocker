package com.example.customer.config;

import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.customer.endpoint.CustomerEndpoint;

@Configuration
public class CxfCustomerConfig {

    @Autowired
    private Bus bus;

    @Bean
    public CustomerEndpoint customerEndpoint() {
        return new CustomerEndpoint();
    }

    @Bean
    public Endpoint customerEndpointMapping(CustomerEndpoint customerEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, customerEndpoint);
        endpoint.publish("/soap/invoice");
        return endpoint;
    }
}
