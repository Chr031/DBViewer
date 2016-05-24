package application.model.descriptor.objectaccessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

import application.model.ClassLink;
import application.model.dataaccessor.ModelAccessor;
import application.model.dataaccessor.condition.FieldCondition;

public class ReverseReadOnlyPlainObjectFieldAccessor<D, F> extends ReverseReadOnlyDirectFieldAccessor<D, F>  {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(ReverseReadOnlyPlainObjectFieldAccessor.class);

	public ReverseReadOnlyPlainObjectFieldAccessor(ClassLink link, ModelAccessor modelAccessor) {
		super(link, modelAccessor);
	}

	@Override
	public F get(D dataObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		List<F> list = (List<F>) modelAccessor.get(link.getBaseClass(), new FieldCondition<>(link.getLinkField(), dataObject));
		if (list.size() > 1) {
			// this should throw an exception...
			log.error("Cardinality of " + link.getLinkField().getName() + " is not respected");

			// TODO change the exception into a ModelException or something else 
			throw new IOException("Cardinality of " + link.getLinkField().getName() + " is not respected");
		} else if (list.size() == 1)
			return list.get(0);
		else
			return null;
	}

	@Override
	public Class<F> getType() {
		return (Class<F>) link.getBaseClass();
	}

	@Override
	public Type getGenericType() {
		return null;
	}

	@Override
	public <S> Class<S> getActualTypeArgument() {
		return null;
	}

	

}
