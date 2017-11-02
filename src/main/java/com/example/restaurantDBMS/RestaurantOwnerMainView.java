package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * The view that is displayed when the restaurant owner logs in 
 * @author alexliu
 *
 */
public class RestaurantOwnerMainView extends VerticalLayout implements View{
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	
	public RestaurantOwnerMainView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		
		addComponent(new Label("Logged in as the restaurant owner!"));
	}	
}