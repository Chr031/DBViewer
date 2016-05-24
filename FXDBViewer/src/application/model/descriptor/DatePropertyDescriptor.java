package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import application.model.Model;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.WebDatePresenter;
import application.model.descriptor.presenter.JFXDatePresenter;
import application.model.descriptor.presenter.PresenterType;

public class DatePropertyDescriptor<T> extends PropertyDescriptorA<T, Date>
{

	@Override
	public void initialize(Model model, Accessor<T, Date> accessor) throws InstantiationException,
			IllegalAccessException {
		this.accessor = accessor;

		this.name = accessor.getName();

	}

	@Override
	public String getPropertyValueAsString(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		Date d = accessor.get(data);

		return d == null ? "" : d.toString();
	}

	@Override
	protected void initPresenter(PresenterType presenterType) {
		if (presenter == null) {
			switch (presenterType) {
			case JFX:
				presenter = new JFXDatePresenter<T>(accessor);
				break;
			case WEB:
				presenter = new WebDatePresenter<T>(accessor);
			}

		}

	}
	
	@Override
	public boolean isLink() {
		return false;
	}

}
