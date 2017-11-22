package com.example.restaurantDBMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.core.RowMapper;

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
	
	public void addUser(User user) {
		jdbcTemplate.update("INSERT INTO Users(username, password, id, name, birthday, email, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[]
			{user.getUsername(), user.getPassword(), user.getId(), user.getName(), user.getBirthday(), user.getEmail(), user.getPhoneNumber()});
	}
	
	//Returns a list of all the employees in the database
	public List<Employee> getAllEmployees() {
		return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees", new EmployeeRowMapper());  
	}
	
	//Returns an Employee with the with the given username - null if doesn't exist
	public Employee searchEmployee(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees WHERE username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	public Employee searchRestaurantOwner(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees WHERE position = 'Owner' AND username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	public Customer searchCustomer(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Customers NATURAL JOIN CustomerCreditCards WHERE username = '" + username + "'", new CustomerRowMapper()));
	}
	
	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Customers NATURAL JOIN CustomerCreditCards", new CustomerRowMapper());
	}
	
	public List<MenuItem> getAllMenuItems() {
		return jdbcTemplate.query("SELECT * FROM MenuItems", new MenuItemRowMapper());
	}
	
	public void addCustomer(Customer cust) {
		jdbcTemplate.update("INSERT INTO CustomerCreditCards VALUES (?, ?, ?)", new Object[]
			{cust.getCreditCardNumber(), cust.getExpirationDate(), cust.getCvv()});
	}
	
	public void addEmployee(Employee employee) {
		//update information in users and employees relations
		jdbcTemplate.update("INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?)", employee.getUsername(),
				employee.getPassword(), employee.getId(), employee.getName(), employee.getBirthday(),
				employee.getEmail(), employee.getPhoneNumber());
		jdbcTemplate.update("INSERT INTO Employees VALUES (?, ?, ?)", employee.getUsername(),
				employee.getPosition(), employee.getSalary());
	}
	
	public int getMaxID(){
		return jdbcTemplate.queryForObject("SELECT MAX(id) FROM Users", Integer.class);
	}
	
	//---------------------------Modification-----------------------------------------------------------------
	
	public void updatePassword(String username, String newPassword) {
		jdbcTemplate.update("UPDATE Users SET password = ? WHERE username = ?", newPassword, username);
	}
	
	public void updateEmployeeEmailPhone(String username, String email, String phone) {
		jdbcTemplate.update("UPDATE Users SET email = ?, phoneNumber = ? WHERE username = ?", email, phone, username);
	}
	
	public void updateEmployeePosition(String username, String newPosition){
		jdbcTemplate.update("UPDATE Employees SET position = ? WHERE username = ? ", newPosition, username);
	}
	
	public void updateEmployeeSalary(String username, int salary){
		jdbcTemplate.update("UPDATE Employees SET salary = ? WHERE username = ? ", salary, username);
	}
	
	public void updateCustomerCC(String username, long oldCCNo, long newCCNo, String expDate, int cvv){
		jdbcTemplate.update("UPDATE Customers SET CreditCardNumber = ? WHERE username = ?", newCCNo, username);
		jdbcTemplate.update("UPDATE CustomerCreditCards SET CreditCardNumber = ?, ExpirationDate = ?, CVV = ? WHERE CreditCardNumber = ?", 
				newCCNo, expDate, cvv, oldCCNo);
		
	}
	
	//---------------------------Deletion-----------------------------------------------------//
	
	public void deleteUser(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username);
	}
	
	public void deleteEmployee(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username); //delete user info
		jdbcTemplate.update("DELETE FROM Employees WHERE username = ?", username); //delete employee info
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

}


