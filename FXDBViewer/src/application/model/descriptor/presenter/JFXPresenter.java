package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import application.model.ModelException;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;
import application.view.TableAndFormService;

public abstract class JFXPresenter<D, P, R extends Region> implements PresenterI<D> {

	protected R fxControl;
	protected ModelAccessor modelAccessor;
	protected TableAndFormService listFormController;
	protected final Accessor<D, P> accessor;
	

	public JFXPresenter(Accessor<D, P> fieldAccessor) {
		this.accessor = fieldAccessor;

	}

	/**
	 * Retrieve the underlying presenter content and set it into the data
	 * instance given in parameter.
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public abstract void setPropertyValue(D data) throws IllegalArgumentException, IllegalAccessException, InstantiationException;

	/**
	 * Set the Presenter content according to the data given in parameter.
	 * 
	 * @param data
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws ModelException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IOException 
	 */
	public abstract void setNodeContent(D data) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ModelException, InvocationTargetException, NoSuchFieldException, SecurityException, IOException;

	/**
	 * Test the type of the presenter
	 * 
	 * @param presenterType
	 * @return
	 */

	@Override
	public boolean isTypeOf(PresenterType presenterType) {
		return presenterType == PresenterType.JFX;
	}

	public R getFXControl() {
		
		return fxControl;
	}

	public void setFxNodePrefWidthBinding(ObservableValue<Double> observable) {
		// TODO should be defined in the children classes
		// According to the default behavior
		fxControl.prefWidthProperty().bind(observable);
	}

	@Override
	public Collection<? extends Object> getDependingData() {
		return Collections.emptyList();
	}

	public void setModelAccessor(ModelAccessor modelAccessor) {

		this.modelAccessor = modelAccessor;
	}
	
	public void setListFormController (TableAndFormService listFormController) {
		this.listFormController = listFormController;
	}

	public abstract void addChangeCallback(Callback<Void,Void> voidCallback) ;
		
	public void setResizableParentPane(Pane parentPane) {
		// no opp to override if necessary.
	}

}
