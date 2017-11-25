package com.example.restaurantDBMS;

public class MenuItem implements Comparable<MenuItem> {

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public int compareTo(MenuItem other){
		return name.compareTo(other.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuItem other = (MenuItem) obj;
		return this.name.equals(other.name) && type.equals(other.type) && description.equals(other.description);
	}
	
	
}
