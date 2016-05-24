package application.model.descriptor.objectaccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This {@link Accessor} is designed to access directly the field passed in the constructor parameter using reflection.
 * 
 * 
 * @author Christophe
 *
 * @param <D>
 * @param <F>
 */
public class DirectFieldAccessor<D, F> implements Accessor<D, F> {

	private final Field field;

	public DirectFieldAccessor(Field field) {
		this.field = field;
	}

	protected Field getField() {
		return field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public F get(D data) throws IllegalArgumentException, IllegalAccessException {
		if (data == null)
			return null;
		field.setAccessible(true);
		return (F) field.get(data);
	}

	@Override
	public void set(D dataObject, F fieldTypeValue) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		field.setAccessible(true);
		field.set(dataObject, fieldTypeValue);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<D> getDeclaringClass() {
		return (Class<D>) getField().getDeclaringClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public F[] getEnumConstants() {
		return (F[]) getField().getType().getEnumConstants();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<F> getType() {
		return (Class<F>) getField().getType();
	}

	@Override
	public String getName() {
		return getField().getName();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return getField().getAnnotation(annotationClass);
	}

	@Override
	public Type getGenericType() {
		return getField().getGenericType();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean isCalculated() {
		return false;
	}
	@SuppressWarnings("unchecked")
	public <S> Class<S> getActualTypeArgument() {
		if (getGenericType() instanceof ParameterizedType) 
			return (Class<S>) ((ParameterizedType) getGenericType()).getActualTypeArguments()[0];
		return null;
	}

	@Override
	public boolean isReverse() {
		return false;
	}

}
