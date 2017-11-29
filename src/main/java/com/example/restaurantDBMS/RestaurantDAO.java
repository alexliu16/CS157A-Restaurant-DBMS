package com.example.restaurantDBMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.core.RowMapper;

/**
 * The class used to connect to the MySQL database
 * @author alexliu
 *
 */
@Component
public class RestaurantDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public RestaurantDAO() {
		jdbcTemplate = new JdbcTemplate();
	}
	
	//-------------------------------------Query-------------------------------------------------------------//

	//Returns a list of all the users in the database
	public List<User> getAllUsers() {
		return jdbcTemplate.query("select * from Users", new UserRowMapper());  
	}

	//Returns a User with the with the given username - null if doesn't exist
	public User searchUser(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("select * from Users WHERE username = '" + username + "'", new UserRowMapper()));
	}
	
	//Returns a list of all the employees in the database
	public List<Employee> getAllEmployees() {
		return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees", new EmployeeRowMapper());  
	}
	
	//Returns an Employee with the with the given username - null if doesn't exist
	public Employee searchEmployee(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees WHERE username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	//Returns the owner of the restaurant
	public Employee searchRestaurantOwner(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees WHERE position = 'Owner' AND username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	//Returns the customer with the given username - null if it doesn't exist
	public Customer searchCustomer(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Customers NATURAL JOIN CustomerCreditCards WHERE username = '" + username + "'", new CustomerRowMapper()));
	}
	
	//Returns a list of all the customers in the database
	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Customers NATURAL JOIN CustomerCreditCards", new CustomerRowMapper());
	}
	
	//Returns a list of all the items on the menu
	public List<MenuItem> getAllMenuItems() {
		return jdbcTemplate.query("SELECT * FROM MenuItems", new MenuItemRowMapper());
	}
	
	//Returns a list of all the dinein orders managed by an employee
	public List<DineinOrder> getEmployeeOrders(Employee emp) {
		return jdbcTemplate.query("SELECT order_id, table_number, time_of_order, status FROM"
				+ "(SELECT * FROM DineinOrders NATURAL JOIN Orders NATURAL JOIN EmployeeManage WHERE employee_username = '" + emp.getUsername() + "') AS S", new DineinOrderRowMapper());
	}
	
	//Returns a list of all the takeout orders
	public List<TakeoutOrder> getTakeoutOrders() {
		return jdbcTemplate.query("SELECT * FROM Orders NATURAL JOIN TakeoutOrders", new TakeoutOrderRowMapper());
	}
	
	//Returns a TreeMap of the items in a given order mapped to the quantity requested
	public TreeMap<MenuItem, Integer> getOrderItems(Order order){
		TreeMap<MenuItem, Integer> items = new TreeMap<>();
		//Get list of MenuItems in the order
		List<MenuItem> listOrderItems = jdbcTemplate.query("SELECT name, type, description, price FROM MenuItems NATURAL JOIN" + 
				"(SELECT order_id, item_name AS name FROM OrderContains WHERE order_id = '" + order.getOrderID() + "'" + ") as S", new MenuItemRowMapper());
		for(MenuItem item: listOrderItems) {
			int quantity = jdbcTemplate.queryForObject("SELECT quantity FROM OrderContains WHERE item_name = '" + item.getName() + "'", Integer.class);
			items.put(item, quantity);
		}
		return items;
	}
	
	//Gets the maximum ID of any user + 1
	public int getMaxID(){
		Integer maxID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM Users", Integer.class);
		return Optional.ofNullable(maxID).orElse(0);
	}
	
	//Return the highest max order id + 1
	public int getMaxOrderID() {
		Integer maxOrderID = jdbcTemplate.queryForObject("SELECT MAX(order_id) FROM Orders", Integer.class);
		return Optional.ofNullable(maxOrderID).orElse(0);
	}
	
	//--------------------------------------------Insertion-------------------------------------------------
	
	//Adds a new user to the database
	public void addUser(User user) {
		jdbcTemplate.update("INSERT INTO Users(username, password, id, name, birthday, email, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[]
			{user.getUsername(), user.getPassword(), user.getId(), user.getName(), user.getBirthday(), user.getEmail(), user.getPhoneNumber()});
	}
	
	//Adds a new customer to the database
	public void addCustomer(Customer cust) {
		jdbcTemplate.update("INSERT INTO Customers VALUES (?, ?)", cust.getUsername(), cust.getCreditCardNumber());
		jdbcTemplate.update("INSERT INTO CustomerCreditCards VALUES (?, ?, ?)", new Object[]
			{cust.getCreditCardNumber(), cust.getExpirationDate(), cust.getCvv()});
	}
	
	//Adds a new employee to the database
	public void addEmployee(Employee employee) {
		//update information in users and employees relations
		jdbcTemplate.update("INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?)", employee.getUsername(),
				employee.getPassword(), employee.getId(), employee.getName(), employee.getBirthday(),
				employee.getEmail(), employee.getPhoneNumber());
		jdbcTemplate.update("INSERT INTO Employees VALUES (?, ?, ?)", employee.getUsername(),
				employee.getPosition(), employee.getSalary());
	}
	
	//Adds a new item to the menu 
	public void addMenuItem(MenuItem item) {
		jdbcTemplate.update("INSERT INTO MenuItems Values (?, ?, ?, ?)", item.getName(), item.getType(), item.getDescription(), item.getPrice());
	}
	
	//Creates a new order in the database
	public void addOrder(Order order) {
		jdbcTemplate.update("INSERT INTO Orders Values (?, ?, ?)", order.getOrderID(), order.getTimeOfOrder(), order.getOrderStatus());
	}
	
	//Creates a new takeout order
	public void addTakeoutOrder(Order order, Address addr) {
		jdbcTemplate.update("INSERT INTO TakeoutOrders Values (?, ?, ?, ?, ?)", 
				order.getOrderID(), addr.getStreet(), addr.getCity(), addr.getState(), addr.getZip()); 
	}
	
	//Creates a new dine-in order 
	public void addDineinOrder(Order order, int tableNum) {
		jdbcTemplate.update("INSERT INTO DineinOrders Values (?, ?)", order.getOrderID(), tableNum);
	}
	
	// Maps an order to the customer who placed it
	public void addOrderToCustomer(Customer cust, Order order) {
		jdbcTemplate.update("INSERT INTO CustomerPlace Values (?, ?)", cust.getUsername(), order.getOrderID());
	}

	// Maps a dine-in order to an employee who will manage it
	public void addOrderToEmployee(Employee emp, Order order) {
		jdbcTemplate.update("INSERT INTO EmployeeManage Values (?, ?)", emp.getUsername(), order.getOrderID());
	}

	// Links a menu item and quantity to an order
	public void addOrderItem(Order order, MenuItem item, int quantity) {
		jdbcTemplate.update("INSERT INTO OrderContains Values (?, ?, ?)", order.getOrderID(), item.getName(), quantity);
	}
	
	//---------------------------Modification-----------------------------------------------------------------
	
	//Updates the user with a new password
	public void updatePassword(String username, String newPassword) {
		jdbcTemplate.update("UPDATE Users SET password = ? WHERE username = ?", newPassword, username);
	}
	
	//Updates an employee's email and phone number
	public void updateEmployeeEmailPhone(String username, String email, String phone) {
		jdbcTemplate.update("UPDATE Users SET email = ?, phoneNumber = ? WHERE username = ?", email, phone, username);
	}
	
	//Update an employee's position at the restaurant
	public void updateEmployeePosition(String username, String newPosition){
		jdbcTemplate.update("UPDATE Employees SET position = ? WHERE username = ? ", newPosition, username);
	}
	
	//Update the salary of an employee
	public void updateEmployeeSalary(String username, int salary){
		jdbcTemplate.update("UPDATE Employees SET salary = ? WHERE username = ? ", salary, username);
	}
	
	//Update the customer's credit card information
	public void updateCustomerCC(String username, long oldCCNo, long newCCNo, String expDate, int cvv){
		jdbcTemplate.update("UPDATE Customers SET CreditCardNumber = ? WHERE username = ?", newCCNo, username);
		jdbcTemplate.update("UPDATE CustomerCreditCards SET CreditCardNumber = ?, ExpirationDate = ?, CVV = ? WHERE CreditCardNumber = ?", 
				newCCNo, expDate, cvv, oldCCNo);
	}
	
	//Update a menu item's information
	public void updateMenuItem(MenuItem newItem, String oldItemName) {
		jdbcTemplate.update("UPDATE MenuItems SET Name = ?, Type = ?, Description = ?, Price = ? WHERE Name = ?",
				newItem.getName(), newItem.getType(), newItem.getDescription(), newItem.getPrice(), oldItemName); 
	}
	
	//Update the status of the order
	public void updateOrderStatus(Order order, String status) {
		jdbcTemplate.update("UPDATE Orders SET status = ? WHERE order_id = ?", status, order.getOrderID());
	}
	
	//---------------------------Deletion-----------------------------------------------------//
	
	//Delete a user from the database
	public void deleteUser(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username);
	}
	
	//Delete an employee from the database
	public void deleteEmployee(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username); //delete user info
		jdbcTemplate.update("DELETE FROM Employees WHERE username = ?", username); //delete employee info
	}
	
	//Delete a menu item from the database
	public void deleteMenuItem(String itemName) {
		jdbcTemplate.update("DELETE FROM MenuItems WHERE Name = ?", itemName);
	}
	
	//Delete all items from an order with the given ID
	public void deleteOrderItems(Order order) {
		jdbcTemplate.update("DELETE FROM OrderContains WHERE order_id = ?", order.getOrderID());
	}
	
	//----------------------Inner Classes----------------------------------------------------//
	
	private class UserRowMapper implements RowMapper<User>{
		
		 @Override  
		    public User mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new User(rs.getString(1), rs.getString(2), rs.getInt(3),
		        		rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
		    }  
	}
	
	private class EmployeeRowMapper implements RowMapper<Employee>{
		
		 @Override  
		    public Employee mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new Employee(rs.getString(1), rs.getString(2), rs.getInt(3),
		        		rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9));
		    }  
	}
	
	private class CustomerRowMapper implements RowMapper<Customer>{
		
		 @Override  
		    public Customer mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new Customer(rs.getString(2), rs.getString(3), rs.getInt(4),
		        		rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getLong(1), rs.getString(9), rs.getInt(10));
		    }  
	}
	
	private class MenuItemRowMapper implements RowMapper<MenuItem>{
		
		 @Override  
		    public MenuItem mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new MenuItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4));
		    }  
	}
	
	private class DineinOrderRowMapper implements RowMapper<DineinOrder>{
		
		 @Override  
		    public DineinOrder mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new DineinOrder(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
		    }  
	}
	
	private class TakeoutOrderRowMapper implements RowMapper<TakeoutOrder>{
		
		 @Override  
		    public TakeoutOrder mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new TakeoutOrder(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(3));
		    }  
	}

}


