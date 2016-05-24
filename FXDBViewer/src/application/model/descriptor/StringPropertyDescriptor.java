package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.Model;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebStringPresenter;
import application.model.descriptor.presenter.JFXStringPresenter;
import application.model.descriptor.presenter.PresenterType;

public class StringPropertyDescriptor<T> extends PropertyDescriptorA<T, String> {

	

	@Override
	public void initialize(Model model, Accessor<T, String> accessor) {

		this.accessor = accessor;

		this.name = accessor.getName();		

	}

	@Override
	protected void initPresenter(PresenterType presenterType) {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXStringPresenter<T>(accessor);
				break;
			case WEB:
				presenter = new WebStringPresenter<T>(accessor);
			}

		}
	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(data);
	}
	
	@Override
	public boolean isLink() {
		return false;
	}

}
