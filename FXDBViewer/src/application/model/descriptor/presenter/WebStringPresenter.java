package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.descriptor.annotations.PresentationType;
import application.model.descriptor.annotations.TextArea;
import application.model.descriptor.objectaccessor.Accessor;

public class WebStringPresenter<T> extends WebPresenter<T, String> {

	private PropertyTemplate templateType;
	 
	
	public WebStringPresenter(Accessor<T, String> accessor) {
		super(accessor);
		
		if (( accessor.getAnnotation(TextArea.class)) != null) {
			templateType = PropertyTemplate.TEXTAREA;
			
		} else {
			templateType = PropertyTemplate.TEXTFIELD;
		}
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return templateType;
	}

	@Override
	public String getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		accessor.set(t, value.getValue());
		
	}

}
