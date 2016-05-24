package application.model.dataaccessor.condition;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import application.model.descriptor.objectaccessor.Accessor;


/**
 * Need explanations :
 * <br> verify(t) will check this thing :
 * <li>t == accessor.get(value)</li> 
 * <li>or t in accessor.get(value)</li>  
 * 
 * 
 * @author Can
 *
 * @param <T>
 * @param <V>
 */
public class AccessorCondition<T, V> implements Condition<T> {

	private Condition<T> condition;
	private final V value;

	private final String name; 
	
	
	public AccessorCondition(Accessor<V, ?> accessor, V value) {
		this.value = value;
		if (Collection.class.isAssignableFrom(accessor.getType())) {
			condition = new CollectionAccessorCondition((Accessor<V, Collection<T>>) accessor);
		} else {
			condition = new SimpleAccessorCondition((Accessor<V, T>) accessor);
		}
		
		name = accessor.getName() + " of " + value;
	}

	@Override
	public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return condition.verify(t);
	}
	
	public String toString() {
		return name;
	}

	private class CollectionAccessorCondition implements Condition<T> {
		private final Accessor<V, Collection<T>> accessor;

		public CollectionAccessorCondition(Accessor<V, Collection<T>> accessor) {
			this.accessor = accessor;
		}

		@Override
		public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {

			Collection<T> tList = accessor.get(value);
			if (tList == null)
				return false; // ???
			return tList.contains(t);
		}

	}

	private class SimpleAccessorCondition implements Condition<T> {
		private final Accessor<V, T> accessor;

		public SimpleAccessorCondition(Accessor<V,T> accessor) {
			this.accessor = accessor;
		}

		@Override
		public boolean verify(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
			if (value == null)
				return accessor.get(value) == null;
			else
				return t.equals(accessor.get(value));
		}

	}

}
