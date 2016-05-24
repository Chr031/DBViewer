package application.view.components;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.objectaccessor.BaseAccessor;
import application.model.descriptor.objectaccessor.ReversibleAccessor;
import application.model.descriptor.presenter.JFXCollectionPresenter;
import application.utils.StringUtils;
import application.view.TableAndFormService;
import application.view.builder.FormBuilder;
import application.view.saver.DataBinder;
import application.view.saver.MainFormSaver;

/**
 * 
 * @author Christophe
 *
 * @param <T>
 */
public class TableFormComponent<T,Tbase> extends BorderPane {

	private static final Logger log = LogManager.getLogger(TableFormComponent.class);

	@FXML
	public TableView<T> table;

	private Pane resizableParentPane;

	private ClassDescriptor<T> gridClassDescriptor;
	private ClassDescriptor<T> formClassDescriptor;
	private ModelAccessor modelAccessor;
	private TableAndFormService listFormController;
	
	private JFXCollectionPresenter<Tbase, T> basePresenter;

	private final List<Object> dependingData = new ArrayList<>();

	public TableFormComponent(ClassDescriptor<T> gridClassDescriptor, ClassDescriptor<T> formClassDescriptor, JFXCollectionPresenter<Tbase, T> basePresenter) {
		

		this.gridClassDescriptor = gridClassDescriptor;
		this.formClassDescriptor = formClassDescriptor;
		
		this.basePresenter = basePresenter;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("TableFormView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		setPrefHeight(160);

		new FormBuilder().initSizeChangeListener(table, /**
		 * propagate a callback
		 * instead of a hard reference
		 */
		() -> {
			return resizableParentPane;
		});

		initGridProperties();
	}

	@SuppressWarnings("rawtypes")
	private void initGridProperties() {
		// TODO Call TableBuilder class !!!
		for (PropertyDescriptorI desc : gridClassDescriptor.getCommonPropertyDescriptors()) {
			// grid.add(new Label(key), column++, 0);
			TableColumn<T, String> tableColumn = new TableColumn<>(desc.getName());
			table.getColumns().add(tableColumn);
			tableColumn.setCellValueFactory(new Callback<CellDataFeatures<T, String>, ObservableValue<String>>() {
				@SuppressWarnings("unchecked")
				public ObservableValue<String> call(CellDataFeatures<T, String> cell) {
					try {
						String propertyValueAsString = desc.getPropertyValueAsString(cell.getValue());
						propertyValueAsString = StringUtils.wordReducer(propertyValueAsString, 56);
						return new ReadOnlyObjectWrapper<String>(propertyValueAsString);
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
						log.error(e);
						return new ReadOnlyObjectWrapper<String>(e.getMessage());
					}
				}
			});
		}
	}

	public void setModelAccessor(ModelAccessor modelAccessor) {
		this.modelAccessor = modelAccessor;
	}

	public void setListFormController(TableAndFormService listFormController) {
		this.listFormController = listFormController;
	}

	public Collection<T> getCollection() {
		ArrayList<T> list = new ArrayList<>();
		for (T t : table.getItems()) {
			list.add(t);
		}
		return list;

	}

	public void setCollection(Collection<T> collection) throws InstantiationException, IllegalAccessException {
		if (collection != null && collection.size() > 0) {
			// T first = collection.iterator().next();
			// setModelDescriptor((ClassDescriptor<T>)(new
			// ClassDescriptorFactory().getGridDescriptor(first.getClass())));
			table.getItems().addAll(collection);
		} else
			table.getItems().clear();
	}

	@FXML
	public void handleNewData() throws Exception {
		DataBinder<T> binder = new DataBinder<T>() {

			private T data;
			
			{
				try {
					data = formClassDescriptor.getNewClassInstance();
					if (basePresenter.getAccessor().isReverse()) {
						// lets set the field according to this reverse accessor
						BaseAccessor<T,Tbase> reverseAccessor = ((ReversibleAccessor)basePresenter.getAccessor()).createReverseAccessor();
						reverseAccessor.set(data, basePresenter.getLastDataSet());
					}
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("Unable to create a new instance of the class " + formClassDescriptor.getObjectName(), e);
				}
				
			}
			
			@Override
			public T getData() {
				return data;
			}

			@Override
			public void setData(T data, Object... dependingDataArray) {
				table.getItems().add(data);
				dependingData.add(data);
				if (dependingDataArray != null)
					for (Object depending : dependingDataArray) {
						dependingData.add(depending);
					}
			}
		};

		/*
		 * FormPickerComponent<T> picker = new FormPickerComponent<>(binder,
		 * modelAccessor, formClassDescriptor); picker.showAsPopup();
		 */
		listFormController.loadForm(binder, formClassDescriptor.clone(), modelAccessor, new MainFormSaver<T>());
	}

	@FXML
	public void handleDeleteRow() throws IOException {
		T itemToDelete = table.getSelectionModel().getSelectedItem();
		// modelAccessor.delete(itemToDelete);
		table.getItems().remove(itemToDelete);
		// handleRefresh();
	}

	@FXML
	public void handleRefresh() {
		// setModelAccessor(modelAccessor);
	}

	@FXML
	public void handleEditRow(MouseEvent e) throws Exception {
		if (e.getClickCount() < 2)
			return;

		final T itemToEdit = table.getSelectionModel().getSelectedItem();

		DataBinder<T> binder = new DataBinder<T>() {

			@Override
			public T getData() {
				return itemToEdit;
			}

			@Override
			public void setData(T data, Object... dependingDataArray) {
				table.getItems().remove(data);

				// TODO document this point
				table.getItems().add(data);

				dependingData.add(data);
				if (dependingDataArray != null)
					for (Object depending : dependingDataArray) {
						dependingData.add(depending);
					}

			}
		};

		/*
		 * FormPickerComponent<T> formPicker = new FormPickerComponent<>(binder,
		 * modelAccessor, formClassDescriptor); formPicker.showAsPopup();
		 */

		listFormController.loadForm(binder, formClassDescriptor.clone(), modelAccessor, new MainFormSaver<T>());
	}

	@FXML
	public void handlePickData() {
		DataBinder<T> binder = new DataBinder<T>() {

			@Override
			public T getData() {
				return null;
			}

			@Override
			public void setData(T data, Object... dependingDataArray) {
				table.getItems().add(data);

				if (dependingDataArray != null)
					for (Object depending : dependingDataArray) {
						dependingData.add(depending);
					}
			}

		};
		ListPickerComponent<T> picker = new ListPickerComponent<>(binder, modelAccessor, gridClassDescriptor);
		picker.showAsPopup();
	}

	public Collection<? extends Object> getDependingData() {
		return dependingData;
	}

	public Pane getResizableParentPane() {
		return resizableParentPane;
	}

	public void setResizableParentPane(Pane resizableParentPane) {
		this.resizableParentPane = resizableParentPane;
	}
}
