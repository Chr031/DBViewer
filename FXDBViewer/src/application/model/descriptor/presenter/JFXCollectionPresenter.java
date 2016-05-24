package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import application.model.Model;
import application.model.ModelException;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;
import application.utils.CollectionsUtility;
import application.view.TableAndFormService;
import application.view.components.TableFormComponent;

public class JFXCollectionPresenter<T, S> extends JFXPresenter<T, Collection<S>, TableFormComponent<S, T>> {

	private T lastDataSet;

	public JFXCollectionPresenter(Model model, Accessor<T, Collection<S>> accessor) throws InstantiationException, IllegalAccessException,
			ModelException, NoSuchFieldException, SecurityException {
		super(accessor);

		Class<S> refClass = accessor.getActualTypeArgument();
		this.fxControl = new TableFormComponent<>(
				(ClassDescriptor<S>) model.getClassDescriptorFactory().getGridDescriptor(refClass),
				(ClassDescriptor<S>) model.getClassDescriptorFactory().getFormDescriptor(refClass),
				this);

	}

	@Override
	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		if (accessor.isReadOnly())
			return;
		Collection<S> collection = fxControl.getCollection();
		Collection<S> targetCollection = CollectionsUtility.getCollectionInstance((Class<Collection<S>>) accessor.getType());
		targetCollection.addAll(collection);
		accessor.set(data, targetCollection);
	}

	@Override
	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, IOException {
		this.lastDataSet = data;
		if (data != null) {
			Collection<S> collection = accessor.get(data);
			fxControl.setCollection(collection);
		}

	}

	@Override
	public void setModelAccessor(ModelAccessor modelAccessor) {
		super.setModelAccessor(modelAccessor);
		fxControl.setModelAccessor(modelAccessor);
	}

	@Override
	public void setListFormController(TableAndFormService listFormController) {
		fxControl.setListFormController(listFormController);
	}

	@Override
	public Collection<? extends Object> getDependingData() {
		return fxControl.getDependingData();
	}

	@Override
	public void addChangeCallback(Callback<Void, Void> voidCallback) {
		fxControl.table.getItems().addListener((ListChangeListener<S>) c -> voidCallback.call(null));

	}

	@Override
	public void setResizableParentPane(Pane parentPane) {
		fxControl.setResizableParentPane(parentPane);
	}

	public Accessor<T, Collection<S>> getAccessor() {
		return accessor;
	}

	public T getLastDataSet() {
		return lastDataSet;

	}

}
