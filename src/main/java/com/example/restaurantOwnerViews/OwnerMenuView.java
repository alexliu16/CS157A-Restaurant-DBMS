package com.example.restaurantOwnerViews;

import java.util.*;

import com.example.restaurantDBMS.*;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View that is displayed when owner wants to view/edit menu
 * @author alexliu
 *
 */
public class OwnerMenuView extends RestaurantOwnerMainView {
	
	private Binder<MenuItem> binder;
	private MenuItem newItem;
	
	private EditMenuWindow window; //window that is displayed when selecting menu items
	private Panel menuPanel; //panel that displays the (editable) menu
	private Panel itemPanel; //panel that displays form to add a new MenuItem
	
	public OwnerMenuView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		binder = new Binder<>();
		newItem = new MenuItem("", "", "", 0);
		menuPanel = new Panel();
		itemPanel = new Panel();
		setupContent(); 
	}
	
	private void setupContent() {
		VerticalLayout content = getContent();
		content.removeAllComponents();
		content.setHeight("100%");
		content.setMargin(false);
		
		//Create panel
		Panel containerPanel = new Panel();
		containerPanel.setSizeFull();
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		
		menuPanel.setSizeFull();
		updateMenu();
		itemPanel.setSizeFull();
		initializeItemPanel();

		splitPanel.setSplitPosition(65, Unit.PERCENTAGE);
		splitPanel.setFirstComponent(menuPanel);
		splitPanel.setSecondComponent(itemPanel);
		containerPanel.setContent(splitPanel);
		content.addComponent(containerPanel);
	}
	
	private void updateMenu() {
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

	private void initializeItemPanel() {
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setWidth("100%");
		
		//Add header
		Label headerLabel = new Label("Add New Menu Item:");
		headerLabel.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_BOLD);
		panelContent.addComponent(headerLabel);
		
		//Create form 
		HorizontalLayout nameLayout = new HorizontalLayout();
		nameLayout.setWidth("100%");
		Label nameLabel = new Label("Name:");
		nameLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField nameField = new TextField();
		nameLayout.addComponents(nameLabel, nameField);
		
		HorizontalLayout typeLayout = new HorizontalLayout();
		typeLayout.setWidth("100%");
		Label typeLabel = new Label("Type:");
		typeLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField typeField = new TextField();
		typeLayout.addComponents(typeLabel, typeField);
		
		HorizontalLayout priceLayout = new HorizontalLayout();
		priceLayout.setWidth("100%");
		Label priceLabel = new Label("Price:");
		priceLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextField priceField = new TextField();
		priceLayout.addComponents(priceLabel, priceField);
		
		Label descriptionLabel = new Label("Description (Optional)");
		descriptionLabel.addStyleName(ValoTheme.LABEL_BOLD);
		TextArea descriptionArea = new TextArea();
		descriptionArea.setWidth("100%");
		descriptionArea.setWordWrap(true);
		
		//Add validation/bindings
		binder.forField(nameField).withValidator(name -> name.length() > 0, "This field cannot be left blank.").bind(MenuItem::getName, MenuItem::setName);
		binder.forField(typeField).withValidator(type -> type.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*"), "Type is of incorrect format").bind(MenuItem::getType, MenuItem::setType);
		binder.forField(priceField).withValidator(state -> state.matches("[0-9]+([,.][0-9]{1,2})?"), "State is of incorrect format").withConverter(new StringToFloatConverter(""))
				.bind(MenuItem::getPrice, MenuItem::setPrice);
		binder.forField(descriptionArea).bind(MenuItem::getDescription, MenuItem::setDescription);
		
		binder.setBean(newItem);
		
		//Add "Add Item" Button
		Button addButton = new Button("Add Item");
		addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		addButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(binder.validate().isOk()) {
					//update MySQL
					getRestaurantDAO().addMenuItem(newItem);
					
					//Reset item
					newItem = new MenuItem("", "", "", 0);
					binder.readBean(null);
					binder.setBean(newItem);
					
					//Display success notification and update the menu
					Notification.show("You have successfully added a new menu item");
					updateMenu();
					
					((MainUI)UI.getCurrent()).updateMenuViews();
				}
				else 
					Notification.show("Not all fields are filled out correctly");
			}

		});
		
		panelContent.addComponents(nameLayout, typeLayout, priceLayout, 
				descriptionLabel, descriptionArea, addButton);
		panelContent.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
		itemPanel.setContent(panelContent);
	}
	
	private List<MenuItem> getMenuItemsOfType(List<MenuItem> items, String type) {
		List<MenuItem> list = new ArrayList<>();
		for(MenuItem item: items) 
			if(item.getType().equals(type))
				list.add(item);
		return list;	
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
				window = new EditMenuWindow(item);
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
	
	private class EditMenuWindow extends Window{
		private Binder<MenuItem> windowBinder;
		private MenuItem item;
		private String oldItemName;
		private TextField nameField;
		private TextField typeField;
		private TextField priceField;
		private TextArea descriptionArea;
		
		public EditMenuWindow(MenuItem i) {
			//initialize globals
			item = i;
			oldItemName = item.getName();
			windowBinder = new Binder<>();
			
			nameField = new TextField();
			typeField = new TextField();
			priceField = new TextField();
			descriptionArea = new TextArea();
			
			//setup window
			this.setWidth("650px");
			this.setHeight("600px");
			this.center();
			setClosable(true);
			addContent();
		}
		
		private void addContent() {
			VerticalLayout content = new VerticalLayout();
			content.setWidth("100%");
			
			//Add header 
			Label header = new Label("Edit Menu Item");
			header.setWidth("100%");
			header.addStyleName(ValoTheme.LABEL_H2);
			content.addComponent(header);
			content.setComponentAlignment(header, Alignment.MIDDLE_CENTER);
			
			HorizontalLayout nameLayout = new HorizontalLayout();
			nameLayout.setWidth("100%");
			Label nameLabel = new Label("Name:");
			nameLabel.addStyleName(ValoTheme.LABEL_BOLD);
			nameLayout.addComponents(nameLabel, nameField);
			
			HorizontalLayout typeLayout = new HorizontalLayout();
			typeLayout.setWidth("100%");
			Label typeLabel = new Label("Type:");
			typeLabel.addStyleName(ValoTheme.LABEL_BOLD);
			typeLayout.addComponents(typeLabel, typeField);
			
			HorizontalLayout priceLayout = new HorizontalLayout();
			priceLayout.setWidth("100%");
			Label priceLabel = new Label("Price:");
			priceLabel.addStyleName(ValoTheme.LABEL_BOLD);
			priceLayout.addComponents(priceLabel, priceField);
			
			Label descriptionLabel = new Label("Description");
			descriptionLabel.addStyleName(ValoTheme.LABEL_BOLD);
			descriptionArea.setWidth("100%");
			descriptionArea.setWordWrap(true);
			
			//Bind fields
			windowBinder.forField(nameField).withValidator(name -> name.length() > 0, "This field cannot be left blank.").bind(MenuItem::getName, MenuItem::setName);
			windowBinder.forField(typeField).withValidator(type -> type.matches("[a-zA-Z]+(\\s+[a-zA-Z]+)*"), "Type is of incorrect format").bind(MenuItem::getType, MenuItem::setType);
			windowBinder.forField(priceField).withValidator(state -> state.matches("[0-9]+([,.][0-9]{1,2})?"), "State is of incorrect format").withConverter(new StringToFloatConverter(""))
					.bind(MenuItem::getPrice, MenuItem::setPrice);
			windowBinder.forField(descriptionArea).bind(MenuItem::getDescription, MenuItem::setDescription);
			
			windowBinder.setBean(item);
			windowBinder.readBean(item);
		
			//Add buttons
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setWidth("100%");
			
			Button cancelButton = new Button("Cancel");
			cancelButton.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().removeWindow(window);
				}
			});
		
			Button addButton = new Button("Confirm Changes");
			addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			addButton.addClickListener(new Button.ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {
					if(windowBinder.validate().isOk()) {
						getRestaurantDAO().updateMenuItem(item, oldItemName);
						UI.getCurrent().removeWindow(window);
						windowBinder.readBean(null);
						updateMenu();
						((MainUI)UI.getCurrent()).updateMenuViews();
					}
					else
						Notification.show("Not all fields are filled out correctly");
				}
				
			});
			
			Button deleteButton = new Button("Remove Item");
			deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
			deleteButton.addClickListener(new Button.ClickListener(){

				@Override
				public void buttonClick(ClickEvent event) {
					//remove item
					getRestaurantDAO().deleteMenuItem(item.getName());
					UI.getCurrent().removeWindow(window);
					updateMenu();
					((MainUI)UI.getCurrent()).updateMenuViews();
				}
				
			});
			
			buttonLayout.addComponents(cancelButton, addButton, deleteButton);
			
			content.addComponents(nameLayout, typeLayout, priceLayout, descriptionLabel, descriptionArea, buttonLayout);
			content.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
			this.setContent(content);
		}
		
		@Override
		public boolean equals(Object other) {
			return super.equals(other) && item.equals(((EditMenuWindow)other).item);
		}
		
	}	

}
