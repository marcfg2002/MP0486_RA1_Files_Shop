package dao;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import model.Employee;
import model.Product;


public class DaoImplObjectDB implements Dao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void connect() {
        try {
            if (emf == null || !emf.isOpen()) {
                emf = Persistence.createEntityManagerFactory("objects/users.odb");
            }
            if (em == null || !em.isOpen()) {
                em = emf.createEntityManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error conectando con ObjectDB");
        }
    }

    @Override
    public void disconnect() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        if (em == null || !em.isOpen()) {
            connect();
        }
        
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeId = :id AND e.password = :pass", 
                Employee.class
            );
            query.setParameter("id", employeeId);
            query.setParameter("pass", password);
            
            return query.getSingleResult();
            
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ArrayList<Product> getInventory() {
        return null; 
    }

    @Override
    public boolean writeInventory(ArrayList<Product> ProductsList) {
        return false; 
    }

    @Override
    public void addProduct(Product product) {
    }

    @Override
    public void updateProduct(Product product) {
    }

    @Override
    public void deleteProduct(int id) {
    }
}