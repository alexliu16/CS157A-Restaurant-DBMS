package com.example.restaurantDBMS;

public class User {
	
	private String username,
	password;
	private int id;
	private String name,
	birthday,
	address,
	phoneNumber;

	public User(String user, String pass, int idNo, String fullName, String birthdate, String addr, String phone){
		username = user;
		password = pass;
		id = idNo;
		name = fullName;
		birthday = birthdate;
		address = addr;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", id=" + id + ", name=" + name + ", birthday="
				+ birthday + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
	}
}
