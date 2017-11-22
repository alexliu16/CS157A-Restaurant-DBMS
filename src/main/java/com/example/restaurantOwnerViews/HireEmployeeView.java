package com.example.restaurantOwnerViews;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.restaurantDBMS.*;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
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

public class HireEmployeeView extends RestaurantOwnerMainView {

	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	private Binder<Employee> binder;

	public HireEmployeeView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		navigator = navigate;
		restaurantDAO = rDAO;
		binder = new Binder<>();
	}

	@Override
	public void displayInitialContent() {
		VerticalLayout contentLayout = getContent();
		contentLayout.removeAllComponents();
		
		VerticalLayout layout = new VerticalLayout();
		
		// add header
		Label header = new Label("Hire New Employee");
		header.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_LARGE);
		layout.addComponent(header);
		layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER);

		// add input fields for first and last name
		Label nameLabel = new Label("Name");
		nameLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField nameField = new TextField();
		nameField.setPlaceholder("Full name");
		nameField.setWidth("100%");
		layout.addComponents(nameLabel, nameField);

		// add username and password fields
		Label usernameLabel = new Label("Choose a username");
		usernameLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField usernameField = new TextField();
		usernameField.setWidth("100%");

		Label passwordLabel = new Label("Create a default password");
		passwordLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField passwordField = new TextField();
		passwordField.setWidth("100%");
		layout.addComponents(usernameLabel, usernameField, passwordLabel, passwordField);

		// add birthday input
		Label birthdayLabel = new Label("Employee birthday");
		birthdayLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField birthdayField = new TextField();
		birthdayField.setPlaceholder("MM-DD-YYYY");
		birthdayField.setWidth("50%");
		layout.addComponents(birthdayLabel, birthdayField);

		// add email input
		Label emailLabel = new Label("Employee email address");
		emailLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField emailField = new TextField();
		emailField.setWidth("100%");
		layout.addComponents(emailLabel, emailField);

		// add phone input
		Label phoneLabel = new Label("Employee mobile phone number");
		phoneLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField phoneField = new TextField();
		phoneField.setPlaceholder("(DDD)-DDD-DDDD");
		phoneField.setWidth("100%");
		layout.addComponents(phoneLabel, phoneField);

		// add input for salary and position
		Label jobLabel = new Label("Enter job information");
		jobLabel.setStyleName(ValoTheme.LABEL_BOLD);

		TextField positionField = new TextField();
		positionField.setPlaceholder("Position");
		positionField.setWidth("50%");
		TextField salaryField = new TextField();
		salaryField.setPlaceholder("Salary");
		salaryField.setWidth("50%");
		HorizontalLayout jobLayout = new HorizontalLayout(positionField, salaryField);
		layout.addComponents(jobLabel, jobLayout);

		// bind fields
		Employee newEmployee = new Employee("", "", -1, "", "", "", "", "", -1);
		binder.forField(nameField)
				.withValidator(name -> name != null && !name.isEmpty(), "This field cannot be left blank")
				.bind(Employee::getName, Employee::setName);
		binder.forField(usernameField).withValidator(user -> user != null && !user.isEmpty(), "Please enter a username")
				.withValidator(user -> restaurantDAO.searchUser(user) == null, "Username is already taken")
				.bind(Employee::getUsername, Employee::setUsername);
		binder.forField(passwordField).withValidator(pass -> pass != null && !pass.isEmpty(), "Please enter a password")
				.bind(Employee::getPassword, Employee::setPassword);
		binder.forField(birthdayField).withValidator(birthday -> birthday.toString().matches("\\d{2}-\\d{2}-\\d{4}"),
				"Please enter a valid date").bind(Employee::getBirthday, Employee::setBirthday);
		binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid email address"))
				.bind(Employee::getEmail, Employee::setEmail);
		binder.forField(phoneField).withValidator(phone -> phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"),
				"Phone number is of incorrect format").bind(Employee::getPhoneNumber, Employee::setPhoneNumber);
		binder.forField(positionField).withValidator(pos -> pos != null && !pos.isEmpty(), "Please enter a position")
				.bind(Employee::getPosition, Employee::setPosition);
		binder.forField(salaryField).withValidator(salary -> salary.matches("[0-9,]+"), "Salary is not a valid number")
				.withConverter(new StringToIntegerConverter("New Salary"))
				.bind(Employee::getSalary, Employee::setSalary);

		binder.setBean(newEmployee);
		salaryField.setValue("");

		// add confirmation button
		Button hireButton = new Button("Hire New Employee", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) { //All fields are filled in and formatted correctly
					binder.setBean(newEmployee);
					newEmployee.setId(restaurantDAO.getMaxID() + 1);
					restaurantDAO.addEmployee(newEmployee);
					binder.readBean(null);
					Notification.show("Success! You have hired a new employee.");
				}	
				else
					//Notification.show("Not all fields are filled out correctly");
					Notification.show(Arrays.toString(binder.validate().getValidationErrors().toArray()));
			}
			
		});
		hireButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		layout.addComponent(hireButton);
		layout.setComponentAlignment(hireButton, Alignment.MIDDLE_CENTER);
		
		Panel hireEmployeePanel = new Panel();
		hireEmployeePanel.setWidth("50%");
		hireEmployeePanel.setContent(layout);
		
		contentLayout.addComponent(hireEmployeePanel);
		contentLayout.setComponentAlignment(hireEmployeePanel, Alignment.MIDDLE_CENTER);
	}
}
