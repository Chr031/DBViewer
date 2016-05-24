package application.view.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterType;
import application.view.handler.Getter;

public class FormBuilder {

	private static final Logger log = LogManager.getLogger(FormBuilder.class);

	public <T> ObjectProperty<Class<? extends T>> buildFormLayout(ClassDescriptor<T> descriptor, Pane parentPane) {

		HBox titleBox = new HBox(10);

		Text text = new Text(descriptor.getObjectName());
		text.getStyleClass().add("title");
		titleBox.getChildren().add(text);

		parentPane.getChildren().add(titleBox);

		int labelPrefWidth = 110;
		for (PropertyDescriptorI desc : descriptor.getCommonPropertyDescriptors()) {
			buildPropertyHBox(parentPane, desc, labelPrefWidth);
		}

		if (!descriptor.hasChildClasses())
			return null;

		/**
		 * For child classes only
		 */
		final ComboBox<Class<? extends T>> childClassesCombo = buildChildrenClassCombo(descriptor);
		titleBox.getChildren().add(childClassesCombo);

	
		for (Class<? extends T> childClass : descriptor.getChildObjectClasses()) {

			for (PropertyDescriptorI pDesc : descriptor.getChildPropertyDescriptors(childClass)) {
				HBox hbox = buildPropertyHBox(parentPane, pDesc, labelPrefWidth);

				BooleanBinding booleanBinding = Bindings.createBooleanBinding(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return childClass.equals(childClassesCombo.valueProperty().get());
					}
				}, childClassesCombo.valueProperty());

				hbox.visibleProperty().bind(booleanBinding);
				hbox.managedProperty().bind(booleanBinding);
			}
		}

		return childClassesCombo.valueProperty();

	}

	private <T> ComboBox<Class<? extends T>> buildChildrenClassCombo(ClassDescriptor<T> descriptor) {
		ObservableList<Class<? extends T>> classes = FXCollections.observableArrayList(descriptor.getChildObjectClasses());

		ComboBox<Class<? extends T>> childrenClassCombo = new ComboBox<>(classes);
		childrenClassCombo.setEditable(false);
		childrenClassCombo.setConverter(new StringConverter<Class<? extends T>>() {

			@Override
			public String toString(Class<? extends T> clazz) {
				return clazz.getSimpleName();
			}

			@Override
			public Class<? extends T> fromString(String className) {
				// should not be called
				return null;
			}
		});
		return childrenClassCombo;
	}

	/**
	 * Generate a hbox
	 * 
	 * @param propertyDescriptor
	 * @param labelPrefWidth
	 * @param parentPane
	 */
	private HBox buildPropertyHBox(Pane parentPane, PropertyDescriptorI propertyDescriptor, int labelPrefWidth) {
		HBox hbox = new HBox(10);
		Label label = new Label(propertyDescriptor.getName());
		// label.setPrefWidth(labelPrefWidth);
		label.setMinWidth(labelPrefWidth);
		hbox.getChildren().add(label);
		JFXPresenter presenter = (JFXPresenter) propertyDescriptor.getPresenter(PresenterType.JFX);
		hbox.getChildren().add(presenter.getFXControl());
		presenter.setFxNodePrefWidthBinding(parentPane.prefWidthProperty().subtract(labelPrefWidth + 10));
		presenter.setResizableParentPane(hbox);
		parentPane.getChildren().add(hbox);
		return hbox;
	}

	/**
	 * Helper method that allow a component to be resized.
	 * 
	 * @param component
	 * @param resizableParentPane
	 */
	public void initSizeChangeListener(final Node component, Getter<Pane> resizableParentPane) {
		final double[] startDrag = new double[2];
		// final DoubleProperty dragHeight = new SimpleDoubleProperty();

		component.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			startDrag[0] = e.getY();
			startDrag[1] = resizableParentPane.get().getPrefHeight() == -1 ? resizableParentPane.get().getHeight() : resizableParentPane
					.get().getPrefHeight();
			log.debug(component.isResizable() + " " + startDrag[0] + " " + startDrag[1]);

		});
		component.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
			component.getScene().setCursor(Cursor.V_RESIZE);
			// table.setPrefHeight(startDrag[1] + e.getY() - startDrag[0] );
				resizableParentPane.get().setPrefHeight(startDrag[1] + e.getY() - startDrag[0]);
				log.debug(startDrag[1] + e.getY() - startDrag[0]);
			});
		component.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
			component.getScene().setCursor(Cursor.DEFAULT);
		});

	}

}
