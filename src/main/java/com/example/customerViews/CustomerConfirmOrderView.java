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

public class CustomerConfirmOrderView extends CustomerMainView {
	
	private Address address;
	private Binder<Address> binder;
	private TreeMap<MenuItem, Integer> orderItems;

	public CustomerConfirmOrderView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		orderItems = new TreeMap<>();
		binder = new Binder<>();
		address = new Address();
		binder.setBean(address);
		
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

		Label stateLabel = new Label("State");
		stateLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField stateField = new TextField();
		stateField.setWidth("100%");
		panelContent.addComponents(stateLabel, stateField);

		Label zipLabel = new Label("Zip");
		zipLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField zipField = new TextField();
		zipField.setWidth("100%");
		panelContent.addComponents(zipLabel, zipField);
		
		//Bind fields and add validation
		binder.forField(addressField).withValidator(addr -> addr.length() > 0, "This field cannot be left blank.").bind(Address::getStreet, Address::setStreet);
		binder.forField(cityField).withValidator(city -> city.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*"), "City is of incorrect format").bind(Address::getCity, Address::setCity);
		binder.forField(stateField).withValidator(state -> state.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*"), "State is of incorrect format")
				.bind(Address::getState, Address::setState);
		binder.forField(zipField).withValidator(zip -> zip.matches("\\d{5}"),
				"Phone number is of incorrect format").withConverter(new StringToIntegerConverter("")).bind(Address::getZip, Address::setZip);
		
		zipField.setValue("");
		
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
					address = new Address();
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
		Order order = new Order(dao.getMaxOrderID() + 1, UI.getCurrent().getPage().getWebBrowser().getCurrentDate().toString());

		//update MySQL
		dao.addOrder(order); //Update "Orders" table
		dao.addTakeoutOrder(order, address); //Update "TakeoutOrders" table
		dao.addOrderToCustomer(getCustomer(), order); //Update "Place" table
		for(MenuItem item: orderItems.keySet())
			dao.addOrderItem(order, item, orderItems.get(item)); //Update "Contain" table
		
	}
	
	public void setOrderItems(TreeMap<MenuItem, Integer> items) {
		orderItems = items;
	}

}
