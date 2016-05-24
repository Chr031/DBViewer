package application.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterType;
import application.utils.Caller;
import application.view.builder.FormBuilder;
import application.view.saver.DataBinder;


@Deprecated
public class FormPickerComponent<T> extends BorderPane {
	private final ClassDescriptor<T> descriptor;
	private final ModelAccessor modelAccessor;
	private final DataBinder<T> binder;

	private T data;
	//private final List<Object> dependingData = new ArrayList<>();

	@FXML
	private VBox vbox;

	private Caller closeCaller;

	public FormPickerComponent(DataBinder<T> binder, ModelAccessor modelAccessor, ClassDescriptor<T> descriptor) {
		this.modelAccessor = modelAccessor;
		this.descriptor = descriptor;
		this.binder = binder;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("FormPickerView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void showAsPopup() {
		Stage stage = new Stage(StageStyle.UTILITY);

		Scene scene = new Scene(this, 250, 400);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setTitle(descriptor.getObjectName() + "'s list");
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		this.setCloseCaller(() -> {
			stage.close();
		});
		stage.showAndWait();
	}

	@FXML
	public void initialize() throws Exception {
		data = binder.getData();

		FormBuilder builder = new FormBuilder();
		builder.buildFormLayout(descriptor, vbox);
		descriptor.bindWith(modelAccessor, data);

	}

	public void setCloseCaller(Caller call) {
		closeCaller = call;
	}

	@FXML
	public void save() throws Exception {

		if (data == null) {
			data = descriptor.getNewClassInstance();
		}
		List<Object> dependingData = new ArrayList<>();
		for (PropertyDescriptorI desc : descriptor.getCommonPropertyDescriptors()) {
			JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);

			presenter.setPropertyValue(data);
			dependingData.addAll(presenter.getDependingData());
		}

		if (descriptor.hasChildClasses()) {
			// TODO check if the data is of the good type ...
			// if not regenerate the new data type ...
			for (PropertyDescriptorI desc : descriptor.getChildPropertyDescriptors(descriptor.getActiveChildClass())) {
				JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);
				presenter.setPropertyValue(data);
				dependingData.addAll(presenter.getDependingData());
			}
		}

		// modelAccessor.save(descriptor.getObjectClass(), data);
		binder.setData(data, dependingData.toArray());
		closeCaller.call();
	}

	@FXML
	public void cancel() {
		closeCaller.call();
	}

}
