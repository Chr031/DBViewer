package application.view.saver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterType;

public class SecondaryFormSaver<T> implements FormSaver<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void save(T data, ClassDescriptor<T> descriptor, DataBinder<T> binder) throws InstantiationException, IllegalAccessException, IOException {
		if (data == null) {
			data = descriptor.getNewClassInstance();
		}
		List<Object> dependingData = new ArrayList<>();
		for (PropertyDescriptorI desc : descriptor.getCommonPropertyDescriptors()) {
			JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);

			presenter.setPropertyValue(data);
			dependingData.addAll(presenter.getDependingData());
		}

		if (descriptor.hasChildClasses()) {
			// TODO check if the data is of the good type ...
			// if not regenerate the new data type ...
			for (PropertyDescriptorI desc : descriptor.getChildPropertyDescriptors(descriptor.getActiveChildClass())) {
				JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);
				presenter.setPropertyValue(data);
				dependingData.addAll(presenter.getDependingData());
			}
		}

		// modelAccessor.save(descriptor.getObjectClass(), data);
		binder.setData(data, dependingData.toArray());
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

}
