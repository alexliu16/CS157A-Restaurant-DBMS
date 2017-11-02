package com.example.restaurantDBMS;

public class Employee extends User{
	private String position;
	private int salary;
	
	public Employee(String user, String pass, int idNo, String fullName, String birthdate, String addr, String phone, String pos, int sal) {
		super(user, pass, idNo, fullName, birthdate, addr, phone);
		position = pos;
		salary = sal;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
}
