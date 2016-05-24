package application.model.dataaccessor.condition;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Condition<T> {

	boolean verify(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;	
	
}
