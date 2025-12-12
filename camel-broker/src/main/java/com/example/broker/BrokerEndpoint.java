package com.example.broker;

import jakarta.jws.WebService;
import com.example.contracts.AllServicesPortType;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(
    serviceName = "AllServices",
    portName = "AllServicesPort",
    targetNamespace = "http://example.com/contracts",
    endpointInterface = "com.example.contracts.AllServicesPortType"
)
public class BrokerEndpoint implements AllServicesPortType {

    private final AllServicesPortType billingServiceProxy;
    private final AllServicesPortType inventoryServiceProxy;
    private final AllServicesPortType customerServiceProxy;

    public BrokerEndpoint(
            @Qualifier("billingServiceProxy") AllServicesPortType billingServiceProxy,
            @Qualifier("inventoryServiceProxy") AllServicesPortType inventoryServiceProxy,
            @Qualifier("customerServiceProxy") AllServicesPortType customerServiceProxy) {
        this.billingServiceProxy = billingServiceProxy;
        this.inventoryServiceProxy = inventoryServiceProxy;
        this.customerServiceProxy = customerServiceProxy;
    }

    @Override
    public String processPayment(String paymentData) {
        System.out.println("🔀 Broker routing processPayment to billing-service");
        return billingServiceProxy.processPayment(paymentData);
    }

    @Override
    public String createShipment(String shipmentData) {
        System.out.println("🔀 Broker routing createShipment to inventory-service");
        return inventoryServiceProxy.createShipment(shipmentData);
    }

    @Override
    public String generateInvoice(String invoiceData) {
        System.out.println("🔀 Broker routing generateInvoice to customer-service");
        return customerServiceProxy.generateInvoice(invoiceData);
    }
}

