package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is displayed when a customer first logs in 
 * @author alexliu
 *
 */
public class CustomerMainView extends VerticalLayout implements View{
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	private Customer customer;
	
	public CustomerMainView(Navigator navigate, RestaurantDAO rDAO) {
		setMargin(false);
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		customer = null;
		
		//Create menu with options
		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
		
		MenuBar bar = new MenuBar();
		bar.setWidth("100%");
		bar.setHeight("20%");
		
		//bar.setStyleName(ValoTheme.MENU_ROOT);
		
		MenuBar.Command mycommand = new MenuBar.Command() {
	
			@Override
			public void menuSelected(MenuItem selectedItem) {
				navigator.navigateTo("SignUpView");
				
			}
		};
		
		bar.addItem("Edit Personal Information", null, mycommand);
		menuLayout.addComponent(bar);
		addComponent(menuLayout);
	}	
	
	public void setCustomer(Customer cust){
		customer = cust;
	}
}
