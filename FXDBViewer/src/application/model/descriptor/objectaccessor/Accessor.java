package application.model.descriptor.objectaccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface Accessor<D, F> extends BaseAccessor<D,F> {

	public Class<D> getDeclaringClass();

	public F[] getEnumConstants();

	public Class<F> getType();

	public String getName();

	public <A extends Annotation> A getAnnotation(Class<A> annotation);

	/**
	 * Should not be used... only for internal access. Prefer the function
	 * getActualTypeArguments()
	 * 
	 * @return
	 */
	@Deprecated
	public Type getGenericType();

	public <S> Class<S> getActualTypeArgument();

	public boolean isReadOnly();

	/**
	 * Defines if the {@link Accessor} represent a calculated data, ie if the
	 * data behind it depends from other data and must be refreshed sometimes.
	 * 
	 * @return
	 */
	public boolean isCalculated();

	/**
	 * Returns true if this {@link Accessor} is a reverse {@link Accessor}, ie if this accessor
	 * deals with an other class than the base class specified.
	 * 
	 * @return
	 */
	public boolean isReverse();

}
