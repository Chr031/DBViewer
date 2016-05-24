package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.descriptor.objectaccessor.Accessor;

public class WebDoublePresenter<T> extends WebPresenter<T,Double> {

	public WebDoublePresenter(Accessor<T,Double> fieldAccessor) {
		super(fieldAccessor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.TEXTFIELD;
	}

	@Override
	public Double getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(t, Double.parseDouble(value.getValue()));
		
	}

}
