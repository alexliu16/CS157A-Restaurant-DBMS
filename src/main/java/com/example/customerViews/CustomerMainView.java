package com.example.customerViews;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.employeeViews.EmployeeChangePasswordView;
import com.example.employeeViews.EmployeeEditProfileView;
import com.example.restaurantDBMS.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is displayed when a customer first logs in 
 * This view is also displayed when a customer clicks "View Profile"
 * @author alexliu
 *
 */
public class CustomerMainView extends HorizontalLayout implements View{
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	private Customer customer;
	
	//Layouts
	private VerticalLayout contentArea;
	private CssLayout menuArea;
	
	private Label nameLabel;
	
	public CustomerMainView(Navigator navigate, RestaurantDAO rDAO) {
		//initialize globals
		restaurantDAO = rDAO;
		navigator = navigate;
		customer = null;
		nameLabel = new Label();
		
		//Initialize base layout
		setSizeFull();
		setMargin(false);
		addStyleName(ValoTheme.UI_WITH_MENU);
		
		//initialize interior layouts and add them to base layout
		menuArea = new CssLayout();
		menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);
		
		contentArea = new VerticalLayout();
		contentArea.setWidth("100%");
		contentArea.setHeightUndefined();
		
		addComponents(menuArea, contentArea);
		setExpandRatio(contentArea, 1);
		
		buildMenu();
	}	
	
	//Setup the navigation menu on the left hand side
	private void buildMenu() {
		//layout that contains the menu
		CssLayout menuItemsLayout = new CssLayout(); 
		menuItemsLayout.setWidth("280px");
		
		//add user image and name label
		VerticalLayout vLayout = new VerticalLayout(); //layout that contains the image and label
		vLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		vLayout.setMargin(new MarginInfo(true, true, false, true));
		vLayout.setSpacing(false);
	
		Image userImage = new Image(null, new ClassResource("/com/example/restaurantDBMS/images/userIcon.png"));
		userImage.setWidth("60px");
		userImage.setHeight("60px");
		vLayout.addComponent(userImage);
		
		nameLabel.setValue("Name"); //label initially set to name
		vLayout.addComponent(nameLabel); 
		menuItemsLayout.addComponent(vLayout);
		
		//create labels	
		String[] labelNames = new String[]{"Profile", "Restaurant"};
		List<Label> labels = new ArrayList<Label>();
		Label label = null;
		for (String labelName : labelNames) {
			label = new Label(labelName, ContentMode.HTML); 
			label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
			label.addStyleName(ValoTheme.LABEL_H4);
			label.setSizeUndefined();
			labels.add(label);
		}
		
		//create buttons
		String[] buttonNames = new String[]{"View Profile", "Edit Profile", "Change Password", "Update Billing Information", "View Menu", "Place Order", "Logout"};
		List<Button> buttons = new ArrayList<Button>();
		Button button = null;
		for (String buttonName : buttonNames) {
			button = new Button(buttonName); 
            button.setCaptionAsHtml(true);
            button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            buttons.add(button);
		}
		
		//add icons and functionality to buttons
		Button viewProfileButton = buttons.get(0);
		viewProfileButton.setIcon(VaadinIcons.USER);
		viewProfileButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerMainView");
				((CustomerMainView) navigator.getCurrentView()).displayInitialContent();
			}
		});
		
		Button editProfileButton = buttons.get(1);
		editProfileButton.setIcon(VaadinIcons.PENCIL);
		editProfileButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerEditProfileView");
				((CustomerEditProfileView) navigator.getCurrentView()).setCustomer(customer);
				((CustomerEditProfileView) navigator.getCurrentView()).displayInitialContent();
			}
		});

		Button changePasswordButton = buttons.get(2);
		changePasswordButton.setIcon(VaadinIcons.PASSWORD);
		changePasswordButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerChangePasswordView");
				((CustomerChangePasswordView) navigator.getCurrentView()).setCustomer(customer);
			}
		});
		
		Button updateBillingButton = buttons.get(3);
		updateBillingButton.setIcon(VaadinIcons.CREDIT_CARD);
		updateBillingButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerEditBillingView");
				((CustomerEditBillingView) navigator.getCurrentView()).setCustomer(customer);
				((CustomerEditBillingView) navigator.getCurrentView()).updateContent();
			}
		});
		
		Button viewMenuButton = buttons.get(4);
		viewMenuButton.setIcon(VaadinIcons.SPOON);
		viewMenuButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerMenuView");
				((CustomerMenuView) navigator.getCurrentView()).setCustomer(customer);
			}
		});
		
		Button placeOrderButton = buttons.get(5);
		placeOrderButton.setIcon(VaadinIcons.PACKAGE);
		placeOrderButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				navigator.navigateTo("CustomerPlaceOrderView");
				((CustomerPlaceOrderView) navigator.getCurrentView()).setCustomer(customer);
			}
		});
		
		//add labels and buttons
		menuItemsLayout.addComponents(labels.get(0), viewProfileButton, editProfileButton, changePasswordButton, updateBillingButton);
		
		menuItemsLayout.addComponents(labels.get(1), viewMenuButton, placeOrderButton);
		
		//add logout button at bottom of the page
		VerticalLayout vLayout2 = new VerticalLayout(); //layout that contains the logout button
		vLayout2.setMargin(new MarginInfo(true, true, true, false));
		vLayout2.setHeight("50%");
		Button signoutButton = buttons.get(6);
		signoutButton.setIcon(VaadinIcons.SIGN_OUT);
		signoutButton.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				//return to login page when clicked
				navigator.navigateTo("LoginView");
				customer = null;
			}
		
		});
		vLayout2.addComponent(signoutButton);
		vLayout2.setComponentAlignment(signoutButton, Alignment.BOTTOM_LEFT);
		menuItemsLayout.addComponent(vLayout2);
		
		//add menu to menuArea
		menuItemsLayout.addStyleName(ValoTheme.MENU_PART);
		menuArea.addComponent(menuItemsLayout);
	}
	
	public void displayInitialContent() {
		contentArea.removeAllComponents();
		Label headerLabel = new Label("Welcome " + customer.getName());
		headerLabel.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H2);
		contentArea.addComponent(headerLabel);
		
		Panel profilePanel = new Panel("Personal Information");
		VerticalLayout profileLayout = new VerticalLayout();
		profileLayout.setSpacing(false);
		
		Label profileHeaderLabel = new Label("Personal Information");
		profileHeaderLabel.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_BOLD);
		//profileLayout.addComponent(profileHeaderLabel);
		
		//first row will display name on left and email on right
		Label nameLabel = new Label("Name");
		nameLabel.setWidth("50%");
		nameLabel.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H4);
		
		Label emailLabel = new Label("Email");
		emailLabel.setWidth("50%");
		emailLabel.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H4);
		
		HorizontalLayout firstLayout = new HorizontalLayout();
		firstLayout.setMargin(false);
		firstLayout.setWidth("100%");
		firstLayout.addComponents(nameLabel, emailLabel);
		
		HorizontalLayout secondLayout = new HorizontalLayout();
		secondLayout.setMargin(false);
		secondLayout.setWidth("100%");
		secondLayout.addComponents(new Label(customer.getName()), new Label(customer.getEmail()));

		//second row will display birthday on left and phone number on right
		Label birthdayLabel = new Label("Birth date");
		birthdayLabel.setWidth("50%");
		birthdayLabel.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H4);

		Label phoneLabel = new Label("Phone number");
		phoneLabel.setWidth("50%");
		phoneLabel.addStyleNames(ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H4);

		HorizontalLayout thirdLayout = new HorizontalLayout();
		thirdLayout.setMargin(false);
		thirdLayout.setWidth("100%");
		thirdLayout.addComponents(birthdayLabel, phoneLabel);
		
		HorizontalLayout fourthLayout = new HorizontalLayout();
		fourthLayout.setMargin(false);
		fourthLayout.setWidth("100%");
		fourthLayout.addComponents(new Label(customer.getBirthday()), new Label(customer.getPhoneNumber()));
	
		profileLayout.addComponents(firstLayout, secondLayout, thirdLayout, fourthLayout);
		profilePanel.setContent(profileLayout);
		profilePanel.setWidth("100%");
		contentArea.addComponent(profilePanel);
	}
	
	public VerticalLayout getContent() {
		return contentArea;
	}
	
	public Customer getCustomer(){
		return customer;
	}
	
	public Navigator getNavigator() {
		return navigator;
	}
	
	public RestaurantDAO getRestaurantDAO() {
		return restaurantDAO;
	}
	
	//Set the customer and repaint the label
	public void setCustomer(Customer cust){
		customer = cust;
		nameLabel.setValue(customer.getName());
		nameLabel.markAsDirty();
	}
}
