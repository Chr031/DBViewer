package application.restserver;

import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.WebPresenter;
import application.model.descriptor.presenter.LinkedValue;
import application.model.descriptor.presenter.PresenterType;
import application.model.descriptor.presenter.WebPresenter.PropertyTemplate;

public class ObjectBeanFactory {

	public <T> ObjectBean<T> toObjectBean(T t, ClassDescriptor<T> classDescriptor) throws Exception {
		ObjectBean<T> bean = new ObjectBean<T>(t.hashCode(), classDescriptor.getObjectClass());

		for (PropertyDescriptorI<T, ?> propertyDescriptor : classDescriptor.getCommonPropertyDescriptors()) {
			String name = propertyDescriptor.getName();
			// String value = propertyDescriptor.getPropertyValueAsString(t);
			WebPresenter<T, ?> presenter = propertyDescriptor.getPresenter(PresenterType.WEB);
			if (presenter == null) {
				throw new Exception("No presenter defined");
			}
			PropertyTemplate templateType = presenter.getTemplateType();		
			String options[] = presenter.getTemplateOptions();
			LinkedValue value = presenter.getLinkedValue(t);

			PropertyBean pBean = new PropertyBean(name, value, templateType.toString(), options);
			bean.addPropertyBean(pBean);
		}
		// todo manage exceptions
		bean.addPropertyBean(new PropertyBean("toString", new LinkedValue<>(t.toString()),""));

		return bean;
	}

	/**
	 * 
	 * @param t
	 * @param bean
	 * @param classDescriptor
	 * @return
	 * @throws Exception 
	 */
	public <T> void fromObjectBean(T t, ObjectBean<T> bean, ClassDescriptor<T> classDescriptor) throws Exception {

		for (PropertyDescriptorI<T, ?> pDesc : classDescriptor.getCommonPropertyDescriptors()) {
			WebPresenter<T, ?> presenter = pDesc.getPresenter(PresenterType.WEB);
			PropertyBean property = bean.getPropertyBeans().get(pDesc.getName());
			presenter.setLinkedValue(t, property.getValue());
		}

	}

}
