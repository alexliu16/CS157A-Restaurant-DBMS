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
		return jdbcTemplate.query("SELECT * from users", new UserRowMapper());  
	}

	//Returns a User with the with the given username - null if doesn't exist
	public User searchUser(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("select * from users WHERE username = '" + username + "'", new UserRowMapper()));
	}
	
	//Returns a list of all the employees in the database
	public List<Employee> getAllEmployees() {
		return jdbcTemplate.query("SELECT * FROM users NATURAL JOIN employees", new EmployeeRowMapper());  
	}
	
	//Returns an Employee with the with the given username 
	public Employee searchEmployee(String username, String password) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users NATURAL JOIN employees WHERE username = '" + username + "'"
				+ " AND password = '" + password + "'", new EmployeeRowMapper()));
	}
	
	//Returns the owner of the restaurant
	public Employee searchRestaurantOwner(String username, String password) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users NATURAL JOIN employees WHERE position = 'Owner' AND username = '" 
				+ username + "'" + " AND password = '" + password + "'", new EmployeeRowMapper()));
	}
	
	//Returns the customer with the given username 
	public Customer searchCustomer(String username, String password) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users NATURAL JOIN customers NATURAL JOIN customer_credit_cards "
				+ "WHERE username = '" + username + "'" + " AND password = '" + password + "'", new CustomerRowMapper()));
	}
	
	//Returns a list of all the customers in the database
	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query("SELECT * FROM users NATURAL JOIN customers NATURAL JOIN customer_credit_cards", new CustomerRowMapper());
	}
	
	//Returns a list of all the items on the menu
	public List<MenuItem> getAllMenuItems() {
		return jdbcTemplate.query("SELECT * FROM menu_items", new MenuItemRowMapper());
	}
	
	//Returns a list of all the dinein orders managed by an employee
	public List<DineinOrder> getEmployeeOrders(Employee emp) {
		return jdbcTemplate.query("SELECT order_id, table_number, time_of_order, status FROM"
				+ "(SELECT * FROM dinein_orders NATURAL JOIN orders NATURAL JOIN employee_manage WHERE employee_username = '" + emp.getUsername() + "') AS S", new DineinOrderRowMapper());
	}
	
	//Returns a list of all the takeout orders
	public List<TakeoutOrder> getTakeoutOrders() {
		return jdbcTemplate.query("SELECT * FROM orders NATURAL JOIN takeout_orders", new TakeoutOrderRowMapper());
	}
	
	//Returns a TreeMap of the items in a given order mapped to the quantity requested
	public TreeMap<MenuItem, Integer> getOrderItems(Order order){
		TreeMap<MenuItem, Integer> items = new TreeMap<>();
		//Get list of MenuItems in the order
		List<MenuItem> listOrderItems = jdbcTemplate.query("SELECT name, type, description, price FROM menu_items NATURAL JOIN" + 
				"(SELECT order_id, item_name AS name FROM order_contains WHERE order_id = '" + order.getOrderID() + "'" + ") as S", new MenuItemRowMapper());
		for(MenuItem item: listOrderItems) {
			int quantity = jdbcTemplate.queryForObject("SELECT quantity FROM order_contains WHERE item_name = '" + item.getName() + "'"
					+ "AND order_id = '" + order.getOrderID() + "'", Integer.class);
			items.put(item, quantity);
		}
		return items;
	}
	
	//Gets the maximum ID of any user 
	public int getMaxID(){
		Integer maxID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM users", Integer.class);
		return Optional.ofNullable(maxID).orElse(0);
	}
	
	//Return the highest max order id 
	public int getMaxOrderID() {
		Integer maxOrderID = jdbcTemplate.queryForObject("SELECT MAX(order_id) FROM orders", Integer.class);
		return Optional.ofNullable(maxOrderID).orElse(0);
	}
	
	//--------------------------------------------Insertion-------------------------------------------------
	
	//Adds a new user to the database
	public void addUser(User user) {
		jdbcTemplate.update("INSERT INTO users(username, password, id, name, birthday, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[]
			{user.getUsername(), user.getPassword(), user.getId(), user.getName(), user.getBirthday(), user.getEmail(), user.getPhoneNumber()});
	}
	
	//Adds a new customer to the database
	public void addCustomer(Customer cust) {
		jdbcTemplate.update("INSERT INTO users(username, password, id, name, birthday, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[]
				{cust.getUsername(), cust.getPassword(), cust.getId(), cust.getName(), cust.getBirthday(), cust.getEmail(), cust.getPhoneNumber()});
		jdbcTemplate.update("INSERT INTO customers VALUES (?, ?)", cust.getUsername(), cust.getCreditCardNumber());
		jdbcTemplate.update("INSERT INTO customer_credit_cards VALUES (?, ?, ?)", new Object[]
			{cust.getCreditCardNumber(), cust.getExpirationDate(), cust.getCvv()});
	}
	
	//Adds a new employee to the database
	public void addEmployee(Employee employee) {
		//update information in users and employees relations
		jdbcTemplate.update("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)", employee.getUsername(),
				employee.getPassword(), employee.getId(), employee.getName(), employee.getBirthday(),
				employee.getEmail(), employee.getPhoneNumber());
		jdbcTemplate.update("INSERT INTO employees VALUES (?, ?, ?)", employee.getUsername(),
				employee.getPosition(), employee.getSalary());
	}
	
	//Adds a new item to the menu 
	public void addMenuItem(MenuItem item) {
		jdbcTemplate.update("INSERT INTO menu_items Values (?, ?, ?, ?)", item.getName(), item.getType(), item.getDescription(), item.getPrice());
	}
	
	//Creates a new takeout order
	public void addTakeoutOrder(TakeoutOrder order, Map<MenuItem, Integer> orderItems, Customer cust) {
		//update orders and takeout_orders tables
		jdbcTemplate.update("INSERT INTO orders Values (?, ?, ?)", order.getOrderID(), order.getTimeOfOrder(), order.getOrderStatus());
		jdbcTemplate.update("INSERT INTO takeout_orders Values (?, ?, ?)", order.getOrderID(), order.getStreet(), order.getCity()); 
		//add order to customer
		jdbcTemplate.update("INSERT INTO customer_place Values (?, ?)", cust.getUsername(), order.getOrderID());
		//update contain table
		for(MenuItem item: orderItems.keySet())
			jdbcTemplate.update("INSERT INTO order_contains Values (?, ?, ?)", order.getOrderID(), item.getName(), orderItems.get(item));
	}
	
	//Creates a new dine-in order 
	public void addDineinOrder(Order order, Map<MenuItem, Integer> orderItems, int tableNum, Employee emp) {
		jdbcTemplate.update("INSERT INTO orders Values (?, ?, ?)", order.getOrderID(), order.getTimeOfOrder(), order.getOrderStatus());
		jdbcTemplate.update("INSERT INTO dinein_orders Values (?, ?)", order.getOrderID(), tableNum);
		// add order to customer
		jdbcTemplate.update("INSERT INTO employee_manage Values (?, ?)", emp.getUsername(), order.getOrderID());
		// update contain table
		for (MenuItem item : orderItems.keySet())
			jdbcTemplate.update("INSERT INTO order_contains Values (?, ?, ?)", order.getOrderID(), item.getName(),
					orderItems.get(item));
	}

	//---------------------------Modification-----------------------------------------------------------------
	
	//Updates the user with a new password
	public void updatePassword(String username, String newPassword) {
		jdbcTemplate.update("UPDATE users SET password = ? WHERE username = ?", newPassword, username);
	}
	
	//Updates an user's email and phone number
	public void updateUserEmailPhone(String username, String email, String phone) {
		jdbcTemplate.update("UPDATE users SET email = ?, phone_number = ? WHERE username = ?", email, phone, username);
	}
	
	//Update an employee's position and salary
	public void updateEmployeeInfo(String username, String newPosition, int salary){
		jdbcTemplate.update("UPDATE employees SET position = ?, salary = ? WHERE username = ? ", newPosition, salary, username);
	}
	
	//Update the customer's credit card information
	public void updateCustomerCC(String username, long oldCCNo, long newCCNo, String expDate, int cvv){
		jdbcTemplate.update("UPDATE customers SET credit_card_number = ? WHERE username = ?", newCCNo, username);
		jdbcTemplate.update("UPDATE customer_credit_cards SET credit_card_number = ?, expiration_date = ?, cvv = ? WHERE credit_card_number = ?", 
				newCCNo, expDate, cvv, oldCCNo);
	}
	
	//Update a menu item's information
	public void updateMenuItem(MenuItem newItem, String oldItemName) {
		jdbcTemplate.update("UPDATE menu_items SET name = ?, type = ?, description = ?, price = ? WHERE name = ?",
				newItem.getName(), newItem.getType(), newItem.getDescription(), newItem.getPrice(), oldItemName); 
	}
	
	//Update the status of the order
	public void updateOrderStatus(Order order, String status) {
		jdbcTemplate.update("UPDATE orders SET status = ? WHERE order_id = ?", status, order.getOrderID());
	}
	
	public void updateOrderItems(Order order, Map<MenuItem, Integer> orderItems) {
		//Delete all items from an order with the given id
		jdbcTemplate.update("DELETE FROM order_contains WHERE order_id = ?", order.getOrderID());
		//Add new items to the order
		for(MenuItem item: orderItems.keySet())
			jdbcTemplate.update("INSERT INTO order_contains Values (?, ?, ?)", order.getOrderID(), item.getName(), orderItems.get(item));
	}
	
	//---------------------------Deletion-----------------------------------------------------//
	
	//Delete a user from the database
	public void deleteUser(String username){
		jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
	}
	
	//Delete an employee from the database
	public void deleteEmployee(String username){
		jdbcTemplate.update("DELETE FROM users WHERE username = ?", username); //delete user info
		jdbcTemplate.update("DELETE FROM employees WHERE username = ?", username); //delete employee info
	}
	
	//Delete a menu item from the database
	public void deleteMenuItem(String itemName) {
		jdbcTemplate.update("DELETE FROM menu_items WHERE Name = ?", itemName);
	}
	
	//Delete all items from an order with the given ID
	public void deleteOrderItems(Order order) {
		jdbcTemplate.update("DELETE FROM order_contains WHERE order_id = ?", order.getOrderID());
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
		        return new TakeoutOrder(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(3));
		    }  
	}

}


