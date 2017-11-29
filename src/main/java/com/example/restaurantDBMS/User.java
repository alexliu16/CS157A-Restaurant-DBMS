package com.example.restaurantDBMS;

/**
 * A user of the Restaurant DBMS - either an employee or a customer
 * @author alexliu
 *
 */
public class User {
	
	private String username, 
	password;
	private int id;
	private String name, 
	birthday, 
	email,
	phoneNumber;
	
	public User(String user, String pass, String fullName, String birthdate, String em, String phone){
		username = user;
		password = pass;
		id = -1;
		name = fullName;
		birthday = birthdate;
		email = em;
		phoneNumber = phone;
	}

	public User(String user, String pass, int idNo, String fullName, String birthdate, String em, String phone){
		username = user;
		password = pass;
		id = idNo;
		name = fullName;
		birthday = birthdate;
		email = em;
		phoneNumber = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String em) {
		email = em;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
