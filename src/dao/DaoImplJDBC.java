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
import java.time.LocalDateTime;
import java.util.ArrayList;

import model.Employee;
import model.Product;
import model.Amount;

public class DaoImplJDBC implements Dao {
	Connection connection;
	private static final String GET_INVENTORY = "SELECT name, price, wholesalerPrice, available, stock FROM inventory";
	private static final String INSERT_HISTORICAL_INVENTORY = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) " + "VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_PRODUCT = "INSERT INTO inventory (name, wholesalerPrice, available, stock, price) VALUES (?, ?, ?, ?, ?)";

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
	public boolean writeInventory(ArrayList<Product> products) {
		LocalDateTime now = LocalDateTime.now();

		try (PreparedStatement ps = connection.prepareStatement(INSERT_HISTORICAL_INVENTORY)) {
			
			for (Product product : products) {
				
				ps.setInt(1, product.getId());
				ps.setString(2, product.getName());
				ps.setDouble(3, product.getWholesalerPrice().getValue()); 
				ps.setBoolean(4, product.isAvailable());
				ps.setInt(5, product.getStock());
				ps.setObject(6, now);

				ps.executeUpdate();
			}
			
			System.out.println("Inventario guardado en base de datos correctamente.");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
    public void addProduct(Product product) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_PRODUCT)) {
            ps.setString(1, product.getName());
            
            ps.setDouble(2, product.getWholesalerPrice().getValue());
            
            ps.setBoolean(3, product.isAvailable());
            
            ps.setInt(4, product.getStock());            

            ps.setDouble(5, product.getPublicPrice().getValue());

            ps.executeUpdate();
            
            System.out.println("Producto " + product.getName() + " insertado en BBDD correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public void updateProduct(Product product) {
        String query = "UPDATE inventory SET stock = ?, price = ?, wholesalerPrice = ?, available = ? WHERE name = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setInt(1, product.getStock());
            
            ps.setDouble(2, product.getPublicPrice().getValue());
            
            ps.setDouble(3, product.getWholesalerPrice().getValue());
            
            ps.setBoolean(4, product.isAvailable());
            
            ps.setString(5, product.getName());
            
            int rowsAffected = ps.executeUpdate();
            
            if(rowsAffected > 0) {
                System.out.println("Producto actualizado en BBDD: " + product.getName());
            } else {
                System.out.println("No se encontró el producto en BBDD para actualizar.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	@Override
    public void deleteProduct(Product product) {
        String query = "DELETE FROM inventory WHERE name = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setString(1, product.getName());
            
            int rowsAffected = ps.executeUpdate();
            
            if(rowsAffected > 0) {
                System.out.println("Producto eliminado de BBDD: " + product.getName());
            } else {
                System.out.println("No se pudo eliminar (producto no encontrado en BBDD).");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
