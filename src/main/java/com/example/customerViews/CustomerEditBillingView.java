package com.example.customerViews;

import java.text.NumberFormat;
import java.util.Locale;

import com.example.restaurantDBMS.Customer;
import com.example.restaurantDBMS.RestaurantDAO;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.navigator.Navigator;
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

/**
 * View that is displayed when a customer wants to edit their billing information
 * @author alexliu
 *
 */
public class CustomerEditBillingView extends CustomerMainView{
	
	private long oldCCNo;
	private Binder<Customer> binder;
	
	public CustomerEditBillingView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		binder = new Binder<>();
		setupContent();	
	}
	
	private void setupContent() {
		VerticalLayout content = getContent();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Add header 
		Label headerLabel = new Label("Edit Billing Information");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		content.addComponent(headerLabel);
		
		//Create panel
		Panel billingPanel = new Panel();
		VerticalLayout panelContent = new VerticalLayout();
		
		//add credit card fields
		Label ccLabel = new Label("Credit Card Information");
		panelContent.addComponent(ccLabel);
		
		TextField ccNoField = new TextField();
		ccNoField.setPlaceholder("Enter your credit card number");
		ccNoField.setWidth("100%");
		panelContent.addComponent(ccNoField);
		
		TextField expDateField = new TextField();
		expDateField.setPlaceholder("MM/YY");
		
		TextField cvvField = new TextField();
		cvvField.setPlaceholder("CVV");
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		hLayout.addComponents(expDateField, cvvField);
		panelContent.addComponent(hLayout);
		
		//add validators/bindings
		binder.forField(ccNoField).withValidator(ccNo -> ccNo.matches("\\d{16}"), "Invalid credit card number").withConverter(new StringToLongConverter("") {
			
			@Override
			protected java.text.NumberFormat getFormat(Locale locale) {
		        NumberFormat format = super.getFormat(locale);
		        format.setGroupingUsed(false);
		        return format;
		    };
		    
		}).bind(Customer::getCreditCardNumber, Customer::setCreditCardNumber);
		
		binder.forField(expDateField).withValidator(expDate -> expDate.matches("(0[1-9]|1[0-2])\\/[0-9]{2}"), "Invalid expiration date").bind(Customer::getExpirationDate, Customer::setExpirationDate);
		binder.forField(cvvField).withValidator(cvv -> cvv.matches("\\d{3}"), "Invalid CVV number").withConverter(new StringToIntegerConverter("")).bind(Customer::getCvv, Customer::setCvv);
		
		binder.setBean(getCustomer());
		
		Button applyButton = new Button("Apply");
		applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		applyButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) { //All fields are filled in and formatted correctly
					//update customer information
					Notification.show("Successfully updated billing information");
					Customer cust = getCustomer();
					getRestaurantDAO().updateCustomerCC(cust.getUsername(), oldCCNo, Long.parseLong(ccNoField.getValue()), expDateField.getValue(), Integer.parseInt(cvvField.getValue()));
					oldCCNo = Long.parseLong(ccNoField.getValue());
				}	
				else
					Notification.show("Not all fields are filled out correctly");
			}
		});
		
		panelContent.addComponent(applyButton);
		panelContent.setComponentAlignment(applyButton, Alignment.MIDDLE_CENTER);
		
		billingPanel.setWidth("50%");
		billingPanel.setContent(panelContent);
		content.addComponent(billingPanel);
		
	}
	
	public void updateContent(){
		binder.readBean(getCustomer());
		oldCCNo = getCustomer().getCreditCardNumber();
	}
}
