package com.example;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InventoryEndpoint {

    private static final String NAMESPACE = "http://example.com/inventory";

    @PayloadRoot(namespace = NAMESPACE, localPart = "CheckInventoryRequest")
    @ResponsePayload
    public CheckInventoryResponse handle(@RequestPayload CheckInventoryRequest request) {

        CheckInventoryResponse response = new CheckInventoryResponse();

        // Simple logic
        boolean success = true;

        if ("1".equals(request.getId()) && request.getQuantity() < 5) {
            success = false;
        }

        response.setSuccess(success);
        return response;
    }
}

/*package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InventoryEndpoint {

    private static final String NAMESPACE = "http://example.com/inventory";

    @Autowired
    private InventoryRepository repository;

    @PayloadRoot(namespace = NAMESPACE, localPart = "CheckInventoryRequest")
    @ResponsePayload
    public CheckInventoryResponse checkInventory(@RequestPayload CheckInventoryRequest request) {
        CheckInventoryResponse response = new CheckInventoryResponse();
        response.setSuccess(repository.checkInventory(request.getId(), request.getQuantity()));
        return response;
    }
}
*/