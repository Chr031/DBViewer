package application.model.descriptor;

import application.model.ModelException;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.PresenterI;
import application.model.descriptor.presenter.PresenterType;

public abstract class PropertyDescriptorA<T, F> implements PropertyDescriptorI<T, F>, Cloneable {

	protected Accessor<T, F> accessor;
	protected String name;
	protected PresenterI<T> presenter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see application.model.descriptor.PropertyDescriptorI#getField()
	 */
	@Override
	public Accessor<T, F> getAccessor() {
		return accessor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see application.model.descriptor.PropertyDescriptorI#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	protected abstract void initPresenter(PresenterType presenterType) throws InstantiationException, IllegalAccessException,
			ModelException, NoSuchFieldException, SecurityException;

	@Override
	public <P extends PresenterI<T>> P getPresenter(PresenterType presenterType) {
		try {
			if (presenter == null) {
				initPresenter(presenterType);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!presenter.isTypeOf(presenterType))
			throw new RuntimeException("Bad presenter initialization");

		return (P) presenter;
	}

	@Override
	public void setModelAccessor(ModelAccessor modelAccessor) {
		presenter.setModelAccessor(modelAccessor);

	}

	@Override
	public boolean isCalculated() {
		return accessor.isCalculated();
	};

	public PropertyDescriptorA<T, F> clone() throws CloneNotSupportedException {
		PropertyDescriptorA<T,F> clone = (PropertyDescriptorA<T,F>)super.clone();
		clone.accessor = this.accessor;
		clone.name = this.name;
		clone.presenter = null;		
		
		return clone;		
		
	}
}
