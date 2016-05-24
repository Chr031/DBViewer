package application.view;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterCallBack;
import application.model.descriptor.presenter.PresenterI;
import application.model.descriptor.presenter.PresenterType;
import application.view.builder.FormBuilder;
import application.view.handler.ComputeCalculatedFieldsHandler;
import application.view.saver.DataBinder;
import application.view.saver.FormSaver;

public class FormViewController<T> {

	private TableAndFormService listFormController;
	private ModelAccessor modelAccessor;
	private ClassDescriptor<T> descriptor;
	private ObjectProperty<Class<? extends T>> childClassSelection;
	private FormSaver<T> formSaver;
	private DataBinder<T> binder;

	private T data;

	@FXML
	public ScrollPane scroller;

	@FXML
	public VBox vbox;

	public FormViewController(TableAndFormService listFormController, ClassDescriptor<T> classDescriptor, ModelAccessor modelAccessor,
			FormSaver<T> formSaver, DataBinder<T> binder) {
		this.listFormController = listFormController;
		this.descriptor = classDescriptor;
		this.modelAccessor = modelAccessor;
		this.formSaver = formSaver;
		this.binder = binder;
	}

	@FXML
	public void initialize() throws Exception {

		vbox.prefWidthProperty().bind(scroller.widthProperty().subtract(20));
		vbox.getStyleClass().add("padded");

		childClassSelection = new FormBuilder().buildFormLayout(descriptor, vbox);

		this.data = binder.getData();

		// descriptor.bindWith(modelAccessor, data);

		descriptor.foreachPresenter(PresenterType.JFX,
				new PresenterCallBack<T>() {
					@Override
					public void call(PresenterI<T> presenter, Class<? extends T> objectClass) throws Exception {
						JFXPresenter p = (JFXPresenter) presenter;
						p.setModelAccessor(modelAccessor);
						p.setListFormController(listFormController);
						// p.set
						if (data == null || objectClass.isAssignableFrom(data.getClass())) {
							p.setNodeContent(data);
						}

					}
				}
				);

		if (childClassSelection != null) {
			childClassSelection.addListener(new ChangeListener<Class<? extends T>>() {
				@Override
				public void changed(ObservableValue<? extends Class<? extends T>> observable, Class<? extends T> oldValue,
						Class<? extends T> newValue) {
					descriptor.setActiveChildClass(newValue);

				}
			});
			if (data != null)
				childClassSelection.setValue((Class<? extends T>) data.getClass());
		}

		if (descriptor.hasCalculatedProperties()) {
			List<PropertyDescriptorI> calculatedProperties = descriptor.getCalculatedProperties();
			for (PropertyDescriptorI pDesc : descriptor.getAllPropertyDescriptors()) {
				if (!calculatedProperties.contains(pDesc)) {
					((JFXPresenter) pDesc.getPresenter(PresenterType.JFX))
							.addChangeCallback(new ComputeCalculatedFieldsHandler(descriptor));

				}
			}
		}

	}

	@FXML
	public void save() throws Exception {
		formSaver.save(data, descriptor, binder);
		// this suppose that this is the current active pane...
		listFormController.removeActiveTab();
	}

	@FXML
	public void cancel() {
		formSaver.cancel();
		// this suppose that this is the current active pane...
		listFormController.removeActiveTab();
	}

}
