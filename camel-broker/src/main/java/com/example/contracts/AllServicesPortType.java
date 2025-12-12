package com.example.contracts;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService(
    targetNamespace = "http://example.com/contracts",
    name = "AllServicesPortType"
)
public interface AllServicesPortType {
    
    @WebMethod
    String processPayment(String paymentData);
    
    @WebMethod
    String createShipment(String shipmentData);
    
    @WebMethod
    String generateInvoice(String invoiceData);
}
