package com.example.customerViews;

import com.example.restaurantDBMS.*;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerEditProfileView extends CustomerMainView {
	
	private Binder<Customer> binder;

	public CustomerEditProfileView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		binder = new Binder<>();

	}

	@Override
	public void displayInitialContent() {
		VerticalLayout layout = getContent(); // layout to hold panel
		layout.removeAllComponents();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		Label headerLabel = new Label("My User Profile");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1, ValoTheme.LABEL_BOLD);
		layout.addComponent(headerLabel);

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(new Label("Enter your information and click Submit to update your profile."));

		// Set up fields
		TextField nameField = new TextField();
		nameField.setWidth("100%");
		nameField.setReadOnly(true);

		TextField birthdayField = new TextField();
		birthdayField.setWidth("100%");
		birthdayField.setReadOnly(true);

		TextField emailField = new TextField();
		emailField.setWidth("100%");

		TextField phoneField = new TextField();
		phoneField.setPlaceholder("(DDD)-DDD-DDDD");
		phoneField.setWidth("100%");

		contentLayout.addComponents(new Label("Name"), nameField, new Label("Birthday"), birthdayField,
				new Label("Email Address"), emailField, new Label("Phone Number"), phoneField);

		// bind fields
		binder.forField(nameField).bind(Customer::getName, Customer::setName);
		binder.forField(birthdayField).bind(Customer::getBirthday, Customer::setBirthday);
		binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid email address"))
				.bind(Customer::getEmail, Customer::setEmail);
		binder.forField(phoneField).withValidator(phone -> phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"),
				"Phone number is of incorrect format").bind(Customer::getPhoneNumber, Customer::setPhoneNumber);

		Customer currentCustomer = getCustomer();
		binder.readBean(currentCustomer);
		binder.setBean(currentCustomer);

		// set up button
		Button submitButton = new Button("Submit", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (binder.validate().isOk()) { // All fields are filled in and formatted correctly
					binder.setBean(currentCustomer);
					getRestaurantDAO().updateEmployeeEmailPhone(currentCustomer.getUsername(),
							currentCustomer.getEmail(), currentCustomer.getPhoneNumber());
					Notification.show("You have sucessfully updated your profile.");
				} else
					Notification.show("Not all fields are filled out correctly");

			}

		});
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		contentLayout.addComponent(submitButton);
		contentLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);

		// Set up panel
		Panel contentPanel = new Panel();
		contentPanel.setContent(contentLayout);
		contentPanel.setWidth("60%");
		layout.addComponent(contentPanel);
	}

}
