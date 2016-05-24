package application.view.components;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.utils.Caller;
import application.view.saver.DataBinder;


/**
 * Should be deprecated.
 * 
 * @author Can
 *
 * @param <T>
 */
public class ListPickerComponent<T> extends BorderPane {

	private final ClassDescriptor<T> descriptor;
	private final ModelAccessor modelAccessor;
	private final DataBinder<T> binder;
	
	@FXML
	private TextField filter;

	@FXML
	private ListView<T> listView;
	
	private Caller closeCaller;

	public ListPickerComponent(DataBinder<T> binder, ModelAccessor modelAccessor, ClassDescriptor<T> descriptor) {
		this.modelAccessor = modelAccessor;
		this.descriptor = descriptor;
		this.binder = binder;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ListPickerView.fxml"));
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
		
		Scene scene = new Scene(this, 210, 340);
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
	public void initialize() throws IOException {
		List<T> list = modelAccessor.getAll(descriptor.getObjectClass());
		
		if (descriptor.hasChildClasses()) {
			for (Class<? extends T> childClass : descriptor.getChildObjectClasses()) {
				list.addAll(modelAccessor.getAll(childClass));
			}
		}
		listView.getItems().addAll(list);
		
		int index = list.indexOf(binder.getData());
		if (index >=0) listView.getSelectionModel().select(index);
	}

	@FXML
	public void handleSelect() throws IOException {
		T data = listView.getSelectionModel().getSelectedItem();
		binder.setData(data, null);
		closeCaller.call();
	}
	
	@FXML
	public void handleClickSelect(MouseEvent e) throws IOException {
		if (e.getClickCount()>=2) {
			handleSelect();
		}
	}
	

	@FXML
	public void handleCancel() {
		closeCaller.call();
	}
	public void setCloseCaller(Caller stageCloseCaller) {
		this.closeCaller = stageCloseCaller;
		
	}

	
	
}
