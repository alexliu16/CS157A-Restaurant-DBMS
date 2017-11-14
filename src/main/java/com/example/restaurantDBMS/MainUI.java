package com.example.restaurantDBMS;

import com.example.customerViews.*;
import com.example.employeeViews.*;
import com.example.restaurantOwnerViews.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Main user interface for the Restaurant DBMS
 * @author alexliu
 *
 */

@SpringUI
public class MainUI extends UI {

	@Autowired
	private RestaurantDAO restaurantDAO;
	
	private Navigator navigator;
	//private VerticalLayout layout;

	@Override
	protected void init(VaadinRequest request) {
		//Create a navigator to control the views
		navigator = new Navigator(this, this);
		
		/*Create and register the views: 
		 * Pass in DAO and navigator to each view
		*/
		
		//Login/Signup Views
		navigator.addView("LoginView", new LoginView(navigator, restaurantDAO));
		navigator.addView("SignUpView", new SignUpView(navigator, restaurantDAO));
		navigator.addView("BillingInformationView", new BillingInformationView(navigator, restaurantDAO));
		
		//Customer views
		navigator.addView("CustomerMainView", new CustomerMainView(navigator, restaurantDAO));
		
		//Employee views
		navigator.addView("EmployeeMainView", new EmployeeMainView(navigator, restaurantDAO));
		
		//Restaurant owner views
		navigator.addView("RestaurantOwnerMainView", new RestaurantOwnerMainView(navigator, restaurantDAO));
		navigator.addView("OwnerEditProfileView", new OwnerEditProfileView(navigator, restaurantDAO));
		navigator.addView("OwnerChangePasswordView", new OwnerChangePasswordView(navigator, restaurantDAO));
		navigator.addView("AllEmployeesView", new AllEmployeesView(navigator, restaurantDAO));
		navigator.addView("HireEmployeeView", new HireEmployeeView(navigator, restaurantDAO));
		
		//Display initial view
		navigator.navigateTo("LoginView");
	}

}
