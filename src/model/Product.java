package model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price; 
    
    @Transient
    private double wholesalerPriceDouble; 

    @Column(name = "available")
    private boolean available;

    @Column(name = "stock")
    private int stock;

    @Transient
    private Amount publicPrice;
    @Transient
    private Amount wholesalerPrice;

    @Transient
    private static int totalProducts;
    
    public final static double EXPIRATION_RATE = 0.60;

    public Product() {
    }

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        super();
        this.id = totalProducts + 1;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue());
        
        this.price = this.publicPrice.getValue();
        
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Amount getPublicPrice() {
        if (publicPrice == null) {
            publicPrice = new Amount(price);
        }
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
        this.price = publicPrice.getValue();
    }

    public Amount getWholesalerPrice() {
        if (wholesalerPrice == null) {
            wholesalerPrice = new Amount(wholesalerPriceDouble);
        }
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
        this.wholesalerPriceDouble = wholesalerPrice.getValue();
    }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public static int getTotalProducts() { return totalProducts; }
    public static void setTotalProducts(int totalProducts) { Product.totalProducts = totalProducts; }

    public void expire() {
        this.publicPrice.setValue(this.getPublicPrice().getValue() * EXPIRATION_RATE);
        this.price = this.publicPrice.getValue();
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", publicPrice=" + getPublicPrice() + ", available=" + available + ", stock=" + stock + "]";
    }
}