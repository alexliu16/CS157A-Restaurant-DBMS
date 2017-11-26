package com.example.restaurantDBMS;

public class Order {
	
	private int orderID;
	private String timeOfOrder;
	private String orderStatus;
	
	public Order(int id, String time, String status) {
		orderID = id;
		timeOfOrder = time;
		orderStatus = status;
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
	
	public String getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(String status) {
		orderStatus = status;
	}
}
