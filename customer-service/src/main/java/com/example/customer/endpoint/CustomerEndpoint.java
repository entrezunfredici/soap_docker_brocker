package com.example.customer.endpoint;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * CustomerEndpoint - Les 3 opérations SOAP
 */
@WebService(
    serviceName = "CustomerService",
    portName = "CustomerPort",
    targetNamespace = "http://example.com/customer"
)
public class CustomerEndpoint {

    /**
     * OPERATION 1 : checkStock
     * Envoie : productId (int) + quantity (int)
     * Reçoit : available (boolean)
     */
    @WebMethod
    public boolean checkStock(
        @WebParam(name = "productId") int productId,
        @WebParam(name = "quantity") int quantity
    ) {
        System.out.println("📩 checkStock called: productId=" + productId + ", quantity=" + quantity);
        
        // Logique de vérification du stock (simulation)
        // Dans une vraie app, tu interrogerais une DB ou un autre service
        
        // Simulation : produits 1-3 ont du stock, produit 4 est en rupture
        if (productId == 4) {
            System.out.println("❌ Product " + productId + " out of stock");
            return false;
        }
        
        // Simulation : si quantité > 100, pas dispo
        if (quantity > 100) {
            System.out.println("❌ Insufficient stock for quantity " + quantity);
            return false;
        }
        
        System.out.println("✅ Product " + productId + " available for quantity " + quantity);
        return true;
    }

    /**
     * OPERATION 2 : receiveAvailability
     * Reçoit : available (boolean)
     * Renvoie : acknowledged (boolean)
     */
    @WebMethod
    public boolean receiveAvailability(
        @WebParam(name = "available") boolean available
    ) {
        System.out.println("📩 receiveAvailability called: available=" + available);
        
        // Traiter la notification de disponibilité
        if (available) {
            System.out.println("✅ Product is now available - notification received");
        } else {
            System.out.println("❌ Product is NOT available - notification received");
        }
        
        // Toujours retourner true pour indiquer qu'on a bien reçu la notification
        return true;
    }

    /**
     * OPERATION 3 : createInvoice
     * Envoie : items (array de {productId, quantity})
     * Reçoit : void (pas de retour utile)
     */
    @WebMethod
    public void createInvoice(
        @WebParam(name = "items") InvoiceItem[] items
    ) {
        System.out.println("📩 createInvoice called with " + items.length + " items");
        
        double total = 0.0;
        for (InvoiceItem item : items) {
            System.out.println("   - Product " + item.getProductId() + 
                             " x" + item.getQuantity());
            
            // Calcul fictif du prix
            double price = item.getProductId() * 10.0;
            total += price * item.getQuantity();
        }
        
        System.out.println("💰 Total invoice: " + total + "€");
        System.out.println("✅ Invoice creation requested");
        
        // Dans une vraie app, tu sauvegarderais la facture en DB
        // ou tu enverrais un message à un autre service
    }

    /**
     * Classe interne pour représenter un item de facture
     */
    public static class InvoiceItem {
        private int productId;
        private int quantity;

        public InvoiceItem() {}

        public InvoiceItem(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
