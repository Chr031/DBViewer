package application.model.descriptor;

import java.util.Collection;

import application.model.Model;
import application.model.ModelException;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebCollectionPresenter;
import application.model.descriptor.presenter.JFXCollectionPresenter;
import application.model.descriptor.presenter.PresenterType;

public class CollectionForFormPropertyDescriptor<T, S> extends PropertyDescriptorA<T, Collection<S>> {

	
	private transient Model model;
	
	@Override
	public void initialize(Model model, Accessor<T, Collection<S>> accessor) throws InstantiationException,
			IllegalAccessException {
		
		this.model = model;
		
		this.accessor = accessor;

		this.name = accessor.getName();

	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException {
		throw new IllegalAccessException("Should not be called ");
	}

	



	@Override
	protected void initPresenter(PresenterType presenterType) throws InstantiationException, IllegalAccessException, ModelException, NoSuchFieldException, SecurityException   {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXCollectionPresenter<T,S>(model ,accessor);
				break;
			case WEB:
				presenter = new WebCollectionPresenter<T,S>(accessor);
			}
				
		} 
		
	}

	@Override
	public boolean isLink() {
		return true;
	}
}
