package application.model.descriptor.objectaccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import application.model.descriptor.annotations.Calculated;

public class MethodAccessor<D, F> implements Accessor<D, F> {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(MethodAccessor.class);
	
	private final Method method;

	/**
	 * Only method with no args should be introduced there !
	 * 
	 * @param method
	 */
	public MethodAccessor(Method method) {
		this.method = method;
	}

	@SuppressWarnings("unchecked")
	@Override
	public F get(D dataObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			return (F) method.invoke(dataObject, (Object[]) null);
		} catch (Throwable t) {
			log.error("unable to get the method value",t);
			return null;
		}
	}

	@Override
	public void set(D dataObject, F fieldTypeObject) {
		// do nothing

	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<D> getDeclaringClass() {
		return (Class<D>) method.getDeclaringClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public F[] getEnumConstants() {
		return (F[]) method.getReturnType().getEnumConstants();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<F> getType() {
		return (Class<F>) method.getReturnType();
	}

	@Override
	public String getName() {
		return method.getName();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	@Override
	public Type getGenericType() {
		return method.getGenericReturnType();
	}

	@Override
	public <S> Class<S> getActualTypeArgument() {
		Type type =  method.getGenericReturnType();
		if (type != null && type instanceof ParameterizedType)
			return (Class<S>) ((ParameterizedType)type).getActualTypeArguments()[0];
		return null;

	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	public boolean isCalculated() {
		return method.getAnnotation(Calculated.class) != null;
	}

	@Override
	public boolean isReverse() {
		return false;
	}

}
