package application.model.descriptor.objectaccessor;

public interface ReversibleAccessor<D,F> {

	BaseAccessor<F,D> createReverseAccessor();
	
}
