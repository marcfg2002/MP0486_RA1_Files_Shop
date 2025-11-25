package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao {

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
		ArrayList<Product> inventory = new ArrayList<>();
		File file = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] sections = line.split(";");
				String name = "";
				double price = 0.0;
				int stock = 0;

				for (int i = 0; i < sections.length; i++) {
					String[] data = sections[i].split(":");
					switch (i) {
					case 0:
						name = data[1];
						break;
					case 1:
						price = Double.parseDouble(data[1]);
						break;
					case 2:
						stock = Integer.parseInt(data[1]);
						break;
					}
				}
				inventory.add(new Product(name, new Amount(price), true, stock));
			}
		} catch (IOException e) {
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
	
	public void addProduct(Product product) {

	}
	
	public void updateProduct(Product product) {
		
	}
}
