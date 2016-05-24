package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import application.model.Model;
import application.model.ModelException;
import application.model.descriptor.ClassDescriptorFactory;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;
import application.view.handler.ObjectPickerEventHandler;
import application.view.saver.DataBinder;

public class JFXButtonPresenter<D, P> extends JFXPresenter<D, P, Button> {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(JFXButtonPresenter.class);
	
	private ObjectProperty<P> subDataProperty;
	private List<Object> dependingData;

	private transient Model model;

	public JFXButtonPresenter(Model model, Accessor<D, P> fieldAccessor, PresenterType presenterType) {
		super(fieldAccessor);
		this.model = model;
		this.fxControl = new Button("Pick");
		subDataProperty = new SimpleObjectProperty<P>();
		this.dependingData = new ArrayList<>();
		fxControl.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return subDataProperty.get() == null ? "pick" : subDataProperty.get().toString();
			}
		}, subDataProperty));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setNodeContent(final D data) throws IllegalArgumentException, IllegalAccessException, InstantiationException,
			ModelException, InvocationTargetException, NoSuchFieldException, SecurityException, IOException {

		final ClassDescriptorFactory classDescriptorfactory = model.getClassDescriptorFactory();
		DataBinder<P> binder = new DataBinder<P>() {

			@Override
			public P getData() {
				try {
					if (data == null)
						return null;
					return accessor.get(data);
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
					log.error("Unable to get the underlying data", e);
					return null;
				}
			}

			@Override
			public void setData(P subData, Object... dependingData) {

				JFXButtonPresenter.this.subDataProperty.set(subData);
				JFXButtonPresenter.this.clearDepending();
				JFXButtonPresenter.this.addDependingData(subData);
				if (dependingData != null)
					JFXButtonPresenter.this.addDependingData(dependingData);
				

			}

		};

		fxControl.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new ObjectPickerEventHandler(listFormController, binder, modelAccessor,
						classDescriptorfactory.getListDescriptor(accessor.getType())
						, classDescriptorfactory.getFormDescriptor(accessor.getType()))
				);

		if (data != null) {
			subDataProperty.set(accessor.get(data));

		}
	}

	protected void clearDepending() {
		this.dependingData.clear();

	}

	protected void addDependingData(Object... dependingData) {
		for (Object d : dependingData) {
			this.dependingData.add(d);
		}

	}

	@Override
	public void setPropertyValue(D data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(data, subDataProperty.get());

	}

	@Override
	public Collection<? extends Object> getDependingData() {
		return dependingData == null ? Collections.emptyList() : dependingData;
	}

	@Override
	public void setFxNodePrefWidthBinding(ObservableValue<Double> observable) {
		// don't resize buttons
	}
	
	@Override
	public void addChangeCallback(Callback<Void,Void> voidCallback) {
		fxControl.textProperty().addListener((s)-> {voidCallback.call(null);});
		
	}

}
