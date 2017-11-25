package com.example.restaurantDBMS;

public class Order {
	
	private int orderID;
	private String timeOfOrder;
	
	public Order(int id, String time) {
		orderID = id;
		timeOfOrder = time;
	}
	
	public int getOrderID() {
		return orderID;
	}
	
	public void setOrderID(int newID) {
		orderID = newID;
	}
	
	public String getTimeOfOrder() {
		return timeOfOrder;
	}
	
	public void setTimeOfOrder(String time) {
		timeOfOrder = time;
	}
}
