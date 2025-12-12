package com.example.broker.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BrokerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // 1️⃣ Endpoint SOAP exposé par Camel en "front"
        // URL du broker que les clients appelleront
        from("cxf:/broker?serviceClass=com.example.contracts.AllServicesPortType")
            .routeId("broker-main-route")
            .log("📩 Broker SOAP reçu : ${header.operationName}")

            // 2️⃣ Route dynamique selon l’opération SOAP
            .choice()
                .when(header("operationName").isEqualTo("processPayment")) // BILLING
                    .log("➡️ Routing vers PaymentService")
                    .to("cxf://http://localhost:8081/ws/payment?serviceClass=com.example.contracts.PaymentPortType")
                .when(header("operationName").isEqualTo("createShipment")) // INVENTORY
                    .log("➡️ Routing vers ShippingService")
                    .to("cxf://http://localhost:8082/ws/shipping?serviceClass=com.example.contracts.ShippingPortType")
                .when(header("operationName").isEqualTo("generateInvoice")) // CUSTOMER
                    .log("➡️ Routing vers InvoiceService")
                    .to("cxf://http://localhost:8083/ws/invoice?serviceClass=com.example.contracts.InvoicePortType")
                .otherwise()
                    .log("❌ Operation non reconnue : ${header.operationName}")
                    .throwException(new IllegalArgumentException("Operation SOAP inconnue"))
            .end();
    }
}


/* version dynamique (ne connait pas les services)
@Component
public class BrokerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:/broker?serviceClass=com.example.contracts.AllServicesPortType")
            .routeId("dynamic-broker")

            .log("📩 Operation reçue : ${header.operationName}")

            // 1️⃣ Set dynamic URL based on operation
            .setHeader("CamelDestinationUrl", simple("http://localhost:808${header.operationName.hashCode() % 3 + 1}/ws"))
            .log("➡️ Dynamically routing to ${header.CamelDestinationUrl}")

            // 2️⃣ Send to dynamic endpoint
            .toD("cxf://#{header.CamelDestinationUrl}?serviceClass=com.example.contracts.AllServicesPortType");
    }
}
*/


/* version sécurité 
from("cxf:/broker?serviceClass=com.example.contracts.AllServicesPortType")
    .routeId("secured-broker")

    .log("🔐 Vérification de l’API Key...")
    .choice()
        .when(header("X-API-KEY").isNotEqualTo("SECRET123"))
            .log("❌ API KEY invalide")
            .throwException(new SecurityException("Unauthorized"))
    .end()
    .log("✔ API Key validée")

    .to("cxf://http://localhost:8081/ws/payment?serviceClass=com.example.contracts.PaymentPortType");
*/