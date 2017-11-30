package com.example.restaurantDBMS;

/**
 * An online order that is placed by a customer
 * @author alexliu
 *
 */
public class TakeoutOrder extends Order {
	
	private String street;
	private String city;
	
	public TakeoutOrder() {
		super(0, "", "");
		street = "";
		city = "";
	}

	public TakeoutOrder(int id, String time, String street, String city, String status) {
		super(id, time, status);
		this.street = street;
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
