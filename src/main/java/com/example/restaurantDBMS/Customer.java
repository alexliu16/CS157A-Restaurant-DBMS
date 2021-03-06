package com.example.restaurantDBMS;

/**
 * Customer of the restaurant - has billing information
 * @author alexliu
 *
 */
public class Customer extends User {
	
	private long creditCardNumber;
	private String expirationDate;
	private int cvv;
	
	public Customer(String user, String pass,String fullName, String birthdate, String addr, String phone) {
		super(user, pass, fullName, birthdate, addr, phone);
		creditCardNumber = -1;
		expirationDate = "";
		cvv = -1;
	}
	
	public Customer(String user, String pass, int idNo, String fullName, String birthdate, String addr, String phone) {
		super(user, pass, idNo, fullName, birthdate, addr, phone);
		creditCardNumber = -1;
		expirationDate = "";
		cvv = -1;
	}
	
	public Customer(String user, String pass, int idNo, String fullName, String birthdate, String addr, String phone, long ccNo, String expDate, int secCVV) {
		super(user, pass, idNo, fullName, birthdate, addr, phone);
		creditCardNumber = ccNo;
		expirationDate = expDate;
		cvv = secCVV;
	}

	public long getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(long creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}	
}
