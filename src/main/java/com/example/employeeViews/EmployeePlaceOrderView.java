package com.example.employeeViews;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.example.restaurantDBMS.MenuItem;
import com.example.restaurantDBMS.Order;
import com.example.restaurantDBMS.RestaurantDAO;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View that is displayed when the employee places an order for a table
 * @author alexliu
 *
 */
public class EmployeePlaceOrderView extends EmployeeMainView{
	
	private MenuWindow window; //window that is displayed when selecting menu items
	private Panel menuPanel; //panel that displays the menu
	private Panel orderPanel; //panel that displays the current order for the customer
	
	//classes to handle customer order
	private TreeMap<MenuItem, Integer> orderItems; //map of menu items to the quantity ordered in customer's order
	private VerticalLayout itemsLayout; //layout that displays the current items in the customer's order
	private Label subtotal;
	private Label taxTotal;
	private Label total;
	private TextField tableField;
	private boolean editQuantity;

	public EmployeePlaceOrderView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		
		//initialize globals
		menuPanel = new Panel();
		orderPanel = new Panel();
		itemsLayout = new VerticalLayout();
		
		subtotal = new Label("$0.00");
		taxTotal = new Label("$0.00");
		total = new Label("$0.00");
		tableField = new TextField();
		orderItems = new TreeMap<>();
		editQuantity = false;
		
		setupContent();
	}
	
	private void setupContent() {
		VerticalLayout content = getContent();
		content.setHeight("100%");
		content.setMargin(false);
		
		//Create panel
		Panel containerPanel = new Panel();
		containerPanel.setSizeFull();
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		
		menuPanel.setSizeFull();
		initializeMenuPanel();
		
		orderPanel.setSizeFull();
		initializeOrderPanel();

		splitPanel.setSplitPosition(70, Unit.PERCENTAGE);
		splitPanel.setFirstComponent(menuPanel);
		splitPanel.setSecondComponent(orderPanel);
		containerPanel.setContent(splitPanel);
		content.addComponent(containerPanel);
		
	}
	
	private void initializeMenuPanel() {
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setHeightUndefined();
		panelContent.setSpacing(false);
		panelContent.setMargin(new MarginInfo(false, true, false, true));
		
		//Add header 
		Label headerLabel = new Label("Menu");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1, ValoTheme.LABEL_BOLD);
		panelContent.addComponent(headerLabel);
		panelContent.setComponentAlignment(headerLabel, Alignment.MIDDLE_CENTER);
		
		//Add menu items
		List<MenuItem> menuItems = getRestaurantDAO().getAllMenuItems();
		
		//Add appetizers
		List<MenuItem> appetizers = getMenuItemsOfType(menuItems, "Appetizer");
		Label appetizersLabel = new Label("Appetizers");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(appetizersLabel);
		panelContent.setComponentAlignment(appetizersLabel, Alignment.MIDDLE_CENTER);
		
		for(int i = 0; i < appetizers.size(); i++) 
			panelContent.addComponent(createMenuItemLayout(appetizers.get(i)));
		
		//Add entrees
		List<MenuItem> entrees = getMenuItemsOfType(menuItems, "Entree");
		Label entreesLabel = new Label("Entreés");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(entreesLabel);
		panelContent.setComponentAlignment(entreesLabel, Alignment.MIDDLE_CENTER);
		
		for(int i = 0; i < entrees.size(); i++) 
			panelContent.addComponent(createMenuItemLayout(entrees.get(i)));
		
		//Add desserts
		List<MenuItem> desserts = getMenuItemsOfType(menuItems, "Dessert");
		Label dessertsLabel = new Label("Desserts");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(dessertsLabel);
		panelContent.setComponentAlignment(dessertsLabel, Alignment.MIDDLE_CENTER);
		
		for(int i = 0; i < desserts.size(); i++) 
			panelContent.addComponent(createMenuItemLayout(desserts.get(i)));
		
		//Add drinks
		List<MenuItem> drinks = getMenuItemsOfType(menuItems, "Drink");
		Label drinksLabel = new Label("Beverages");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(drinksLabel);
		panelContent.setComponentAlignment(drinksLabel, Alignment.MIDDLE_CENTER);

		for(int i = 0; i < drinks.size(); i++) 
			panelContent.addComponent(createMenuItemLayout(drinks.get(i)));
		
		menuPanel.setContent(panelContent);
	}

	private void initializeOrderPanel() {
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setWidth("100%");
		
		// Add header
		Label headerLabel = new Label("Place Dine-in Order:");
		headerLabel.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_BOLD);
		panelContent.addComponent(headerLabel);
		
		//Add order items: initally empty
		itemsLayout.setWidth("100%");
		itemsLayout.setMargin(false);
		itemsLayout.setSpacing(false);
		itemsLayout.addComponent(new Label("Cart is Empty"));
		
		//Add price labels
		HorizontalLayout subtotalLayout = new HorizontalLayout();
		subtotalLayout.setWidth("100%");
		Label subtotalLabel = new Label("Subtotal:");
		subtotalLayout.addComponents(subtotalLabel, subtotal);
		subtotalLayout.setExpandRatio(subtotalLabel, .9f);
		
		HorizontalLayout taxLayout = new HorizontalLayout();
		taxLayout.setWidth("100%");
		Label taxLabel = new Label("Tax (8.25%): ");
		taxLayout.addComponents(taxLabel, taxTotal);
		taxLayout.setExpandRatio(taxLabel, .9f);
		
		HorizontalLayout totalLayout = new HorizontalLayout();
		totalLayout.setWidth("100%");
		Label totalLabel = new Label("Total: ");
		totalLabel.addStyleName(ValoTheme.LABEL_BOLD);
		total.addStyleName(ValoTheme.LABEL_BOLD);
		totalLayout.addComponents(totalLabel, total);
		totalLayout.setExpandRatio(totalLabel, .9f);
		
		HorizontalLayout tableLayout = new HorizontalLayout();
		tableLayout.setWidth("100%");
		Label tableLabel = new Label("Table Number: ");
		tableLabel.addStyleName(ValoTheme.LABEL_BOLD);
		total.addStyleName(ValoTheme.LABEL_BOLD);

		tableField.setWidth("100%");
		tableLayout.addComponents(tableLabel, tableField);
		
		//Add "Place Order" Button
		Button orderButton = new Button("Place Order");
		orderButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		orderButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(orderItems.isEmpty()) //no items in order
					Notification.show("No items in order");
				else if(!tableField.getValue().matches("[0-9]+"))
					Notification.show("Table number must be a valid number");
				else {
					//add order to MySQL
					createOrder();
					//reset order
					tableField.setValue("");
					Notification.show("You have successfully placed an order");
					orderItems = new TreeMap<>();
					updateOrder();
				}
			}
		});
		
		panelContent.addComponents(itemsLayout, subtotalLayout, taxLayout, totalLayout, tableLayout, orderButton);
		panelContent.setComponentAlignment(orderButton, Alignment.MIDDLE_CENTER);
		orderPanel.setContent(panelContent);
	}
	
	private void createOrder(){
		RestaurantDAO dao = getRestaurantDAO();
		Order order = new Order(dao.getMaxOrderID() + 1, UI.getCurrent().getPage().getWebBrowser().getCurrentDate().toString(), "Incomplete");

		//update MySQL
		dao.addOrder(order); //Update "Orders" table
		dao.addDineinOrder(order, Integer.parseInt(tableField.getValue())); //Update "DineinOrders" table
		dao.addOrderToEmployee(getEmployee(), order); //Update "Manage" table
		for(MenuItem item: orderItems.keySet())
			dao.addOrderItem(order, item, orderItems.get(item)); //Update "Contain" table
	}
	
	private List<MenuItem> getMenuItemsOfType(List<MenuItem> items, String type) {
		List<MenuItem> list = new ArrayList<>();
		for(MenuItem item: items) 
			if(item.getType().equals(type))
				list.add(item);
		return list;	
	}
	
	private void updateOrder() {
		itemsLayout.removeAllComponents(); //clear all previous items
		float currentSubtotal = 0;
		int pos = 1;
		
		HorizontalLayout hLayout, buttonLayout;
		for(MenuItem item: orderItems.keySet()) {
			hLayout = new HorizontalLayout();
			hLayout.setWidth("100%");
			hLayout.setMargin(false);
			Label itemLabel = new Label(pos++ + ". " + item.getName() + " x " + orderItems.get(item));
			itemLabel.setWidth("80%");
			Label totalPriceLabel = new Label("$" + (item.getPrice() * orderItems.get(item)));
			hLayout.addComponents(itemLabel, totalPriceLabel);
			hLayout.setExpandRatio(itemLabel, .8f);
			
			//Add buttons
			buttonLayout = new HorizontalLayout();
			buttonLayout.setMargin(false);
			Button editButton = new Button("Edit");
			editButton.addStyleName(ValoTheme.BUTTON_LINK);
			editButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					editQuantity = true;
					window = new MenuWindow(item);
					window.updateQuantity(orderItems.get(item));
					UI.getCurrent().addWindow(window);
					updateOrder();
				}
			
			});
			
			Button deleteButton = new Button("Delete");
			deleteButton.addStyleName(ValoTheme.BUTTON_LINK);
			deleteButton.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					orderItems.remove(item);
					updateOrder();
				}
			});
			buttonLayout.addComponents(editButton, deleteButton);
			itemsLayout.addComponents(hLayout, buttonLayout);
			itemsLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
			
			currentSubtotal += orderItems.get(item) * item.getPrice();
		}
		
		//update labels
		float tax = currentSubtotal * 0.0875f;
		subtotal.setValue("$" + String.format("%.2f", currentSubtotal));
		taxTotal.setValue("$" + String.format("%.2f", tax));
		total.setValue("$" + String.format("%.2f", (currentSubtotal + tax)));
		
		itemsLayout.markAsDirty(); //redraw component
		
	}
	
	private VerticalLayout createMenuItemLayout(MenuItem item){
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		layout.setWidth("100%");
		
		//create horizontal layout with name and price
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		Button itemName = new Button(item.getName());
		itemName.addStyleNames(ValoTheme.BUTTON_LINK);
		itemName.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				window = new MenuWindow(item);
				UI.getCurrent().addWindow(window);
			}
		});
		Label priceLabel = new Label(Float.toString(item.getPrice()));
		hLayout.addComponents(itemName, priceLabel);
		hLayout.setExpandRatio(itemName, .9f);
		
		Label descriptionLabel = new Label(item.getDescription());
		descriptionLabel.setWidth("80%");
	
		layout.addComponents(hLayout, descriptionLabel);
		return layout;
	}
	
	private class MenuWindow extends Window{
		private MenuItem item;
		private Label priceLabel;
		private Label totalPriceLabel;
		private TextField quantityField;
		
		public MenuWindow(MenuItem i) {
			//super(i.getName());
			item = i;
		
			this.setWidth("500px");
			this.setHeight("350px");
			this.center();
			
			setClosable(true);
			
			addContent();
		}
		
		private void addContent() {
			VerticalLayout content = new VerticalLayout();
			
			//Add header 
			Label header = new Label(item.getName());
			header.setWidth("100%");
			header.addStyleName(ValoTheme.LABEL_H1);
			content.addComponent(header);
			
			//Add price row
			HorizontalLayout priceLayout = new HorizontalLayout();
			priceLayout.setWidth("100%");
			priceLabel = new Label("Price: 1 x " + item.getPrice() + " =");
			priceLabel.addStyleName(ValoTheme.LABEL_BOLD);
			totalPriceLabel = new Label("$" + item.getPrice());
			totalPriceLabel.addStyleName(ValoTheme.LABEL_BOLD);
			priceLayout.addComponents(priceLabel, totalPriceLabel);
			priceLayout.setExpandRatio(priceLabel, .9f);
			
			//Add quantity row
			HorizontalLayout quantityLayout = new HorizontalLayout();
			quantityLayout.setWidth("100%");
			Label quantityLabel = new Label("Quantity: ");
			quantityLabel.setWidth("70%");
			quantityLabel.addStyleName(ValoTheme.LABEL_BOLD);
			quantityField = new TextField();
			quantityField.setValueChangeMode(ValueChangeMode.LAZY);
			quantityField.setWidth("100%");
			quantityField.setValue("1");
			quantityField.addValueChangeListener(new ValueChangeListener<String>() {

				@Override
				public void valueChange(ValueChangeEvent<String> event) {
					if(event.getValue().matches("\\d+"))
						totalPriceLabel.setValue("$" + ((Integer.parseInt(quantityField.getValue()) * item.getPrice())));
					else
						quantityField.setValue("1");
					
				}
				
			});
			quantityLayout.addComponents(quantityLabel, quantityField);
			
			//Add buttons
			HorizontalLayout buttonLayout = new HorizontalLayout();
			Button addButton = new Button("Add To Cart");
			addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			addButton.addClickListener(new Button.ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {
					int quantity = Integer.parseInt(quantityField.getValue());
					if(orderItems.containsKey(item) && !editQuantity) { //item in order already - update quantity
						orderItems.put(item, orderItems.get(item) + quantity);
						editQuantity = false;
					}	
					else //item not in currently in order
						orderItems.put(item, quantity);
					updateOrder();
					UI.getCurrent().removeWindow(window);
				}
				
			});
			
			Button cancelButton = new Button("Cancel");
			cancelButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().removeWindow(window);
				}
			});
			buttonLayout.addComponents(addButton, cancelButton);
			
			content.addComponents(quantityLayout, priceLayout, buttonLayout);
			content.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
			this.setContent(content);
		}
		
		public void updateQuantity(int quantity){
			priceLabel.setValue("Price: " + quantity + " x " + item.getPrice() + " =");
			totalPriceLabel.setValue("$" + item.getPrice() * quantity);
			quantityField.setValue(Integer.toString(quantity));
			
		}
		
		@Override
		public boolean equals(Object other) {
			return super.equals(other) && item.equals(((MenuWindow)other).item);
		}
		
	}	
}
