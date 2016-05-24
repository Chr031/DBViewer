package application.model.descriptor.presenter;

import java.util.Collection;

import application.model.dataaccessor.ModelAccessor;

public interface PresenterI<T> {
	
	boolean isTypeOf(PresenterType presenterType);

	Collection<? extends Object> getDependingData();

	void setModelAccessor(ModelAccessor modelAccessor);

}
