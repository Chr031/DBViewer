package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;

public class JFXDoublePresenter<T> extends JFXPresenter<T, Double, TextField> {

	public JFXDoublePresenter(Accessor<T, Double> accessor) {
		super(accessor);
		this.fxControl = new TextField();
		if (accessor.isReadOnly()) { 
			fxControl.setEditable(false);
			fxControl.setBackground(Background.EMPTY);
			fxControl.setBorder(Border.EMPTY);
		}
	}

	@Override
	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		if (fxControl.getText() == null || fxControl.getText().trim().length() == 0)
			accessor.set(data, null);
		else
			accessor.set(data, Double.parseDouble(fxControl.getText()));

	}

	@Override
	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (data == null)
			return;
		Double d = accessor.get(data);		
		fxControl.setText(d == null ? null : d.toString());

	}
	@Override
	public void addChangeCallback(Callback<Void,Void> voidCallback) {
		fxControl.textProperty().addListener((s)-> {voidCallback.call(null);});
		
	}
}
