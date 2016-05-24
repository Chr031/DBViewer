package application.restserver;

import application.model.descriptor.ClassDescriptor;

public class ObjectBeanForm<T> {
	private ObjectBean<T> objectBean;
	private ClassDescriptor<T> classDescriptor;

	private ObjectBeanForm() {};
	
	public ObjectBeanForm(ObjectBean<T> objectBean, ClassDescriptor<T> classDescriptor) {
		super();
		this.objectBean = objectBean;
		this.classDescriptor = classDescriptor;
	}

	public ObjectBean<T> getObjectBean() {
		return objectBean;
	}

	public ClassDescriptor<T> getClassDescriptor() {
		return classDescriptor;
	}

}
