package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;

public class JFXDatePresenter<T> extends JFXPresenter<T, Date, DatePicker> {

	public JFXDatePresenter(Accessor<T, Date> fieldAccessor) {
		super(fieldAccessor);
		this.fxControl = new DatePicker();
	}

	@Override
	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		LocalDate ldate = fxControl.getValue();	
		
		if (ldate == null)
			return;
		
		@SuppressWarnings("deprecation")
		Date d = new Date(ldate.getYear() - 1900, ldate.getMonthValue() - 1, ldate.getDayOfMonth());
		accessor.set(data, d);
	}

	@Override
	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (data == null)
			return;
		Date d = accessor.get(data);
		if (d==null) return ;
		@SuppressWarnings("deprecation")
		LocalDate ldate = LocalDate.of(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
		fxControl.setValue(ldate);
	}

	@Override
	public void setFxNodePrefWidthBinding(ObservableValue<Double> observable) {
		// do nothing ... keep the current size

	}

	@Override
	public void addChangeCallback(Callback<Void,Void> voidCallback) {
		fxControl.valueProperty().addListener((s)-> {voidCallback.call(null);});
		
	}
	
}
