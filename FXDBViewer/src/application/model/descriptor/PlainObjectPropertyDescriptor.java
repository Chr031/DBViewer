package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.Model;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebButtonPresenter;
import application.model.descriptor.presenter.JFXButtonPresenter;
import application.model.descriptor.presenter.PresenterType;

public class PlainObjectPropertyDescriptor<T, F> extends PropertyDescriptorA<T, F> {

	private ModelAccessor modelAccessor;
	private transient Model model;

	@Override
	public void initialize(Model model, Accessor<T, F> accessor) {
		this.model = model;
		this.accessor = accessor;
		this.name = accessor.getName();

	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		F subData = accessor.get(data);
		return subData != null ? subData.toString() : null;
	}

	
	@Override
	protected void initPresenter(PresenterType presenterType) {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXButtonPresenter<T, F>(model,accessor, presenterType);
				break;
			case WEB:
				presenter = new WebButtonPresenter<T,F>(accessor, modelAccessor, presenterType);
			}
				
		} 

	}
	
	public boolean isLink() { return true;}

}
