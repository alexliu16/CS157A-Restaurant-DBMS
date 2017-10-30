package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Frame that displays a grid of all the employees and their information
 * @author alexliu
 *
 */
public class LoginView extends VerticalLayout implements View{
	
	@Autowired
	private UserDAO userDAO;

	// Component declarations
	private Navigator navigator;
	private TextField usernameField;
	private TextField passwordField;

	public LoginView(Navigator navigate, UserDAO uDAO) {
		userDAO = uDAO;
		navigator = navigate;
		//set up layout
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//initialize components
		usernameField = new TextField();
		passwordField = new TextField();
		
		//add header
		Label header = new Label("Login To Your Account");
	    header.addStyleName(ValoTheme.LABEL_H1);
	    addComponentAsFirst(header);
	    
	    //add username label and field
	    HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setWidth("30%");
        usernameLayout.addComponent(new Label("Username"));
        usernameLayout.addComponentsAndExpand(usernameField);
        addComponent(usernameLayout);
        
        //add password label and field
	    HorizontalLayout passwordLayout = new HorizontalLayout();
        passwordLayout.setWidth("30%");
        passwordLayout.addComponent(new Label("Password"));
        passwordLayout.addComponentsAndExpand(passwordField);
	    addComponent(passwordLayout);
	    
	    //add login button
		Button loginButton = new Button("Login", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String username = usernameField.getValue().trim();
				String password = passwordField.getValue().trim();
				//Search if there are any users with the given username and password
				User user = userDAO.searchUsers(username);
				if (user == null || !user.getPassword().equals(password))
					Notification.show("Incorrect username/password");
				else
					navigator.navigateTo("AllEmployeesView");
			}
		});
	    loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
	    addComponent(loginButton);
	}

}
