package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {

    private SessionFactory sessionFactory;
    private Session session;

    @Override
    public void connect() {
        try {
            if (sessionFactory == null || sessionFactory.isClosed()) {
                Configuration config = new Configuration();
                config.configure("hibernate.cfg.xml");
                sessionFactory = config.buildSessionFactory();
            }

            if (session == null || !session.isOpen()) {
                session = sessionFactory.openSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error conectando con Hibernate");
        }
    }

    @Override
    public void disconnect() {
        if (session != null && session.isOpen()) {
            session.close();
        }
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        return null; 
    }

    @Override
    public ArrayList<Product> getInventory() {
        if (session == null || !session.isOpen()) connect();       
        Query<Product> query = session.createQuery("FROM Product", Product.class);
        List<Product> list = query.list();        
        return new ArrayList<>(list);
    }

    @Override
    public boolean writeInventory(ArrayList<Product> productsList) {
        if (session == null || !session.isOpen()) connect();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Product p : productsList) {
                ProductHistory history = new ProductHistory(p);
                session.save(history);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        if (session == null || !session.isOpen()) connect();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
            System.out.println("Producto añadido: " + product.getName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (session == null || !session.isOpen()) connect();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
            System.out.println("Producto actualizado: " + product.getName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int id) {
        if (session == null || !session.isOpen()) connect();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();           
            Product product = session.get(Product.class, id);
            
            if (product != null) {
                session.delete(product);
                System.out.println("Producto eliminado con ID: " + id);
            } else {
                System.out.println("No se encontró producto con ID: " + id);
            }            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}