package com.example.restaurantDBMS;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
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
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

public class AllEmployeesView extends VerticalLayout implements View{
	
	@Autowired
	private UserDAO userDAO;
	
	private Grid<User> grid;
	private Navigator navigator;

	public AllEmployeesView(Navigator navigate, UserDAO uDAO) {
		//initialize globals
		grid = new Grid<>(User.class);
		userDAO = uDAO;
		navigator = navigate;
		
		//set up layout
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//add header
		Label header = new Label("Users"); //change to employees later
	    header.addStyleName(ValoTheme.LABEL_H1);
	    addComponentAsFirst(header);
		
		//set up grid
		grid.setColumns("username", "password", "id", "name", "birthday", "address", "phoneNumber");
		grid.setItems(userDAO.getAllUsers());
		grid.getColumn("username").setHidden(true);
		grid.getColumn("password").setHidden(true);
		grid.getColumn("id").setHidden(true);
		addComponent(grid);
		
	    //add button to go back to login page
		Button backButton = new Button("Back", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("LoginView");
			}
		});
	    backButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
	    addComponent(backButton);
	}

}
