package application.view.saver;

import java.io.IOException;

import application.model.descriptor.ClassDescriptor;

public interface FormSaver<T> {
	void save(T data, ClassDescriptor<T> descriptor, DataBinder<T> binder) throws InstantiationException, IllegalAccessException, IOException;
	void cancel();
}