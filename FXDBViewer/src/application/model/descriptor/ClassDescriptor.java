package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.model.ModelException;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.annotations.PresentationType;
import application.model.descriptor.objectaccessor.ReverseDirectFieldAccessor;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterCallBack;
import application.model.descriptor.presenter.PresenterType;

/**
 * This class does a lot of things event it has no intelligence ...
 * 
 * 
 * @author Can
 *
 * @param <T>
 */
public class ClassDescriptor<T> {

	private PresentationType presentationType;

	private Class<T> objectClass;

	/**
	 * The simpleName of the underlying class.
	 */
	private String objectName;

	private Class<? extends T> activeChildClass;

	private List<PropertyDescriptorI> commonPropertyDescriptors;

	private Map<Class<? extends T>, List<PropertyDescriptorI>> childrenPropertyDescriptorsMap;

	private ClassDescriptor() {
		// Only used for serialization with rest webserver.
		// fields are not final to because of that ...
		// TODO change the JSON library ...

	}

	/**
	 * Single constructor with no children classes.
	 * 
	 * @param presentationType
	 * @param objectClass
	 * @param propertyDescriptors
	 */
	public ClassDescriptor(PresentationType presentationType, Class<T> objectClass, List<PropertyDescriptorI> propertyDescriptors) {
		this.presentationType = presentationType;
		this.objectClass = objectClass;
		this.objectName = objectClass.getSimpleName();
		this.commonPropertyDescriptors = propertyDescriptors;
	}

	public void addChildClass(Class<? extends T> childClass, List<PropertyDescriptorI> propertyDescriptors) {
		if (childrenPropertyDescriptorsMap == null) {
			childrenPropertyDescriptorsMap = new HashMap<>();

		}

		childrenPropertyDescriptorsMap.put(childClass, propertyDescriptors);

	}

	public boolean hasChildClasses() {
		return childrenPropertyDescriptorsMap != null;
	}

	public PresentationType getPresentationType() {
		return presentationType;
	}

	public Class<? extends T> getActiveChildClass() {
		return activeChildClass;
	}

	public void setActiveChildClass(Class<? extends T> activeChildClass) {
		this.activeChildClass = activeChildClass;
	}

	public Class<T> getObjectClass() {
		return objectClass;
	}

	public Set<Class<? extends T>> getChildObjectClasses() {

		return childrenPropertyDescriptorsMap == null ? null : childrenPropertyDescriptorsMap.keySet();
	}

	/**
	 * The simple name of the underlying object class.
	 * 
	 * @return
	 */
	public String getObjectName() {
		return objectName;
	}

	public List<PropertyDescriptorI> getCommonPropertyDescriptors() {
		return commonPropertyDescriptors;
	}

	public List<PropertyDescriptorI> getChildPropertyDescriptors(Class<? extends T> childClass) {
		return childrenPropertyDescriptorsMap.get(childClass);
	}

	@Deprecated
	public void setModelAccessor(ModelAccessor modelAccessor) {
		for (PropertyDescriptorI pDesc : commonPropertyDescriptors) {
			pDesc.setModelAccessor(modelAccessor);
		}
	}

	/**
	 * Why is it deprecated !!!
	 */
	@Deprecated
	public void initPresenters(PresenterType presenterType) {
		for (PropertyDescriptorI pdesc : commonPropertyDescriptors) {
			pdesc.getPresenter(presenterType);
		}
	}

	public T getNewClassInstance() throws InstantiationException, IllegalAccessException {
		if (hasChildClasses() && activeChildClass != null)
			return activeChildClass.newInstance();
		else
			return objectClass.newInstance();
	}

	/**
	 * Injects some references into the presenters. There should be a better way
	 * to do that ... maybe with a foreach presenter and a callback on the
	 * presenter type.
	 * 
	 * @param modelAccessor
	 * @param data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ModelException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public void bindWith(ModelAccessor modelAccessor, T data) throws IllegalArgumentException, IllegalAccessException,
			InstantiationException, ModelException, InvocationTargetException, NoSuchFieldException, SecurityException, IOException {
		for (PropertyDescriptorI desc : getCommonPropertyDescriptors()) {
			JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);
			presenter.setModelAccessor(modelAccessor);
			presenter.setNodeContent(data);
		}
		if (hasChildClasses() && data != null) {
			for (PropertyDescriptorI desc : getChildPropertyDescriptors((Class<? extends T>) data.getClass())) {
				JFXPresenter presenter = (JFXPresenter) desc.getPresenter(PresenterType.JFX);
				presenter.setModelAccessor(modelAccessor);
				presenter.setNodeContent(data);
			}
		}
	}

	/**
	 * Replace {@link #bindWith(ModelAccessor, Object)} function
	 * 
	 * @param presenterType
	 * @param callback
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void foreachPresenter(PresenterType presenterType, PresenterCallBack<T> callback) throws Exception {
		for (PropertyDescriptorI desc : getCommonPropertyDescriptors()) {
			callback.call(desc.getPresenter(presenterType), objectClass);
		}
		if (hasChildClasses()) {
			for (Class<? extends T> childClass : childrenPropertyDescriptorsMap.keySet()) {
				for (PropertyDescriptorI desc : getChildPropertyDescriptors(childClass)) {
					callback.call(desc.getPresenter(presenterType), childClass);
				}
			}
		}
	}

	/**
	 * XXX Lookup method : Should be optimized !!!
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public PropertyDescriptorI getPropertyDescriptor(String propertyName) {
		for (PropertyDescriptorI pDesc : commonPropertyDescriptors) {
			if (pDesc.getName().equals(propertyName))
				return pDesc;
		}
		if (hasChildClasses()) {
			for (List<PropertyDescriptorI> pDescList : childrenPropertyDescriptorsMap.values()) {
				for (PropertyDescriptorI pDesc : pDescList) {
					if (pDesc.getName().equals(propertyName))
						return pDesc;
				}
			}
		}
		return null;
	}

	private Boolean calculatedProperties = null;

	public synchronized boolean hasCalculatedProperties() {
		if (calculatedProperties == null) {

			for (PropertyDescriptorI pDesc : commonPropertyDescriptors) {
				if (pDesc.isCalculated()) {
					calculatedProperties = true;
					break;
				}
			}
			if (hasChildClasses()) {
				for (List<PropertyDescriptorI> pDescList : childrenPropertyDescriptorsMap.values()) {
					for (PropertyDescriptorI pDesc : pDescList) {
						if (pDesc.isCalculated()) {
							calculatedProperties = true;
							break;
						}
					}
				}
			}
			if (calculatedProperties == null)
				calculatedProperties = false;

		}
		return calculatedProperties;
	}

	public List<PropertyDescriptorI> getAllPropertyDescriptors() {

		List<PropertyDescriptorI> allPropertyDescriptors = new ArrayList<>();
		allPropertyDescriptors.addAll(commonPropertyDescriptors);
		if (hasChildClasses()) {
			for (List<PropertyDescriptorI> pDescList : childrenPropertyDescriptorsMap.values()) {
				allPropertyDescriptors.addAll(pDescList);
			}
		}
		return allPropertyDescriptors;

	}

	public List<PropertyDescriptorI> getCalculatedProperties() {
		List<PropertyDescriptorI> calculatedProperties = new ArrayList<>();
		for (PropertyDescriptorI pdesc : getAllPropertyDescriptors()) {
			if (pdesc.isCalculated())
				calculatedProperties.add(pdesc);
		}

		return calculatedProperties;
	}

	@Deprecated
	public void activateReverseFieldSetters(boolean activate) {
		for (PropertyDescriptorI pDesc : commonPropertyDescriptors) {
			if (pDesc.getAccessor() instanceof ReverseDirectFieldAccessor) {
				((ReverseDirectFieldAccessor) pDesc.getAccessor()).setActivateReverseAccessor(activate);
			}
		}
		if (hasChildClasses()) {
			for (List<PropertyDescriptorI> pDescList : childrenPropertyDescriptorsMap.values()) {
				for (PropertyDescriptorI pDesc : pDescList) {
					if (pDesc.getAccessor() instanceof ReverseDirectFieldAccessor) {
						((ReverseDirectFieldAccessor) pDesc.getAccessor()).setActivateReverseAccessor(activate);
					}
				}
			}
		}
	}

	@Override
	public ClassDescriptor<T> clone() throws CloneNotSupportedException {

		ClassDescriptor<T> clone = new ClassDescriptor<>();
		clone.activeChildClass = null;
		clone.calculatedProperties = null;
		clone.presentationType = this.presentationType;
		clone.objectName = this.objectName;
		clone.objectClass = this.objectClass;

		clone.commonPropertyDescriptors = new ArrayList<>();
		for (PropertyDescriptorI pDesc : this.commonPropertyDescriptors) {
			clone.commonPropertyDescriptors.add(((PropertyDescriptorA) pDesc).clone());
		}

		if (childrenPropertyDescriptorsMap == null)
			clone.childrenPropertyDescriptorsMap = null;
		else {
			clone.childrenPropertyDescriptorsMap = new HashMap<>();
			for (Class<? extends T> childClass : this.childrenPropertyDescriptorsMap.keySet()) {
				List<PropertyDescriptorI> pDescList = new ArrayList<>();
				clone.childrenPropertyDescriptorsMap.put(childClass, pDescList);
				for (PropertyDescriptorI pDesc : this.childrenPropertyDescriptorsMap.get(childClass)) {
					pDescList.add(((PropertyDescriptorA) pDesc).clone());
				}
			}
		}
		return clone;
	}

}