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
		return DataAccessUtils.singleResult(jdbcTemplate.query("select * from Users where username = '" + username + "'", new UserRowMapper()));
	}
	
	public void addUser(User user) {
		jdbcTemplate.update("INSERT INTO Users(username, password, id, name, birthday, email, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[]
			{user.getUsername(), user.getPassword(), user.getId(), user.getName(), user.getBirthday(), user.getEmail(), user.getPhoneNumber()});
	}
	
	//Returns a list of all the employees in the database
	public List<Employee> getAllEmployees() {
		return jdbcTemplate.query("SELECT a.username, c.password, c.id, c.name, c.birthday, c.email, c.phoneNumber, b.position, b.salary "
				+ "FROM Employees a JOIN EmployeeSalaries b on a.position = b.position JOIN Users c on a.username = c.username", new EmployeeRowMapper());  
	}
	
	//Returns an Employee with the with the given username - null if doesn't exist
	public Employee searchEmployee(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT a.username, c.password, c.id, c.name, c.birthday, c.email, c.phoneNumber, b.position, b.salary "
				+ "FROM Employees a JOIN EmployeeSalaries b on a.position = b.position "
				+ "JOIN Users c on a.username = c.username WHERE a.username = '" + username + "'", new EmployeeRowMapper()));
	}
	
	//Returns an Employee with the position 'Owner' - null if username doesn't match
	public Employee searchRestaurantOwner(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT a.username, c.password, c.id, c.name, c.birthday, c.email, c.phoneNumber, b.position, b.salary "
				+ "FROM Employees a JOIN EmployeeSalaries b on a.position = b.position "
				+ "JOIN Users c on a.username = c.username "
				+ "WHERE b.position = 'Owner' AND a.username = '" + username + "'", new EmployeeRowMapper()));
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
	
	public int getMaxID(){
		return jdbcTemplate.queryForObject("SELECT MAX(id) FROM Users", Integer.class);
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


