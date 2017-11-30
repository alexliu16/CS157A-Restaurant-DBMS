package com.example.employeeViews;

import com.example.restaurantDBMS.Employee;
import com.example.restaurantDBMS.RestaurantDAO;
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

/**
 * View that is displayed when an employee wants to edit their profile
 * @author alexliu
 *
 */
public class EmployeeEditProfileView extends EmployeeMainView{
	
private Binder<Employee> binder;
	
	public EmployeeEditProfileView(Navigator navigate, RestaurantDAO dao){
		super(navigate, dao);
		binder = new Binder<>();
		
	}
	
	//check override
	@Override
	public void displayInitialContent(){
		VerticalLayout layout = getContent(); //layout to hold panel
		layout.removeAllComponents();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label headerLabel = new Label("My User Profile");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		layout.addComponent(headerLabel);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(new Label("Enter your information and click Submit to update your profile."));
		
		//Set up fields
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
		
		contentLayout.addComponents(new Label("Name"), nameField,
				new Label("Birthday"), birthdayField,
				new Label("Email Address"), emailField,
				new Label("Phone Number"), phoneField);
		
		//bind fields
		binder.forField(nameField).bind(Employee::getName, Employee::setName);
		binder.forField(birthdayField).bind(Employee::getBirthday, Employee::setBirthday);
		binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid email address"))
				.bind(Employee::getEmail, Employee::setEmail);
		binder.forField(phoneField).withValidator(phone -> phone.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"),
				"Phone number is of incorrect format").bind(Employee::getPhoneNumber, Employee::setPhoneNumber);
		
		Employee currentEmployee = getEmployee();
		binder.readBean(currentEmployee);
		binder.setBean(currentEmployee);
		
		//set up button
		Button submitButton = new Button("Submit", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) { //All fields are filled in and formatted correctly
					binder.setBean(currentEmployee);
					getRestaurantDAO().updateEmployeeEmailPhone(currentEmployee.getUsername(), currentEmployee.getEmail(), currentEmployee.getPhoneNumber());
					Notification.show("You have sucessfully updated your profile.");
				}	
				else
					Notification.show("Not all fields are filled out correctly");
				
			}
			
		});
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		contentLayout.addComponent(submitButton);
		contentLayout.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		//Set up panel
		Panel contentPanel = new Panel();
		contentPanel.setContent(contentLayout);
		contentPanel.setWidth("60%");
		layout.addComponent(contentPanel);
	}
}
