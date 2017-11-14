package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.customerViews.*;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class BillingInformationView extends VerticalLayout implements View {

	@Autowired
	private RestaurantDAO restaurantDAO;
	
	private Navigator navigator;
	private VerticalLayout layout;
	private Binder<Customer> binder;
	private Customer customer;

	public BillingInformationView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		layout = new VerticalLayout();
		binder = new Binder<Customer>();
		customer = new Customer("", "", "","","", "");
		
		//set up layout
	  	setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
	    setupForm();
	    setupPanel();
	}
	
	private void setupForm() {
		// add header
		Label header = new Label("Billing Information");
		header.addStyleName(ValoTheme.LABEL_BOLD);
		header.addStyleName(ValoTheme.LABEL_LARGE);
		layout.addComponent(header);
		layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
		
		//add credit card fields
		Label ccLabel = new Label("Credit Card Information");
		layout.addComponent(ccLabel);
		
		TextField ccNoField = new TextField();
		ccNoField.setPlaceholder("Enter your credit card number");
		ccNoField.setWidth("100%");
		layout.addComponent(ccNoField);
		
		TextField expDateField = new TextField();
		expDateField.setPlaceholder("MM/YY");
		expDateField.setWidth("50%");
		
		TextField cvvField = new TextField();
		cvvField.setPlaceholder("CVV");
		cvvField.setWidth("50%");
		
		HorizontalLayout hLayout = new HorizontalLayout(expDateField, cvvField);
		layout.addComponent(hLayout);
		
		//add validators/bindings
		binder.forField(ccNoField).withValidator(ccNo -> ccNo.matches("\\d{16}"), "Invalid credit card number").withConverter(new StringToLongConverter("")).bind(Customer::getCreditCardNumber, Customer::setCreditCardNumber);
		binder.forField(expDateField).withValidator(expDate -> expDate.matches("(0[1-9]|1[0-2])\\/[0-9]{2}"), "Invalid expiration date").bind(Customer::getExpirationDate, Customer::setExpirationDate);
		binder.forField(cvvField).withValidator(cvv -> cvv.matches("\\d{3}"), "Invalid CVV number").withConverter(new StringToIntegerConverter("")).bind(Customer::getCvv, Customer::setCvv);
		
		//add buttons
		Button finishButton = new Button("Finish", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) { //All fields are filled in and formatted correctly
					//update customer information
					customer.setCreditCardNumber(Long.parseLong(ccNoField.getValue()));
					customer.setExpirationDate(expDateField.getValue());
					customer.setCvv(Integer.parseInt(cvvField.getValue()));
					
					//add information to database
					restaurantDAO.addUser(customer);
					restaurantDAO.addCustomer(customer);
					
					//change view
					navigator.navigateTo("CustomerMainView");
					((CustomerMainView)navigator.getCurrentView()).setCustomer(customer);
				}	
				else
					Notification.show("Not all fields are filled out correctly");
			}
			
		});
		finishButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		finishButton.setWidth("100%");
		layout.addComponent(finishButton);
		
		//cancel button - changes view to LoginView
		Button cancelButton = new Button("Already have an account? Sign in", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("LoginView");
			}
		});
		cancelButton.setStyleName(ValoTheme.BUTTON_LINK);
		layout.addComponent(cancelButton);
	    layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
	}
	
	private void setupPanel() {
		Panel signupPanel = new Panel();
		signupPanel.setWidth("30%");
		signupPanel.setContent(layout);
		addComponent(signupPanel);
	}
	
	public void setCustomer(Customer cust) {
		customer = cust;
	}
}
