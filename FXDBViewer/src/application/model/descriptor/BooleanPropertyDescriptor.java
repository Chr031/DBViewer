package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.Model;
import application.model.ModelException;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebBooleanPresenter;
import application.model.descriptor.presenter.JFXBooleanPresenter;
import application.model.descriptor.presenter.PresenterType;

public class BooleanPropertyDescriptor<T> extends PropertyDescriptorA<T, Boolean> {

	@Override
	public void initialize(Model model, Accessor<T, Boolean> accessor)
			throws InstantiationException, IllegalAccessException {
		this.accessor = accessor;

		this.name = accessor.getName();

	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(data).toString();
	}

	@Override
	protected void initPresenter(PresenterType presenterType) throws InstantiationException, IllegalAccessException, ModelException {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXBooleanPresenter<T>(accessor);
				break;
			case WEB:
				presenter = new WebBooleanPresenter<T>(accessor);
			}
				
		}
		
	}

	@Override
	public boolean isLink() {
		return false;
	}

}
