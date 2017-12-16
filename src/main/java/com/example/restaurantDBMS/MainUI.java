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
 */

@SpringUI
public class MainUI extends UI {

	@Autowired
	private RestaurantDAO restaurantDAO; //DAO to MySQL database
	
	private Navigator navigator; //navigator to navigate between the views

	@Override
	protected void init(VaadinRequest request) {
		//Create a navigator to control the views
		navigator = new Navigator(this, this);
		
		//Create and register the views: 
		
		//Login/Signup Views
		navigator.addView("LoginView", new LoginView(navigator, restaurantDAO));
		navigator.addView("SignUpView", new SignUpView(navigator, restaurantDAO));
		navigator.addView("BillingInformationView", new BillingInformationView(navigator, restaurantDAO));
		
		//Customer views
		navigator.addView("CustomerMainView", new CustomerMainView(navigator, restaurantDAO));
		navigator.addView("CustomerEditProfileView", new CustomerEditProfileView(navigator, restaurantDAO));
		navigator.addView("CustomerChangePasswordView", new CustomerChangePasswordView(navigator, restaurantDAO));
		navigator.addView("CustomerEditBillingView", new CustomerEditBillingView(navigator, restaurantDAO));
		navigator.addView("CustomerMenuView", new CustomerMenuView(navigator, restaurantDAO));
		navigator.addView("CustomerPlaceOrderView", new CustomerPlaceOrderView(navigator, restaurantDAO));
		navigator.addView("CustomerConfirmOrderView", new CustomerConfirmOrderView(navigator, restaurantDAO));
		
		//Employee views
		navigator.addView("EmployeeMainView", new EmployeeMainView(navigator, restaurantDAO));
		navigator.addView("EmployeeEditProfileView", new EmployeeEditProfileView(navigator, restaurantDAO));
		navigator.addView("EmployeeChangePasswordView", new EmployeeChangePasswordView(navigator, restaurantDAO));
		navigator.addView("EmployeePlaceOrderView", new EmployeePlaceOrderView(navigator, restaurantDAO));
		navigator.addView("EmployeeOrdersView", new EmployeeOrdersView(navigator, restaurantDAO));
		navigator.addView("SingleOrderView", new SingleOrderView(navigator, restaurantDAO));
		
		//Restaurant owner views
		navigator.addView("RestaurantOwnerMainView", new RestaurantOwnerMainView(navigator, restaurantDAO));
		navigator.addView("OwnerEditProfileView", new OwnerEditProfileView(navigator, restaurantDAO));
		navigator.addView("OwnerChangePasswordView", new OwnerChangePasswordView(navigator, restaurantDAO));
		navigator.addView("AllEmployeesView", new AllEmployeesView(navigator, restaurantDAO));
		navigator.addView("HireEmployeeView", new HireEmployeeView(navigator, restaurantDAO));
		navigator.addView("OwnerMenuView", new OwnerMenuView(navigator, restaurantDAO));
		
		//Display initial view
		navigator.navigateTo("LoginView");
	}
	
	public void updateMenuViews() {
		navigator.navigateTo("CustomerMenuView");
		((CustomerMenuView)navigator.getCurrentView()).redraw();
		navigator.navigateTo("CustomerPlaceOrderView");
		((CustomerPlaceOrderView)navigator.getCurrentView()).redraw();
		navigator.navigateTo("EmployeePlaceOrderView");
		((EmployeePlaceOrderView)navigator.getCurrentView()).redraw();
		navigator.navigateTo("OwnerMenuView");
	}
}
