package com.example.customerViews;

import java.util.TreeMap;

import com.example.restaurantDBMS.*;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The view that is displayed before a customer places an order 
 * View will display a form to fill out address information
 * @author alexliu
 *
 */
public class CustomerConfirmOrderView extends CustomerMainView {
	
	private TakeoutOrder order;
	private Binder<TakeoutOrder> binder;
	private TreeMap<MenuItem, Integer> orderItems;

	public CustomerConfirmOrderView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		orderItems = new TreeMap<>();
		binder = new Binder<>();
		order = new TakeoutOrder();
		binder.setBean(order);
		
		initializeContent();
	}
	
	private void initializeContent(){
		VerticalLayout content = getContent();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		// Add header
		Label headerLabel = new Label("Address Details");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		content.addComponent(headerLabel);
		
		//Create panel
		Panel orderPanel = new Panel();
		orderPanel.setWidth("50%");
	
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setWidth("100%");
		panelContent.setHeightUndefined();
		
		//Add address fields
		Label addressLabel = new Label("Street Address");
		addressLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField addressField = new TextField();
		addressField.setPlaceholder("Street and House Number");
		addressField.setWidth("100%");
		panelContent.addComponents(addressLabel, addressField);
		
		Label cityLabel = new Label("Town/City");
		cityLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField cityField = new TextField();
		cityField.setWidth("100%");
		panelContent.addComponents(cityLabel, cityField);
		
		//Bind fields and add validation
		binder.forField(addressField).withValidator(addr -> addr.length() > 0, "This field cannot be left blank.").bind(TakeoutOrder::getStreet, TakeoutOrder::setStreet);
		binder.forField(cityField).withValidator(city -> city.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*"), "City is of incorrect format").bind(TakeoutOrder::getCity, TakeoutOrder::setCity);

		//Add button layout
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidthUndefined();
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Navigator navigator = getNavigator();
				navigator.navigateTo("CustomerPlaceOrderView");
			}
		});
		
		Button confirmButton = new Button("Confirm Order");
		confirmButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		confirmButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) {
					createOrder();
					Notification.show("You have successfully placed your order.");
					getNavigator().navigateTo("CustomerMainView");
					//reset fields
					binder.readBean(null);
					order = new TakeoutOrder();
					binder.setBean(order);
				}
				else
					Notification.show("Not all fields are filled out correctly");
			}
		});
		buttonLayout.addComponents(cancelButton, confirmButton);
		panelContent.addComponent(buttonLayout);
		panelContent.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		
		orderPanel.setContent(panelContent);
		content.addComponent(orderPanel);
	}
	
	private void createOrder(){
		RestaurantDAO dao = getRestaurantDAO();
		order.setOrderID(dao.getMaxOrderID() + 1);
		order.setTimeOfOrder(UI.getCurrent().getPage().getWebBrowser().getCurrentDate().toString());
		order.setOrderStatus("Incomplete");
		
		//update MySQL
		dao.addTakeoutOrder(order, orderItems, getCustomer()); 
	}
	
	public void setOrderItems(TreeMap<MenuItem, Integer> items) {
		orderItems = items;
	}

}
