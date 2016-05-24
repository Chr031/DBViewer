package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import application.model.Model;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebEnumPresenter;
import application.model.descriptor.presenter.JFXEnumPresenter;
import application.model.descriptor.presenter.PresenterType;

public class EnumPropertyDescriptor<T, E> extends PropertyDescriptorA<T, E> {
	@Override
	public void initialize(Model model, Accessor<T, E> accessor) {
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
				presenter = new JFXEnumPresenter<T, E>(accessor);
				break;
			case WEB:
				presenter = new WebEnumPresenter<T, E>(accessor);
			}

		}

	}

	@Override
	public boolean isLink() {
		return false;
	}

}
