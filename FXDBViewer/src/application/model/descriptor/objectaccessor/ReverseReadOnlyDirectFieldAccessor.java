package application.model.descriptor.objectaccessor;

import java.lang.annotation.Annotation;

import application.model.ClassLink;
import application.model.dataaccessor.ModelAccessor;

public abstract class ReverseReadOnlyDirectFieldAccessor<D, F> implements Accessor<D, F> , ReversibleAccessor<D,F> {

	protected final ClassLink link;
	protected final ModelAccessor modelAccessor;

	public ReverseReadOnlyDirectFieldAccessor(ClassLink link, ModelAccessor modelAccessor) {
		this.link = link;
		this.modelAccessor = modelAccessor;
	}

	@Override
	public void set(D dataObject, F fieldTypeObject) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		// Readonly accessor
		// No-op

	}

	@Override
	public Class<D> getDeclaringClass() {
		return (Class<D>) link.getLinkedClass();
	}

	@Override
	public F[] getEnumConstants() {
		// One should check that enums cannot be considered...
		return null;
	}

	@Override
	public String getName() {
		if (link.getReverseName() != null && link.getReverseName().length()>0) 
			return link.getReverseName();
		return link.getLinkField().getName() + "_" + link.getBaseClass().getSimpleName();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotation) {
		return link.getLinkField().getAnnotation(annotation);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public boolean isCalculated() {
		return false;
	}

	@Override
	public boolean isReverse() {
		return true;
	}
	@Override
	public BaseAccessor<F, D> createReverseAccessor() {
		return new BaseFieldAdderAccessor<D, F>(link.getLinkField());
	}
}
