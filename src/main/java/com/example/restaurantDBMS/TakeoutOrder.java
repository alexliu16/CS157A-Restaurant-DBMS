package com.example.restaurantDBMS;

/**
 * An online order that is placed by a customer
 * @author alexliu
 *
 */
public class TakeoutOrder extends Order {
	
	private String street;
	private String city;
	private String state;
	private int zip;

	public TakeoutOrder(int id, String time, String street, String city, String state, int zip, String status) {
		super(id, time, status);
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}
}
