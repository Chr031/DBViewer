package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.CheckBox;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;

public class JFXBooleanPresenter<T> extends JFXPresenter<T, Boolean, CheckBox> {

	public JFXBooleanPresenter(Accessor<T, Boolean> accessor) {
		super(accessor);
		this.fxControl = new CheckBox();

	}

	@Override
	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(data, fxControl.isSelected());
	}

	@Override
	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (data == null)
			fxControl.setSelected(false);
		Boolean value = accessor.get(data);
		
			fxControl.setSelected(value==null ? false:value);

	}
	
	@Override
	public void addChangeCallback(Callback<Void,Void> voidCallback) {
		fxControl.selectedProperty().addListener((s)-> {voidCallback.call(null);});
		
	}

}
