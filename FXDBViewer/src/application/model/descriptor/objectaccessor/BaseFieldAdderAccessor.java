package application.model.descriptor.objectaccessor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import application.utils.CollectionsUtility;

final class BaseFieldAdderAccessor<D, F> implements BaseAccessor<F, D> {
	
	protected final Field field;
	
	public BaseFieldAdderAccessor(Field field) {
		this.field = field;
	}
	
	@Override
	public D get(F dataObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		// TODO : No need to implement it now but after maybe
		return null;
	}

	@Override
	public void set(F dataObject, D fieldTypeObject) throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		// two case to distinguish :
		// if it's a collection to set or if it's a plain object !
		
		field.setAccessible(true);
		if (Collection.class.isAssignableFrom(field.getType())) {
			// case of collection 
			// TODO check if a collection does not exists before
			Collection<D> col = CollectionsUtility.getCollectionInstance((Class)field.getType());
			col.add(fieldTypeObject);
			field.set(dataObject, col);
		} else {
			// plain Object 
			field.set(dataObject, fieldTypeObject);
		}
	}
}