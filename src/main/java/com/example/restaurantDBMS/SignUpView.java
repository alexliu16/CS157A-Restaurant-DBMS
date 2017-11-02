package com.example.restaurantDBMS;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is displayed when a customer wants to create an account
 * @author alexliu
 *
 */
public class SignUpView extends VerticalLayout implements View {
	
	@Autowired
	private RestaurantDAO restaurantDAO;
	
	private Navigator navigator;
	private FormLayout form;

	public SignUpView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		form = new FormLayout();
		form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//set up layout
	  	setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//add header
		Label header = new Label("Create a New Customer Account"); 
	    header.addStyleName(ValoTheme.LABEL_H1);
	    addComponent(header);
	   
	    setupTextFields();
	    setupButtons();
	    
	    //add login panel
	    Panel signupPanel = new Panel();
	    signupPanel.setWidth("30%");
	    form.setMargin(true);
	    signupPanel.setContent(form);
	    signupPanel.addStyleName("test");
	    addComponent(signupPanel);
	  
	}

    //add fields that user has to fill in:
	private void setupTextFields() {
		//name field
	    TextField nameField = new TextField("Name");
	    nameField.setIcon(VaadinIcons.CALENDAR_USER);
	    nameField.setRequiredIndicatorVisible(true);
	    form.addComponent(nameField);
	    
	    //username field
	    TextField usernameField = new TextField("Username");
	    usernameField.setIcon(VaadinIcons.USER);
	    usernameField.setRequiredIndicatorVisible(true);
	    form.addComponent(usernameField);
	    
	    //password field
	    TextField passwordField = new TextField("Password");
	    passwordField.setIcon(VaadinIcons.KEY);
	    passwordField.setRequiredIndicatorVisible(true);
	    form.addComponent(passwordField);
	    
	    //address field
	    TextField addressField = new TextField("Address");
	    addressField.setIcon(VaadinIcons.ROAD);
	    form.addComponent(addressField);
	    
	    //birthday field
	    DateField birthdayField = new DateField("Birthday");
	    birthdayField.setValue(LocalDate.now());
	    birthdayField.setIcon(VaadinIcons.CALENDAR);
	    form.addComponent(birthdayField);
	    
	    //email field
	    TextField emailField = new TextField("Email Address");
	    emailField.setIcon(VaadinIcons.MAILBOX);
	    form.addComponent(emailField);
	    
	}
	
	//add buttons: cancel button and sign-up button
	private void setupButtons() {
		HorizontalLayout layout = new HorizontalLayout(); //layout to hold buttons
		 //cancel button - changes view to LoginView
		Button cancelButton = new Button("Cancel", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("LoginView");
			}
		});
		
		Button signUpButton = new Button("Sign Up", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//TODO: Add button functionality
			}
		});
		signUpButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		layout.addComponent(cancelButton);
		layout.addComponent(signUpButton);
		//addComponent(layout);
		
		form.addComponent(signUpButton);
	}
}
