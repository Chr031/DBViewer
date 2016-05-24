package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;

public class JFXEnumPresenter<T,E> extends JFXPresenter<T, E, ComboBox<E>> {

	public JFXEnumPresenter(Accessor<T, E> accessor) {
		super(accessor);
		
		E[] enums= (E[])accessor.getEnumConstants();
		this.fxControl = new ComboBox<E>();
		for (E e : enums) {
			this.fxControl.getItems().add(e);			
		}
		
	}

	@Override
	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		final E e =  this.fxControl.getSelectionModel().getSelectedItem();
		accessor.set(data, e);		
	}

	@Override
	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		this.fxControl.getSelectionModel().select(accessor.get(data));
		
	}
	
	@Override
	public void setFxNodePrefWidthBinding(ObservableValue<Double> observable) {
		// don't resize combos...
	}
	
	@Override
	public void addChangeCallback(Callback<Void,Void> voidCallback) {
		fxControl.selectionModelProperty().addListener((s)-> {voidCallback.call(null);});
		
	}

}
