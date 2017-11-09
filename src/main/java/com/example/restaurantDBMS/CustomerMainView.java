package com.example.restaurantDBMS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is displayed when a customer first logs in 
 * @author alexliu
 *
 */
public class CustomerMainView extends HorizontalLayout implements View{
	
	@Autowired
	private RestaurantDAO restaurantDAO;

	private Navigator navigator;
	private Customer customer;
	
	//Layouts
	private CssLayout contentArea;
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
		
		contentArea = new CssLayout();
		contentArea.setSizeFull();
		
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
		String[] buttonNames = new String[]{"Edit Profile", "Update Billing Information", "View Menu", "Place Order", "Logout"};
		List<Button> buttons = new ArrayList<Button>();
		Button button = null;
		for (String buttonName : buttonNames) {
			button = new Button(buttonName); 
            button.setCaptionAsHtml(true);
            button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            buttons.add(button);
		}
		
		//add icons and functionality to buttons
		buttons.get(0).setIcon(VaadinIcons.USER);
		buttons.get(1).setIcon(VaadinIcons.CREDIT_CARD);
		buttons.get(2).setIcon(VaadinIcons.MENU);
		buttons.get(3).setIcon(VaadinIcons.PACKAGE);
		buttons.get(4).setIcon(VaadinIcons.SIGN_OUT);
		
		//add labels and buttons
		menuItemsLayout.addComponent(labels.get(0));
		menuItemsLayout.addComponent(buttons.get(0));
		menuItemsLayout.addComponent(buttons.get(1));
		
		menuItemsLayout.addComponent(labels.get(1));
		menuItemsLayout.addComponent(buttons.get(2));
		menuItemsLayout.addComponent(buttons.get(3));
		
		//add logout button at bottom of the page
		VerticalLayout vLayout2 = new VerticalLayout(); //layout that contains the logout button
		vLayout2.setMargin(new MarginInfo(true, true, true, false));
		vLayout2.setHeight("50%");
		Button signoutButton = buttons.get(4);
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
	
	//Set the customer and repaint the label
	public void setCustomer(Customer cust){
		customer = cust;
		nameLabel.setValue(customer.getName());
		nameLabel.markAsDirty();
	}
}
