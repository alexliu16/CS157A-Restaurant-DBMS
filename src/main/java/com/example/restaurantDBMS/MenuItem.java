package com.example.restaurantDBMS;

public class MenuItem {

	private String name;
	private String type;
	private String description;
	private float price;
	
	public MenuItem(String na, String ty, String des, float pri){
		name = na;
		type = ty;
		description = des;
		price = pri;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getType() {
		return type;
	}
	
	public float getPrice(){
		return price;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public void setType(String newType) {
		type = newType;
	}
	
	public void setDescription(String newDescription) {
		description = newDescription;
	}
	
	public void setPrice(Float newPrice) {
		price = newPrice;
	}
}
