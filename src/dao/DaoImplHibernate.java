package dao;


import java.util.ArrayList;
import model.Employee;
import model.Product;


public class DaoImplHibernate implements Dao {

	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> ProductsList) {
	    return false;
	}
	
	public void addProduct(Product product) {

	}
	
	public void updateProduct(Product product) {
		
	}
	
	public void deleteProduct(Product product) {
		
	}
}
