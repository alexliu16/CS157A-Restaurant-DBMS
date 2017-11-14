package com.example.restaurantOwnerViews;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.restaurantDBMS.Employee;
import com.example.restaurantDBMS.RestaurantDAO;
import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.themes.ValoTheme;

public class AllEmployeesView extends RestaurantOwnerMainView{
	
	@Autowired
	private RestaurantDAO restaurantDAO;
	
	private Grid<Employee> grid;
	private Employee selectedEmployee;

	public AllEmployeesView(Navigator navigate, RestaurantDAO rDAO) {
		super(navigate, rDAO);
		
		//initialize globals
		grid = new Grid<>(Employee.class);
		restaurantDAO = rDAO;
		selectedEmployee = null;
		
		setupContent();
	}
	
	private void setupContent(){
		VerticalLayout layout = getContent();
		layout.removeAllComponents();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.setSpacing(false);
		//layout.setMargin(false);
		
		//set up header
		Label header = new Label("Current Employees"); 
	    header.addStyleName(ValoTheme.LABEL_H1);
	    layout.addComponent(header);
	    
		//set up grid
		grid.setSizeFull();
		grid.setDescription("Double click on a row to edit.");
		grid.setColumns("name", "position", "salary", "username", "id", "birthday", "email", "phoneNumber", "password");
		grid.setItems(restaurantDAO.getAllEmployees());
		
		//Create header row to place filters
		HeaderRow filterRow = grid.appendHeaderRow();
		filterRow.getCell("name").setComponent(createTextFilter("name"));;
		filterRow.getCell("position").setComponent(createTextFilter("position"));;
		
		int numCols = grid.getColumns().size();
		for(int i = 0; i < numCols; i++) {
			//grid.getColumns().get(i).setMinimumWidth(200);
			grid.getColumns().get(i).setMinimumWidthFromContent(true);
			grid.getColumns().get(i).setStyleGenerator(item -> "v-align-center");
		}	
	
		grid.removeColumn("password");
		
		//create inline grid editor
		grid.getEditor().setBuffered(true);
		grid.getEditor().setEnabled(true);
		
		//only allow owner to edit position and salary
		TextField positionField = new TextField();
		grid.getColumn("position").setEditorComponent(positionField);
		TextField salaryField = new TextField();
		Binder<Employee> binder = grid.getEditor().getBinder();
		Binding<Employee, Integer> binding = binder.forField(salaryField).withValidator(salary -> salary.matches("[0-9,]+"), "Salary is not a valid number").withConverter(new StringToIntegerConverter("New Salary")).bind(Employee::getSalary, Employee::setSalary);
		grid.getColumn("salary").setEditorBinding(binding);
	
		grid.addItemClickListener(new ItemClickListener<Employee>() {

			@Override
			public void itemClick(ItemClick<Employee> event) {
				selectedEmployee = event.getItem();
			}
			
		});
		
		grid.getEditor().addSaveListener(new EditorSaveListener<Employee>() {

			@Override
			public void onEditorSave(EditorSaveEvent<Employee> event) {
				if(binder.validate().isOk()){
					restaurantDAO.updateEmployeePosition(selectedEmployee.getUsername(), positionField.getValue());
					restaurantDAO.updateEmployeeSalary(selectedEmployee.getUsername(), Integer.parseInt(salaryField.getValue().replaceAll(",", "")));
				}	
			}
		
		});
		
		//add fire button column
		grid.addColumn(person -> "Fire",  new ButtonRenderer<Employee>(clickEvent -> {
			restaurantDAO.deleteEmployee(clickEvent.getItem().getUsername()); //delete employee information 
			grid.setItems(restaurantDAO.getAllEmployees());
			grid.getDataProvider().refreshAll();
		}));
		
		layout.addComponent(grid);
	}
	
	private TextField createTextFilter(String colName){
		 TextField filter = new TextField();
		 filter.setHeight("30px");
		 filter.setWidth("80px");
		 if(colName.equals("name")) {
			 filter.setPlaceholder("Filter by name");
			 filter.setDescription("Search employees by name");
		 }	 
		 else {
			 filter.setPlaceholder("Filter by position");
			 filter.setDescription("Search employees by position");
		 }	 
		 
		 filter.addValueChangeListener(new ValueChangeListener<String>() {

			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				ListDataProvider<Employee> dataProvider = (ListDataProvider<Employee>) grid.getDataProvider();
				if(colName.equals("name"))
					dataProvider.setFilter(Employee::getName, name -> name.toLowerCase().contains((String)event.getValue().toLowerCase()));
				else
					dataProvider.setFilter(Employee::getPosition, job -> job.toLowerCase().contains((String)event.getValue().toLowerCase()));
			}
		 
		 });
		 
		 return filter;
	}
	
	public void update(){
		grid.setItems(restaurantDAO.getAllEmployees());
		grid.getDataProvider().refreshAll();
	}

}
