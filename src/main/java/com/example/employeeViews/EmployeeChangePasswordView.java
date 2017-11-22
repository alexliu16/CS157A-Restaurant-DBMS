package com.example.employeeViews;

import com.example.restaurantDBMS.RestaurantDAO;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class EmployeeChangePasswordView extends EmployeeMainView {
	
	public EmployeeChangePasswordView(Navigator navigate, RestaurantDAO dao) {
		super(navigate, dao);
		setupContent();
	}
	
	private void setupContent() {
		VerticalLayout content = getContent();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Add header 
		Label headerLabel = new Label("Change Password");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		content.addComponent(headerLabel);
		
		//Create panel
		Panel passwordPanel = new Panel();
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		FormLayout form = new FormLayout();
		PasswordField currentPasswordField = new PasswordField("Current Password");
		PasswordField newPasswordField = new PasswordField("New Password");
		PasswordField confirmPasswordField = new PasswordField("Retype Password");
		
		form.addComponents(currentPasswordField, newPasswordField, confirmPasswordField);
		
		Button applyButton = new Button("Apply");
		applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		applyButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(currentPasswordField.getValue().equals(getEmployee().getPassword())) {
					String newPassword = newPasswordField.getValue();
					if(newPassword.equals(confirmPasswordField.getValue())){
						getEmployee().setPassword(newPassword);
						getRestaurantDAO().updatePassword(getEmployee().getUsername(), newPassword);
						Notification.show("You have successfully updated your password");
						//clear fields
						currentPasswordField.setValue("");
						newPasswordField.setValue("");
						confirmPasswordField.setValue("");
					}	
					else
						Notification.show("New passwords must match");
				}
				else
					Notification.show("Old password is incorrect");
				
			}
		});
		
		panelContent.addComponents(form, applyButton);
		
		passwordPanel.setWidth("50%");
		passwordPanel.setContent(panelContent);
		content.addComponent(passwordPanel);
		
	}
}
