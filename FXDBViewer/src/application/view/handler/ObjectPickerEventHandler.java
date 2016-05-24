package application.view.handler;

import java.io.IOException;




import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.view.TableAndFormService;
import application.view.components.FormPickerComponent;
import application.view.components.ListPickerComponent;
import application.view.saver.DataBinder;import application.view.saver.MainFormSaver;


public class ObjectPickerEventHandler<T> implements EventHandler<MouseEvent> {

	private final ClassDescriptor<T> formDescriptor;
	private final ClassDescriptor<T> listDescriptor;
	private final ModelAccessor modelAccessor;
	private final DataBinder<T> binder;
	private final TableAndFormService listFormController;

	public ObjectPickerEventHandler(TableAndFormService listFormController, DataBinder<T> binder, ModelAccessor modelAccessor,
			ClassDescriptor<T> listDescriptor, ClassDescriptor<T> formDescriptor) {
		this.listFormController = listFormController;
		this.binder = binder;
		this.listDescriptor = listDescriptor;
		this.formDescriptor = formDescriptor;
		this.modelAccessor = modelAccessor;
	}

	@Override
	public void handle(MouseEvent event) {

		switch (event.getButton()) {
		case PRIMARY:
			new ListPickerComponent<>(binder, modelAccessor, listDescriptor).showAsPopup();
			break;
		case SECONDARY:
			showContextMenu(event);
		default:
			break;
		}

	}

	private void showContextMenu(MouseEvent event) {

		ContextMenu context = getPickerContextMenu();
		context.show((Node) event.getSource(), Side.RIGHT, 0d, 5d);

	}

	public ContextMenu getPickerContextMenu() {
		MenuItem createObjectMI = new MenuItem("Create");
		createObjectMI.setOnAction((e) -> {

			try {
				listFormController.loadForm(new DataBinder<T>() {

					@Override
					public T getData() {
						return null;
					}

					@Override
					public void setData(T data, Object... dependingDataArray) throws IOException {
						binder.setData(data, dependingDataArray);

					}

				}, formDescriptor, modelAccessor , new MainFormSaver<T>());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*new FormPickerComponent<>(new DataBinder<T>() {

				@Override
				public T getData() {
					return null;
				}

				@Override
				public void setData(T data, Object... dependingDataArray) throws IOException {
					binder.setData(data, dependingDataArray);

				}

			}, modelAccessor, formDescriptor).showAsPopup();*/
		});
		MenuItem changeObjectMI = new MenuItem("Edit");
		changeObjectMI.setOnAction((e) -> {
			try {
				listFormController.loadForm(binder,formDescriptor, modelAccessor , new MainFormSaver<T>());
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			//new FormPickerComponent<>(binder, modelAccessor, formDescriptor).showAsPopup();
		});
		MenuItem nullObjectMI = new MenuItem("Empty");
		nullObjectMI.setOnAction((e) -> {
			try {
				binder.setData((T) null, (Object[]) null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		ContextMenu context = new ContextMenu(createObjectMI, changeObjectMI, nullObjectMI);
		return context;
	}

}