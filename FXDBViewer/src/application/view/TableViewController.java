package application.view;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.dataaccessor.ModelAccessor;
import application.model.dataaccessor.condition.Condition;
import application.model.descriptor.ClassDescriptor;
import application.view.builder.TableBuilder;
import application.view.components.FilterEvaluator;
import application.view.saver.DataBinder;
import application.view.saver.MainFormSaver;
import application.view.saver.ModelAccessorDataBinder;

public class TableViewController<T> {

	private static final Logger log = LogManager.getLogger(TableViewController.class);

	private final TableAndFormService tableAndFormService;
	private final ClassDescriptor<T> classDescriptor;
	private final ModelAccessor modelAccessor;
	private final Condition<T> condition;
	private final FilterEvaluator<T> filterEvaluator;

	@FXML
	private Pane root;
	@FXML
	private TextField filter;
	@FXML
	public TableView<T> table;

	public TableViewController(TableAndFormService listFormController, ClassDescriptor<T> classDescriptor, ModelAccessor modelAccessor) {
		this(listFormController, classDescriptor, modelAccessor, null);
	}

	public TableViewController(TableAndFormService listFormController, ClassDescriptor<T> classDescriptor, ModelAccessor modelAccessor, Condition<T> condition ) {
		this.tableAndFormService = listFormController;
		this.filterEvaluator = new FilterEvaluator<T>();
		this.classDescriptor = classDescriptor;
		this.modelAccessor = modelAccessor;
		this.condition = condition;
	}

	@FXML
	public void initialize() throws IOException {

		table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					try {
						handleEditRaw();
					} catch (Exception e) {
						log.error("Unable to edit the current row ", e);
					}
				}
			}
		});
		
		this.initializeTableColumns();		
		this.initializeTableData();
		
	}

	private void initializeTableColumns() {		

		new TableBuilder().buildTableLayout(tableAndFormService, table, classDescriptor);

		root.visibleProperty().addListener((ChangeListener<Boolean>) (c, oldV, newV) -> {
			if (newV)
				try {
					this.handleRefresh();
				} catch (Exception e) {
					log.error("Unable to refresh the table ", e);
				}
		});
	}

	private void initializeTableData() throws IOException {

		ObservableList<T> data = FXCollections.observableArrayList();
		data.addAll(modelAccessor.get(classDescriptor.getObjectClass(), condition));

		if (classDescriptor.hasChildClasses()) {
			for (Class<? extends T> childClass : classDescriptor.getChildObjectClasses()) {				
				data.addAll(modelAccessor.get(childClass,(Condition) condition));
			}
		}

		setTableMasterData(data);
	}

	protected void setTableMasterData(ObservableList<T> masterData) {

		// rewrap this list with a filter possibility...

		FilteredList<T> filteredData = new FilteredList<>(masterData, t -> true);

		// There maybe there a memory leak...
		// I don't see how the newly created listener can be a weak listener;
		filter.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(t -> {
				// If filter text is empty, display all data.
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					// TODO implement a nice and/or search compiler.
					return filterEvaluator.evaluate(classDescriptor, newValue, t);
				});
		});

		if (table.getItems().size() > 0) {
			// table.getItems().clear();
		}
		table.setItems(filteredData);
		/*
		 * workArround : See
		 * http://stackoverflow.com/questions/11065140/javafx-2-1
		 * -tableview-refresh-items should be fixed with j8u60
		 */
		if (table.getColumns().size() > 0) {
			table.getColumns().get(0).setVisible(false);
			table.getColumns().get(0).setVisible(true);
		}
	}

	@FXML
	public void handleNewData() throws Exception {
		DataBinder<T> binder = new ModelAccessorDataBinder<T>(classDescriptor.getNewClassInstance(), modelAccessor);
		tableAndFormService.loadForm(binder, classDescriptor.getObjectClass(), new MainFormSaver<T>());
	}

	@FXML
	public void handleDeleteRow() throws IOException {
		T itemToDelete = table.getSelectionModel().getSelectedItem();
		modelAccessor.delete(itemToDelete);
		handleRefresh();
	}

	@FXML
	public void handleRefresh() throws IOException {
		initializeTableData();
	}

	@FXML
	public void handleEditRaw() throws Exception {
		T itemToEdit = table.getSelectionModel().getSelectedItem();
		DataBinder<T> binder = new ModelAccessorDataBinder<T>(itemToEdit, modelAccessor);
		tableAndFormService.loadForm(binder, classDescriptor.getObjectClass(), new MainFormSaver<T>());
	}

}
