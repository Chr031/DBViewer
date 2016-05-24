package application.model.dataaccessor.condition;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class FieldCondition<T, V> implements Condition<T> {
	protected final Field field;
	protected final V value;

	protected final Condition<T> condition;

	public FieldCondition(Field field, V value) {
		this.field = field;
		this.value = value;
		if (Collection.class.isAssignableFrom(field.getType()))
			this.condition = new CollectionCondition();
		else
			this.condition = new PlainObjectCondition();
	}

	@Override
	public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return condition.verify(t);
	}
	
	public String toString() {
		return field.getName() +" of " + value.toString();
	}

	private class PlainObjectCondition implements Condition<T> {

		@Override
		public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException {
			field.setAccessible(true);
			if (value == null)
				return field.get(t) == null;
			else
				return value.equals(field.get(t));
		}

		

	}

	private class CollectionCondition implements Condition<T> {

		@Override
		public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException {
			field.setAccessible(true);
			Collection<V> vList = (Collection<V>) field.get(t);
			if (vList == null)
				return false; // ???
			return vList.contains(value);
		}


	}

	

}
