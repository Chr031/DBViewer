package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.descriptor.objectaccessor.Accessor;

public class WebBooleanPresenter<T> extends WebPresenter<T, Boolean> {

	public WebBooleanPresenter(Accessor<T, Boolean> fieldAccessor) {
		super(fieldAccessor);

	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.BOOLEAN;
	}

	@Override
	public Boolean getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(t, Boolean.valueOf(value.getValue()));
		
	}
}
