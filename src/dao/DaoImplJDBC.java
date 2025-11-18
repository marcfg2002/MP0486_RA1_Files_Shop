package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Employee;
import model.Product;
import model.Amount;

public class DaoImplJDBC implements Dao {
	Connection connection;
	private static final String GET_INVENTORY = "SELECT name, price, wholesalerPrice, available, stock FROM inventory";

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> inventory = new ArrayList<>();
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_INVENTORY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                	
                	String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    double wholesalerPrice = resultSet.getDouble("wholesalerPrice");
                    boolean available = resultSet.getBoolean("available");
                    int stock = resultSet.getInt("stock");

                    Product product = new Product(name, new Amount(price), available, stock);
                    inventory.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> ProductsList) {
	    LocalDate myObj = LocalDate.now();
	    String fileName = "inventory_" + myObj.toString() + ".txt";

	    // locate file, path and name
	    File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);
	           
	    try (

	        FileWriter fw = new FileWriter(f, false); 
	        PrintWriter pw = new PrintWriter(fw)
	    ) {

	        int counterProduct = 1;
	        for (Product product : ProductsList) {				
	            StringBuilder firstLine = new StringBuilder(
	                counterProduct + "Id=" + product.getId() + ";" +
	                "Name=" + product.getName() + ";" + 
	                "WholesalerPrice=" + product.getWholesalerPrice() + ";" +
	                "PublicPrice=" + product.getPublicPrice() + ";" +
	                "Available=" + product.isAvailable() + ";" +
	                "Stock=" + product.getStock() + ";"
	            );
	            pw.println(firstLine.toString());
	            
	            counterProduct++;
	        }
	        
	        return true;
	        
	    } catch (IOException e) {
	        System.err.println("Error al escribir el inventario en: " + f.getAbsolutePath());
	        e.printStackTrace();
	        return false;
	    }
	}

}
