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
	
	//Returns an Employee with the position 'Owner' - null if username doesn't match
	public Employee searchRestaurantOwner(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN Employees WHERE position = 'Owner' AND username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	//Returns an Employee with the position 'Owner' - null if username doesn't match
	public List<Customer> getAllCustomers(String username) {
		return jdbcTemplate.query("SELECT a.username, c.password, c.id, c.name, c.birthday, c.email, c.phoneNumber, b.CreditCardNumber, b.ExpirationDate, b.CVV "
				+ "FROM Customers a JOIN CustomerCreditCards b on a.CreditCardNumber = b.CreditCardNumber "
				+ "JOIN Users c on a.username = c.username "
				+ "WHERE a.username = '" + username + "'", new CustomerRowMapper());
	}
	
	//Returns an Employee with the position 'Owner' - null if username doesn't match
	public Customer searchCustomer(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT a.username, c.password, c.id, c.name, c.birthday, c.email, c.phoneNumber, b.CreditCardNumber, b.ExpirationDate, b.CVV "
				+ "FROM Customers a JOIN CustomerCreditCards b on a.CreditCardNumber = b.CreditCardNumber "
				+ "JOIN Users c on a.username = c.username "
				+ "WHERE a.username = '" + username + "'", new CustomerRowMapper()));
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
	
	//update
	
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
	
	//deletion
	
	public void deleteUser(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username);
	}
	
	public void deleteEmployee(String username){
		jdbcTemplate.update("DELETE FROM Users WHERE username = ?", username); //delete user info
		jdbcTemplate.update("DELETE FROM Employees WHERE username = ?", username); //delete employee info
	}
	
	//Inner classes:
	
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
		        return new Customer(rs.getString(1), rs.getString(2), rs.getInt(3),
		        		rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getLong(8), rs.getString(9), rs.getInt(10));
		    }  
	}

}


