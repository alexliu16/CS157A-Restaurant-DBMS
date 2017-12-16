package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.employeeViews.*;
import com.example.customerViews.*;
import com.example.restaurantOwnerViews.*;
import com.vaadin.icons.VaadinIcons;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is initially displayed - will ask the user to sign in with their username/password
 * Also contains a link to the sign-up view
 * @author alexliu
 *
 */
public class LoginView extends VerticalLayout implements View{
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	//Layouts
	private VerticalLayout layout; //main layout 
	private FormLayout form; //layout that ask user to input username, password

	public LoginView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		layout = new VerticalLayout();
		form = new FormLayout();
		
		//set up 
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	    initializeLayouts();
	    initializePanel();
	}
	
	private void initializeLayouts() {
		//add header
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label header = new Label("Login to your Account");
		header.setStyleName(ValoTheme.LABEL_BOLD);
		header.addStyleName(ValoTheme.LABEL_LARGE);
		layout.addComponent(header);
		
		//form setup
		form.setMargin(true);
		
		//add username field
        TextField usernameField = new TextField("Username");
        usernameField.setIcon(VaadinIcons.USER);
        form.addComponent(usernameField);
        
        //add password label and field
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setIcon(VaadinIcons.KEY);
        form.addComponent(passwordField);
        
        //add button to login
        	Button loginButton = new Button("Login", new Button.ClickListener() {

        		@Override
        		public void buttonClick(ClickEvent event) {
        			String username = usernameField.getValue().trim();
        			String password = passwordField.getValue().trim();
        			//Determine the user based on username and password 
        			User user = null;
        			if((user = restaurantDAO.searchRestaurantOwner(username, password)) != null){ //user is restaurant owner
        				navigator.navigateTo("RestaurantOwnerMainView");
        				((RestaurantOwnerMainView) navigator.getCurrentView()).setEmployee((Employee)user);
        				((RestaurantOwnerMainView) navigator.getCurrentView()).displayInitialContent();
        			}	
        			else if ((user = restaurantDAO.searchEmployee(username, password)) != null){ //user is regular employee
        				navigator.navigateTo("EmployeeMainView");
        				((EmployeeMainView) navigator.getCurrentView()).setEmployee((Employee)user);
        				((EmployeeMainView) navigator.getCurrentView()).displayInitialContent();;
        			}	
        			else if((user = restaurantDAO.searchCustomer(username, password)) != null) { //user is customer
        				navigator.navigateTo("CustomerMainView");
        				((CustomerMainView) navigator.getCurrentView()).setCustomer((Customer)user);
        				((CustomerMainView) navigator.getCurrentView()).displayInitialContent();
        			}	
        			else  //no user with given username/password
        				Notification.show("Invalid username/pasword");
        			
        			
        			//reset fields
        			if(user != null) {
        				usernameField.setValue("");
        				passwordField.setValue("");
        			}
        				
        		}
        	});
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        form.addComponent(loginButton);
        
        layout.addComponent(form); //done with adding components to the form
        
        //Add a button to switch the view to the sign-up page
        Button registerButton = new Button("Don't have an account? Sign up here", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("SignUpView");
			}
		});
        registerButton.setStyleName(ValoTheme.BUTTON_LINK);
        
        layout.addComponent(registerButton);
        layout.setComponentAlignment(registerButton, Alignment.BOTTOM_CENTER);
	}
	
	//Creates the panel that will be displayed 
	private void initializePanel() {
		Panel signinPanel = new Panel(); 
		signinPanel.setWidth("30%");
		signinPanel.setContent(layout); 
		addComponent(signinPanel);
	}

}
