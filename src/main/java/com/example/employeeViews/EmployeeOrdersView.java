package com.example.employeeViews;

import java.util.List;

import com.example.restaurantDBMS.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class EmployeeOrdersView extends EmployeeMainView {

	public EmployeeOrdersView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
	}
	
	void displayContent() {
		VerticalLayout content = getContent();
		content.removeAllComponents();
		content.setHeight("100%");
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Add header 
		Label headerLabel = new Label("Manage Orders");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1);
		content.addComponent(headerLabel);
		
		//Create panel
		Panel orderPanel = new Panel();
		orderPanel.setWidth("50%");
		orderPanel.setHeight("100%");
		
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
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
				panelContent.addComponents(layout);
			}
		}	
	
		orderPanel.setContent(panelContent);
		content.addComponent(orderPanel);
		content.setExpandRatio(orderPanel, .8f);
		
	}

}
