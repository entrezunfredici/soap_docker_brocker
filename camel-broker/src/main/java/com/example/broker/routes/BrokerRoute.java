package com.example.broker.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BrokerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // 1️⃣ CXF SOAP endpoint that the broker exposes
        from("cxf:bean:brokerService")
            .routeId("broker-main-route")
            .log("📩 SOAP request received on operation: ${header.CamelCxfOperationName}")

            // 2️⃣ Route based on the SOAP operation
            .choice()
                .when(header("CamelCxfOperationName").isEqualTo("processPayment"))
                    .log("➡️ Routing to billing-service")
                    .to("cxf:bean:billingService")
                .when(header("CamelCxfOperationName").isEqualTo("createShipment"))
                    .log("➡️ Routing to inventory-service")
                    .to("cxf:bean:inventoryService")
                .when(header("CamelCxfOperationName").isEqualTo("generateInvoice"))
                    .log("➡️ Routing to customer-service")
                    .to("cxf:bean:customerService")
                .otherwise()
                    .log("❌ Unknown operation: ${header.CamelCxfOperationName}")
            .end();
    }
}
