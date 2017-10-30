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
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserDAO() {
		jdbcTemplate = new JdbcTemplate();
	}

	//Returns a list of all the users in the database
	public List<User> getAllUsers() {
		return jdbcTemplate.query("select * from Users",new userRowMapper());  
	}

	//Returns a User with the with the givecn username - null if doesn't exist
	public User searchUsers(String username) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("select * from Users where username = '" + username + "'", new userRowMapper()));
	}
	
	private class userRowMapper implements RowMapper<User>{
		
		 @Override  
		    public User mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        return new User(rs.getString(1), rs.getString(2), rs.getInt(3),
		        		rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
		    }  
	}

}


