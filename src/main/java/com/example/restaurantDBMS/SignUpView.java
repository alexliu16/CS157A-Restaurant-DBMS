package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
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
	private VerticalLayout layout;
	private Binder<Customer> binder;
	private Customer customer;

	public SignUpView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		layout = new VerticalLayout();
		binder = new Binder<Customer>();
		customer = new Customer("", "", "","","", "");
		
		//set up layout
	  	setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
	    setupForm();
	    setupButtons();
	    setupPanel();
	}
	
	private void setupPanel() {
	    Panel signupPanel = new Panel();
	    signupPanel.setWidth("30%");
	    signupPanel.setContent(layout);
	    addComponent(signupPanel);
	}

    //add fields that user has to fill in:
	private void setupForm() {
		// add header
		Label header = new Label("Sign Up");
		header.addStyleName(ValoTheme.LABEL_BOLD);
		header.addStyleName(ValoTheme.LABEL_LARGE);
		layout.addComponent(header);
		layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);

		// add input fields for first and last name
		Label nameLabel = new Label("Name");
		nameLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(nameLabel);
		
		TextField nameField = new TextField();
		nameField.setPlaceholder("Full name");
		nameField.setWidth("100%");
		layout.addComponent(nameField);
		
		//add username and password fields
		Label usernameLabel = new Label("Choose your username");
		usernameLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(usernameLabel);
		
		TextField usernameField = new TextField();
		usernameField.setWidth("100%");
		layout.addComponent(usernameField);
		
		Label passwordLabel = new Label("Create a password");
		passwordLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(passwordLabel);
		
		PasswordField passwordField = new PasswordField();
		passwordField.setWidth("100%");
		layout.addComponent(passwordField);
		
		//add birthday input
		Label birthdayLabel = new Label("Birthday");
		birthdayLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(birthdayLabel);	    
		
		TextField birthdayField = new TextField();
		birthdayField.setPlaceholder("MM-DD-YYYY");
		birthdayField.setWidth("50%");
		
		layout.addComponent(birthdayField);
		
		//add email input
		Label emailLabel = new Label("Email address");
		emailLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(emailLabel);	 
		
		TextField emailField = new TextField();
		emailField.setWidth("100%");
		layout.addComponent(emailField);
		
		//add phone input
		Label phoneLabel = new Label("Mobile phone");
		phoneLabel.setStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(phoneLabel);	 
		
		TextField phoneField = new TextField();
		phoneField.setPlaceholder("(DDD)-DDD-DDDD");
		phoneField.setWidth("100%");
		layout.addComponent(phoneField);	 
		
		//bind fields
		binder.forField(nameField).withValidator(name -> name != null && !name.isEmpty(), "This field cannot be left blank").bind(Customer::getName, Customer::setName);
		binder.forField(usernameField).withValidator(user -> user != null && !user.isEmpty(), "Please enter a username").withValidator(user -> restaurantDAO.searchUser(user) == null, "Username is already taken").bind(Customer::getUsername, Customer::setUsername);
		binder.forField(passwordField).withValidator(pass -> pass !=null && !pass.isEmpty(), "Please enter a password").bind(Customer::getPassword, Customer::setPassword);
		binder.forField(birthdayField).withValidator(birthday -> birthday.toString().matches("\\d{2}-\\d{2}-\\d{4}"), "Please enter a valid date").bind(Customer::getBirthday, Customer::setBirthday);
		binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid email address")).bind(Customer::getEmail, Customer::setEmail);
		binder.forField(phoneField).withValidator(phone -> phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"), "Phone number is of incorrect format").bind(Customer::getPhoneNumber, Customer::setPhoneNumber);
		
		binder.setBean(customer);
	}
	
	//add buttons: cancel button and sign-up button
	private void setupButtons() {
		
		Button nextButton = new Button("Continue", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) { //All fields are filled in and formatted correctly
					binder.setBean(customer);
					customer.setId(restaurantDAO.getMaxID() + 1);
					navigator.navigateTo("BillingInformationView");
					((BillingInformationView) navigator.getCurrentView()).setCustomer(customer);
					binder.readBean(null);
				}	
				else
					Notification.show("Not all fields are filled out correctly");
				
			}
			
		});
		nextButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		nextButton.setWidth("100%");
		layout.addComponent(nextButton);
		
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

}
