package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.descriptor.objectaccessor.Accessor;

public class WebIntegerPresenter<T> extends WebPresenter<T,Integer> {

	public WebIntegerPresenter(Accessor<T, Integer> fieldAccessor) {
		super(fieldAccessor);
		
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.TEXTFIELD;
	}

	@Override
	public Integer getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(t, Integer.parseInt(value.getValue()));
	}

}
