package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.Model;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebIntegerPresenter;
import application.model.descriptor.presenter.JFXIntegerPresenter;
import application.model.descriptor.presenter.PresenterType;

public class IntegerPropertyDescriptor<T> extends PropertyDescriptorA<T, Integer> {

	@Override
	public void initialize(Model model, Accessor<T, Integer> accessor) {
		this.accessor = accessor;

		this.name = accessor.getName();

	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IOException {
		return accessor.get(data).toString();
	}

	@Override
	protected void initPresenter(PresenterType presenterType) {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXIntegerPresenter<T>(accessor);
				break;
			case WEB:
				presenter = new WebIntegerPresenter<T>(accessor);
			}

		}

	}

	@Override
	public boolean isLink() {
		return false;
	}
}
