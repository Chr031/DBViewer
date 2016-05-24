package application.restserver;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.Model;
import application.model.dataaccessor.condition.AccessorCondition;
import application.model.dataaccessor.condition.Condition;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;

public class FilterBean<T,B> {

	private static final Logger log = LogManager.getLogger(FilterBean.class);

	public Class<B> baseClass;
	public int id;
	public String propertyName;

	public Condition<T> getCondition(Model model) throws IOException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		log.debug("Build condition with " + baseClass.getName() + " id=" + id + " on " + propertyName);
		B baseClassInstance = model.getModelAccessor().getById(baseClass, id);
		Condition<T> condition;
		ClassDescriptor<B> classDescriptor = model.getClassDescriptorFactory().getGridDescriptor(baseClass);
		for (PropertyDescriptorI pDesc : classDescriptor.getAllPropertyDescriptors()) {
			if (pDesc.getName().equals(propertyName)) {
				condition = new AccessorCondition<T, B>(pDesc.getAccessor(), baseClassInstance);
				log.debug("Condition returned: " + condition);
				return condition;
			} else {
				log.debug(propertyName + " doesn't match with " + pDesc.getName());
			}
		}
		log.debug("no property found ... condition is null" );
		return null;

	}

}
