package application.model.descriptor.objectaccessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface BaseAccessor<D,F> {
	

	public F get(D dataObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;

	public void set(D dataObject, F fieldTypeObject) throws IllegalArgumentException, IllegalAccessException, InstantiationException;

	
}
