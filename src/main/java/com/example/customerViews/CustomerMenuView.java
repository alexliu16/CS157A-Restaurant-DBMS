package com.example.customerViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.restaurantDBMS.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View that is displayed when the customer wants to view the menu
 * @author alexliu
 *
 */
public class CustomerMenuView extends CustomerMainView{

	public CustomerMenuView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		setupContent();
	}
	
	private void setupContent() {
		VerticalLayout content = getContent();
		content.setHeight("100%");
		content.setMargin(false);
		
		//Create panel
		Panel menuPanel = new Panel();
		menuPanel.setSizeFull();
		
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setWidth("100%");
		panelContent.setHeightUndefined();
		panelContent.setSpacing(false);
		panelContent.setMargin(new MarginInfo(false, true, false, true));
		
		//Add header 
		Label headerLabel = new Label("Menu");
		headerLabel.addStyleNames(ValoTheme.LABEL_H1, ValoTheme.LABEL_BOLD);
		panelContent.addComponent(headerLabel);
		panelContent.setComponentAlignment(headerLabel, Alignment.MIDDLE_CENTER);
		
		//Add menu items
		panelContent.setSpacing(false);
		List<MenuItem> menuItems = getRestaurantDAO().getAllMenuItems();
		
		//Add appetizers
		List<MenuItem> appetizers = getMenuItemsOfType(menuItems, "Appetizer");
		Label appetizersLabel = new Label("Appetizers");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(appetizersLabel);
		panelContent.setComponentAlignment(appetizersLabel, Alignment.MIDDLE_CENTER);
		
		if(appetizers.size() % 2 != 0)
			appetizers.add(null);
		for(int i = 0; i < appetizers.size() / 2; i++) 
			panelContent.addComponent(createHorizontalItemsLayout(appetizers.get(i * 2), appetizers.get(i * 2 + 1)));
		
		//Add entrees
		List<MenuItem> entrees = getMenuItemsOfType(menuItems, "Entree");
		Label entreesLabel = new Label("Entreés");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(entreesLabel);
		panelContent.setComponentAlignment(entreesLabel, Alignment.MIDDLE_CENTER);
		
		if(entrees.size() % 2 != 0)
			entrees.add(null);
		for(int i = 0; i < entrees.size() / 2; i++) 
			panelContent.addComponent(createHorizontalItemsLayout(entrees.get(i * 2), entrees.get(i * 2 + 1)));
		
		//Add desserts
		List<MenuItem> desserts = getMenuItemsOfType(menuItems, "Dessert");
		Label dessertsLabel = new Label("Desserts");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(dessertsLabel);
		panelContent.setComponentAlignment(dessertsLabel, Alignment.MIDDLE_CENTER);
		
		if(desserts.size() % 2 != 0)
			desserts.add(null);
		for(int i = 0; i < desserts.size() / 2; i++) 
			panelContent.addComponent(createHorizontalItemsLayout(desserts.get(i * 2), desserts.get(i * 2 + 1)));
		
		//Add drinks
		List<MenuItem> drinks = getMenuItemsOfType(menuItems, "Drink");
		Label drinksLabel = new Label("Beverages");
		appetizersLabel.addStyleNames(ValoTheme.LABEL_H3);
		panelContent.addComponent(drinksLabel);
		panelContent.setComponentAlignment(drinksLabel, Alignment.MIDDLE_CENTER);
		
		if(drinks.size() % 2 != 0)
			drinks.add(null);
		for(int i = 0; i < drinks.size() / 2; i++) 
			panelContent.addComponent(createHorizontalItemsLayout(drinks.get(i * 2), drinks.get(i * 2 + 1)));
		
		menuPanel.setContent(panelContent);
		content.addComponent(menuPanel);
		
	}
	
	private List<MenuItem> getMenuItemsOfType(List<MenuItem> items, String type) {
		List<MenuItem> list = new ArrayList<>();
		for(MenuItem item: items) 
			if(item.getType().equals(type))
				list.add(item);
		return list;	
	}
	
	private HorizontalLayout createHorizontalItemsLayout(MenuItem item1, MenuItem item2){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.addComponent(createMenuItemLayout(item1));
		if(item2 != null)
			layout.addComponent(createMenuItemLayout(item2));
		else
			layout.setWidth("50%");

		return layout;
	}
	
	private VerticalLayout createMenuItemLayout(MenuItem item){
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(false);
		
		//create horizontal layout with name and price
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		Label itemName = new Label(item.getName());
		itemName.addStyleNames(ValoTheme.LABEL_BOLD);
		Label priceLabel = new Label(Float.toString(item.getPrice()));
		hLayout.addComponents(itemName, priceLabel);
		hLayout.setExpandRatio(itemName, .9f);
		
		Label descriptionLabel = new Label(item.getDescription());
		descriptionLabel.setWidth("85%");
	
		layout.addComponents(hLayout, descriptionLabel);
		return layout;
	}

	
}
