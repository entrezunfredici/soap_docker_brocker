package com.example.billing;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.Endpoint;
import javax.xml.bind.annotation.XmlType;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.*;

@WebService(serviceName = "BillingService", 
            portName = "BillingPort",
            targetNamespace = "http://example.com/billing")
public class BillingEndpoint {
    
    // URL du broker Camel (sur le réseau Docker)
    private static final String BROKER_URL = "http://broker:8080/cxf/soap/broker";
    
    /**
     * Met à jour l'inventaire via le broker
     * @param productId L'ID du produit
     * @param quantity La quantité à déduire
     * @return UpdateInventoryResponse avec succès et message
     */
    @WebMethod
    public UpdateInventoryResponse updateInventory(
            @WebParam(name = "productId") int productId,
            @WebParam(name = "quantity") int quantity) {
        
        UpdateInventoryResponse response = new UpdateInventoryResponse();
        
        try {
            // Appel au broker pour mettre à jour l'inventaire
            System.out.println("🔄 Appel au broker pour mettre à jour l'inventaire - Produit ID: " + productId + ", Quantité: " + quantity);
            
            // Simulation de l'appel au broker (à remplacer par un vrai appel SOAP)
            // Dans une vraie implémentation, vous utiliseriez un client SOAP dynamique
            // pour appeler le broker qui routera vers l'inventory-service
            
            boolean success = callInventoryServiceViaBroker(productId, quantity);
            
            response.setSuccess(success);
            response.setMessage(success ? 
                "Inventaire mis à jour avec succès pour le produit " + productId : 
                "Échec de la mise à jour de l'inventaire");
                
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Génère une facture pour une liste d'IDs de produits
     * @param productIds Liste des IDs de produits
     * @return GenerateInvoiceResponse avec détails de la facture
     */
    @WebMethod
    public GenerateInvoiceResponse generateInvoice(
        @WebParam(name = "productIds") List<Integer> productIds) {
        
        GenerateInvoiceResponse response = new GenerateInvoiceResponse();
        
        try {
            System.out.println("📄 Génération de facture pour " + productIds.size() + " produits");
            
            // Génération d'un ID de facture unique
            String invoiceId = "INV-" + System.currentTimeMillis();
            response.setInvoiceId(invoiceId);
            
            // Création de la liste des items
            List<InvoiceItem> items = new ArrayList<>();
            double total = 0.0;
            
            // Pour chaque ID de produit, récupérer les informations et calculer le prix
            for (Integer productId : productIds) {
                InvoiceItem item = createInvoiceItem(productId);
                items.add(item);
                total += item.getSubtotal();
                
                // Mise à jour automatique de l'inventaire lors de la facturation
                updateInventory(productId, item.getQuantity());
            }
            
            response.setItems(items);
            response.setTotalAmount(total);
            
            System.out.println("✅ Facture générée: " + invoiceId + " - Total: " + total + "€");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la génération de facture: " + e.getMessage());
            e.printStackTrace();
            response.setInvoiceId("ERROR");
            response.setTotalAmount(0.0);
        }
        
        return response;
    }
    
    /**
     * Appelle l'inventory service via le broker
     */
    private boolean callInventoryServiceViaBroker(int productId, int quantity) {
        try {
            System.out.println("📞 Appel au broker: " + BROKER_URL);
            System.out.println("   Operation: updateStock");
            System.out.println("   ProductId: " + productId + ", Quantity: " + quantity);
            
            // Construction de la requête SOAP pour le broker
            String soapRequest = 
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:inv=\"http://example.com/inventory\">" +
                "   <soapenv:Header>" +
                "      <operationName>updateStock</operationName>" +
                "   </soapenv:Header>" +
                "   <soapenv:Body>" +
                "      <inv:updateStockRequest>" +
                "         <inv:productId>" + productId + "</inv:productId>" +
                "         <inv:quantity>" + quantity + "</inv:quantity>" +
                "      </inv:updateStockRequest>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
            
            // Envoi de la requête HTTP au broker
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) 
                new URL(BROKER_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", "updateStock");
            connection.setDoOutput(true);
            
            // Écriture de la requête
            try (java.io.OutputStream os = connection.getOutputStream()) {
                byte[] input = soapRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Lecture de la réponse
            int responseCode = connection.getResponseCode();
            System.out.println("   ✅ Broker réponse: " + responseCode);
            
            if (responseCode == 200) {
                // Lecture du corps de la réponse
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("   📦 Réponse du broker reçue");
                    // Vérifier si la réponse contient "success"
                    return response.toString().contains("true") || 
                           response.toString().contains("success");
                }
            } else {
                System.err.println("   ❌ Erreur HTTP: " + responseCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'appel au broker: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Crée un item de facture pour un produit donné
     * (Dans une vraie application, ces données viendraient d'une base de données)
     */
    private InvoiceItem createInvoiceItem(int productId) {
        InvoiceItem item = new InvoiceItem();
        item.setProductId(productId);
        
        // Simulation de données produit (à remplacer par une vraie requête DB)
        Map<Integer, ProductData> productCatalog = getProductCatalog();
        ProductData product = productCatalog.getOrDefault(productId, 
            new ProductData("Produit inconnu", 0.0, 1));
        
        item.setProductName(product.name);
        item.setPrice(product.price);
        item.setQuantity(product.quantity);
        item.setSubtotal(product.price * product.quantity);
        
        return item;
    }
    
    /**
     * Catalogue de produits simulé
     */
    private Map<Integer, ProductData> getProductCatalog() {
        Map<Integer, ProductData> catalog = new HashMap<>();
        catalog.put(1, new ProductData("Ordinateur portable", 899.99, 1));
        catalog.put(2, new ProductData("Souris sans fil", 29.99, 2));
        catalog.put(3, new ProductData("Clavier mécanique", 129.99, 1));
        catalog.put(4, new ProductData("Écran 27 pouces", 299.99, 1));
        catalog.put(5, new ProductData("Webcam HD", 79.99, 1));
        return catalog;
    }
    
    // Classes internes pour les types de données
    @XmlType(name = "UpdateInventoryResponseType")
    public static class UpdateInventoryResponse {
        private boolean success;
        private String message;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    @XmlType(name = "GenerateInvoiceResponseType")
    public static class GenerateInvoiceResponse {
        private String invoiceId;
        private List<InvoiceItem> items = new ArrayList<>();
        private double totalAmount;
        
        public String getInvoiceId() { return invoiceId; }
        public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
        public List<InvoiceItem> getItems() { return items; }
        public void setItems(List<InvoiceItem> items) { this.items = items; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    }
    
    public static class InvoiceItem {
        private int productId;
        private String productName;
        private double price;
        private int quantity;
        private double subtotal;
        
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
    
    private static class ProductData {
        String name;
        double price;
        int quantity;
        
        ProductData(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
    
    /**
     * Main pour démarrer le service
     */
    public static void main(String[] args) {
        String url = "http://localhost:8084/ws/billing";
        System.out.println("🚀 Démarrage du Billing Service sur " + url);
        Endpoint.publish(url, new BillingEndpoint());
        System.out.println("✅ Billing Service démarré avec succès!");
    }
}
