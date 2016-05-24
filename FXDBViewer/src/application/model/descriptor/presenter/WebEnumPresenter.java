package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.descriptor.objectaccessor.Accessor;

public class WebEnumPresenter<T, E> extends WebPresenter<T,E> {

	public WebEnumPresenter(Accessor<T, E> accessor) {
		super(accessor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PropertyTemplate getTemplateType() {
		
		
		return PropertyTemplate.ENUM ;
	}
	
	
	public String[] getTemplateOptions() {
		final E[] enums = (E[])accessor.getEnumConstants();
		String [] options = new String[enums.length];
		for (int i = 0;i<enums.length;i++) {
			options[i] = enums[i].toString();
		}
		return options;
	}

	@Override
	public E getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, IOException, Exception {
		E e = (E)Enum.valueOf((Class)(accessor.getType()), value.getValue());
		accessor.set(t, e);
		
	}



}
