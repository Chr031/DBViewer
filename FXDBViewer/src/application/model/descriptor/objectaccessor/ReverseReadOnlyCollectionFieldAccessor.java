package application.model.descriptor.objectaccessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import application.model.ClassLink;
import application.model.dataaccessor.ModelAccessor;
import application.model.dataaccessor.condition.FieldCondition;

public class ReverseReadOnlyCollectionFieldAccessor<D, F> extends ReverseReadOnlyDirectFieldAccessor<D, Collection<F>> {

	public ReverseReadOnlyCollectionFieldAccessor(ClassLink link, ModelAccessor modelAccessor) {
		super(link, modelAccessor);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<F> get(D dataObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		List<F> list = (List<F>) modelAccessor.get(link.getBaseClass(), new FieldCondition<>(link.getLinkField(), dataObject));
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class<Collection<F>> getType() {
		// Funny work around
		return (Class) ((List<F>) Collections.emptyList()).getClass();
	}

	@Override
	public Type getGenericType() {
		// since deprecated;
		return null;
	}

	@Override
	public <S> Class<S> getActualTypeArgument() {
		return (Class<S>) link.getBaseClass();
	}

}
