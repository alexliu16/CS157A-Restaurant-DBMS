package com.example.restaurantDBMS;

/**
 * Represents an order placed by an employee for a table at the restaurant
 * @author alexliu
 *
 */
public class DineinOrder extends Order {
	
	private int tableNumber;

	public DineinOrder(int id, int tableNum, String time, String status) {
		super(id, time, status);
		tableNumber = tableNum;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

}
