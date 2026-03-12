package dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.OffsetDateTime;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {

    private MongoClient mongoClient;
    private MongoDatabase database;

    @Override
    public void connect() {
        try {
            if (mongoClient == null) {
                mongoClient = new MongoClient("localhost", 27017);
                database = mongoClient.getDatabase("shop");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error conectando con MongoDB");
        }
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        if (database == null) connect();
        
        MongoCollection<Document> collection = database.getCollection("users");
        
        Document query = new Document("employeeId", employeeId).append("password", password);
        Document doc = collection.find(query).first();
        
        if (doc != null) {
            String name = doc.containsKey("name") ? doc.getString("name") : "Empleado";
            return new Employee(employeeId, name, password); 
        }
        return null; 
    }

    @Override
    public ArrayList<Product> getInventory() {
        if (database == null) connect();
        ArrayList<Product> inventory = new ArrayList<>();
        
        MongoCollection<Document> collection = database.getCollection("inventory");
        
        for (Document doc : collection.find()) {
            int id = doc.getInteger("id");
            String name = doc.getString("name");
            boolean available = doc.getBoolean("available");
            int stock = doc.getInteger("stock");
            
            Document priceDoc = (Document) doc.get("wholesalerPrice");
            double value = priceDoc.getDouble("value");
            
            Amount wholesalerPrice = new Amount(value);
            
            Product p = new Product(name, wholesalerPrice, available, stock); 
            p.setId(id);
            
            inventory.add(p);
        }
        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> productsList) {
        if (database == null) connect();
        try {
            MongoCollection<Document> collection = database.getCollection("historical_inventory");
            ArrayList<Document> documents = new ArrayList<>();
            
            String createdAt = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            
            for (Product p : productsList) {
                Document priceDoc = new Document("value", p.getWholesalerPrice().getValue())
                                        .append("currency", "€");
                
                Document doc = new Document("id", p.getId())
                                .append("name", p.getName())
                                .append("wholesalerPrice", priceDoc)
                                .append("available", p.isAvailable())
                                .append("stock", p.getStock())
                                .append("created_at", createdAt);
                
                documents.add(doc);
            }
            
            if (!documents.isEmpty()) {
                collection.insertMany(documents);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        if (database == null) connect();
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");
            
            Document priceDoc = new Document("value", product.getWholesalerPrice().getValue())
                                    .append("currency", "€");
                                    
            Document doc = new Document("id", product.getId())
                            .append("name", product.getName())
                            .append("wholesalerPrice", priceDoc)
                            .append("available", product.isAvailable())
                            .append("stock", product.getStock());
                            
            collection.insertOne(doc);
            System.out.println("Producto añadido en MongoDB: " + product.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (database == null) connect();
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");
            
            Document priceDoc = new Document("value", product.getWholesalerPrice().getValue())
                                    .append("currency", "€");
                                    
            Document updateDoc = new Document("$set", new Document("name", product.getName())
                            .append("wholesalerPrice", priceDoc)
                            .append("available", product.isAvailable())
                            .append("stock", product.getStock()));
                            
            collection.updateOne(eq("id", product.getId()), updateDoc);
            System.out.println("Producto actualizado en MongoDB: " + product.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int id) {
        if (database == null) connect();
        try {
            MongoCollection<Document> collection = database.getCollection("inventory");
            
            collection.deleteOne(eq("id", id));
            System.out.println("Producto eliminado con ID en MongoDB: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}