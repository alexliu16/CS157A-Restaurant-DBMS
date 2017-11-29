package com.example.employeeViews;

import java.util.List;

import com.example.restaurantDBMS.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class EmployeeOrdersView extends EmployeeMainView {
	
	private Panel containerPanel; //panel to hold panels
	private Panel employeeOrdersPanel; //panel that displays the menu
	private Panel onlineOrdersPanel; //panel that displays the current order for the customer

	public EmployeeOrdersView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		//set up layout
		setSizeFull();
		setMargin(false);
		setupPanels();
		initializeContent();
	}
	
	private void initializeContent() {
		VerticalLayout content = getContent();
		content.setHeight("100%");
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Add header 
		Label headerLabel = new Label("Manage Orders");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		content.addComponents(headerLabel, containerPanel);
		content.setExpandRatio(containerPanel, .8f);
	}

	private void setupPanels() {
		containerPanel = new Panel();
		containerPanel.setSizeFull();
		containerPanel.setWidth("80%");
		containerPanel.setHeight("100%");

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		employeeOrdersPanel = new Panel();
		employeeOrdersPanel.setSizeFull();
		onlineOrdersPanel = new Panel();
		onlineOrdersPanel.setSizeFull();

		splitPanel.setSplitPosition(50, Unit.PERCENTAGE);
		splitPanel.setFirstComponent(employeeOrdersPanel);
		splitPanel.setSecondComponent(onlineOrdersPanel);
		containerPanel.setContent(splitPanel);
	}
	
	void displayContent() {
		//VerticalLayout content = getContent();
		
		//display content for employee orders
		VerticalLayout panelContent1 = new VerticalLayout();
		panelContent1.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label header1 = new Label("Dine-in Orders");
		header1.addStyleName(ValoTheme.LABEL_H2);
		panelContent1.addComponent(header1);
		
		List<DineinOrder> orders = getRestaurantDAO().getEmployeeOrders(getEmployee());
	
		for(DineinOrder order: orders) {
			if (!order.getOrderStatus().equals("Completed")) {
				HorizontalLayout layout = new HorizontalLayout();
				layout.setMargin(false);
				layout.setWidth("100%");
				Button button = new Button("View Order");
				button.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						getNavigator().navigateTo("SingleOrderView");
						((SingleOrderView) getNavigator().getCurrentView()).setOrder(order);
						((SingleOrderView) getNavigator().getCurrentView()).displayContent();

					}
				});
				layout.addComponents(new Label("Table " + order.getTableNumber()), button);
				panelContent1.addComponents(layout);
			}
		}	
	
		employeeOrdersPanel.setContent(panelContent1);
		
		//display content for employee orders
		VerticalLayout panelContent2 = new VerticalLayout();
		panelContent2.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label header2 = new Label("Takeout Orders");
		header2.addStyleName(ValoTheme.LABEL_H2);
		panelContent2.addComponent(header2);
		
		List<TakeoutOrder> onlineOrders = getRestaurantDAO().getTakeoutOrders();
	
		for(TakeoutOrder order: onlineOrders) {
			if (!order.getOrderStatus().equals("Completed")) {
				HorizontalLayout layout = new HorizontalLayout();
				layout.setMargin(false);
				layout.setWidth("100%");
				Button button = new Button("View Order");
				button.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						getNavigator().navigateTo("SingleOrderView");
						((SingleOrderView) getNavigator().getCurrentView()).setOrder(order);
						((SingleOrderView) getNavigator().getCurrentView()).displayContent();

					}
				});
				layout.addComponents(new Label("Online Order " + order.getOrderID()), button);
				panelContent2.addComponents(layout);
			}
		}	
	
		onlineOrdersPanel.setContent(panelContent2);
	}

}
