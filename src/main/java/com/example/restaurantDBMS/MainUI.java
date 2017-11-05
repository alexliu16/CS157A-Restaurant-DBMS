package com.example.restaurantDBMS;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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
		
		//Create and register the views:
		navigator.addView("LoginView", new LoginView(navigator, restaurantDAO));
		navigator.addView("AllEmployeesView", new AllEmployeesView(navigator, restaurantDAO));
		navigator.addView("SignUpView", new SignUpView(navigator, restaurantDAO));
		navigator.addView("BillingInformationView", new BillingInformationView(navigator, restaurantDAO));
		navigator.addView("CustomerMainView", new CustomerMainView(navigator, restaurantDAO));
		navigator.addView("EmployeeMainView", new EmployeeMainView(navigator, restaurantDAO));
		navigator.addView("RestaurantOwnerMainView", new RestaurantOwnerMainView(navigator, restaurantDAO));
		navigator.navigateTo("LoginView");
		
	}

}
