package application.model.descriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.model.ClassLink;
import application.model.Model;
import application.model.ModelException;
import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.PresentationType;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.objectaccessor.DirectFieldAccessor;
import application.model.descriptor.objectaccessor.MethodAccessor;
import application.model.descriptor.objectaccessor.ReverseReadOnlyCollectionFieldAccessor;
import application.model.descriptor.objectaccessor.ReverseReadOnlyPlainObjectFieldAccessor;

public class ClassDescriptorFactory {

	private final Map<Class, Class<? extends PropertyDescriptorI>> formPropertyMapping;
	private final Map<Class, Class<? extends PropertyDescriptorI>> gridPropertyMapping;

	private final Model model;

	public ClassDescriptorFactory(Model model) {

		this.model = model;

		formPropertyMapping = new HashMap<>();
		formPropertyMapping.put(String.class, StringPropertyDescriptor.class);
		formPropertyMapping.put(Integer.class, IntegerPropertyDescriptor.class);
		formPropertyMapping.put(Integer.TYPE, IntegerPropertyDescriptor.class);
		formPropertyMapping.put(Double.class, DoublePropertyDescriptor.class);
		formPropertyMapping.put(Double.TYPE, DoublePropertyDescriptor.class);
		formPropertyMapping.put(Date.class, DatePropertyDescriptor.class);
		formPropertyMapping.put(Boolean.class, BooleanPropertyDescriptor.class);
		formPropertyMapping.put(Boolean.TYPE, BooleanPropertyDescriptor.class);

		gridPropertyMapping = new HashMap<>();
		gridPropertyMapping.put(String.class, StringPropertyDescriptor.class);
		gridPropertyMapping.put(Integer.class, IntegerPropertyDescriptor.class);
		gridPropertyMapping.put(Integer.TYPE, IntegerPropertyDescriptor.class);
		gridPropertyMapping.put(Double.class, DoublePropertyDescriptor.class);
		gridPropertyMapping.put(Double.TYPE, DoublePropertyDescriptor.class);
		gridPropertyMapping.put(Date.class, DatePropertyDescriptor.class);
		gridPropertyMapping.put(Boolean.class, BooleanPropertyDescriptor.class);
		gridPropertyMapping.put(Boolean.TYPE, BooleanPropertyDescriptor.class);

	}

	@SuppressWarnings("rawtypes")
	private PropertyDescriptorI getFormPropertyDescriptor(Field field) throws InstantiationException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		Class<? extends PropertyDescriptorI> propertyDescriptorClass = formPropertyMapping.get(field.getType());
		if (propertyDescriptorClass == null) {
			if (Collection.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType())) {
				propertyDescriptorClass = CollectionForFormPropertyDescriptor.class;
			} else if (Enum.class.isAssignableFrom(field.getType())) {
				propertyDescriptorClass = EnumPropertyDescriptor.class;
			} else {
				// this seems to be an object and toString() should be called
				// for the value
				// TODO create a ToStringReadOnlyDescriptor...
				propertyDescriptorClass = PlainObjectPropertyDescriptor.class;
			}
		}
		PropertyDescriptorI propertyDescriptor = getPropertyDescriptorInstance(field, propertyDescriptorClass, PresentationType.FORM);
		return propertyDescriptor;
	}

	@SuppressWarnings("rawtypes")
	private PropertyDescriptorI getFormPropertyDescriptor(Method method) throws InstantiationException, IllegalAccessException {

		if (method.getAnnotation(Calculated.class) != null) {
			Class<? extends PropertyDescriptorI> propertyDescriptorClass = formPropertyMapping.get(method.getReturnType());
			if (propertyDescriptorClass != null)
				return getPropertyDescriptorInstance(method, propertyDescriptorClass, PresentationType.FORM);
		}
		return null;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PropertyDescriptorI getFormLinkPropertyDescriptor(ClassLink link) throws IllegalAccessException, InstantiationException {
		if (link.getCardinality() != null) {
			
			PropertyDescriptorI propertyDescriptor = null;
			Accessor accessor = null;
			
			if (link.getCardinality().isLinkSingle()) {
				propertyDescriptor = new PlainObjectPropertyDescriptor<>();
				accessor = new ReverseReadOnlyPlainObjectFieldAccessor(link, model.getModelAccessor());

			} else if (link.getCardinality().isLinkMultiple()) {
				propertyDescriptor = new CollectionForFormPropertyDescriptor<>();
				accessor = new ReverseReadOnlyCollectionFieldAccessor(link, model.getModelAccessor());
			} else
				return null;
			
			propertyDescriptor.initialize(model, accessor);
			return propertyDescriptor;

		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private PropertyDescriptorI getGridPropertyDescriptor(Field field) throws InstantiationException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		Class<? extends PropertyDescriptorI> propertyDescriptorClass = gridPropertyMapping.get(field.getType());
		if (propertyDescriptorClass == null) {
			if (Collection.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType())) {
				// TODO To be implemented as button list maybe ....
				propertyDescriptorClass = CollectionForFormPropertyDescriptor.class;
			} else {
				// this seems to be an object and toString() should be called
				// for the value
				propertyDescriptorClass = PlainObjectPropertyDescriptor.class;
			}
		}
		return getPropertyDescriptorInstance(field, propertyDescriptorClass, PresentationType.GRID);
	}
	
	@SuppressWarnings("rawtypes")
	private PropertyDescriptorI getGridPropertyDescriptor(Method method) throws InstantiationException, IllegalAccessException {
		if (method.getAnnotation(Calculated.class) != null) {
			
			Class<? extends PropertyDescriptorI> propertyDescriptorClass = formPropertyMapping.get(method.getReturnType());
			if (propertyDescriptorClass != null)
				return getPropertyDescriptorInstance(method, propertyDescriptorClass, PresentationType.GRID);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PropertyDescriptorI getPropertyDescriptorInstance(Field field, Class<? extends PropertyDescriptorI> propertyDescriptorClass, PresentationType type)
			throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		PropertyDescriptorI propertyDescriptor = propertyDescriptorClass.newInstance();
		propertyDescriptor.initialize(model, new DirectFieldAccessor(field));
		return propertyDescriptor;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PropertyDescriptorI getPropertyDescriptorInstance(Method method, Class<? extends PropertyDescriptorI> propertyDescriptorClass, PresentationType type) 
			throws InstantiationException, IllegalAccessException {
	
		PropertyDescriptorI propertyDescriptor = propertyDescriptorClass.newInstance();
		propertyDescriptor.initialize(model, new MethodAccessor(method));
		return propertyDescriptor;
	}

	
	/**
	 * Return the declared class field and the other fields of the parents... 
	 * @param clazz
	 * @return
	 */
	private <T> List<Field> getClassFields(Class<T> clazz) {
		List<Field> fields = new ArrayList<>();
		if (!clazz.getSuperclass().equals(Object.class)) {
			fields.addAll(getClassFields(clazz.getSuperclass()));
		}
		for (Field field : clazz.getDeclaredFields()) {
			fields.add(field);
		}
		
		
		return fields;
	}
	
	public <T> Field[] getSingleClassFields(Class<T> clazz) {
		return clazz.getDeclaredFields();
	}

	private <T> Method[] getClassMethod(Class<T> clazz) {
		return clazz.getDeclaredMethods();
	}

	/**
	 * Returns a {@link ClassDescriptor} for a form layout
	 * 
	 * @param clazz
	 * @return
	 * @throws ModelException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> ClassDescriptor<T> getFormDescriptor(Class<T> clazz) throws ModelException, SecurityException, InstantiationException,
			IllegalAccessException, NoSuchFieldException {

		List<PropertyDescriptorI> propertyDescriptors = new ArrayList<>();

		List<Field> fields = getClassFields(clazz);

		for (Field field : fields) {
			PropertyDescriptorI formPropertyDescriptor = getFormPropertyDescriptor(field);
			if (formPropertyDescriptor != null)
				propertyDescriptors.add(formPropertyDescriptor);
			else
				throw new ModelException("Unable to get the property descriptor class for the field " + field.getName());
		}

		Method[] methods = getClassMethod(clazz);
		for (Method method : methods) {
			PropertyDescriptorI formPropertyDescriptor = getFormPropertyDescriptor(method);
			if (formPropertyDescriptor != null)
				propertyDescriptors.add(formPropertyDescriptor);
		}

		List<ClassLink> linkList = model.getReverseClassLinkList(clazz);
		for (ClassLink link : linkList) {
			PropertyDescriptorI formLinkPropertyDescriptor = getFormLinkPropertyDescriptor(link);
			if (formLinkPropertyDescriptor != null)
				propertyDescriptors.add(formLinkPropertyDescriptor);
		}

		ClassDescriptor<T> classDescriptor = new ClassDescriptor<T>(PresentationType.FORM, clazz, propertyDescriptors);

		List<Class<?>> childrenClasses = model.getChildClassesList(clazz);
		for (Class<?> cc : childrenClasses) {
			List<PropertyDescriptorI> childPropertyDescriptors =
					getFormDescriptor(cc).getCommonPropertyDescriptors();
			for (int i=childPropertyDescriptors.size()-1;i>=0;i--) {
				if (childPropertyDescriptors.get(i).getAccessor().getDeclaringClass().equals(clazz))
					childPropertyDescriptors.remove(i);
			}
			classDescriptor.addChildClass((Class<? extends T>) cc, childPropertyDescriptors);

		}

		return classDescriptor;
	}

	/**
	 * Returns a {@link ClassDescriptor} instance for a table layout.
	 * 
	 * should be refactorize since it's exactly the same code as the one for the forms {@link ClassDescriptorFactory#getFormDescriptor(Class)}.
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> ClassDescriptor<T> getGridDescriptor(Class<T> clazz) throws InstantiationException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		List<PropertyDescriptorI> propertyDescriptors = new ArrayList<>();

		List<Field> fields = getClassFields(clazz);

		for (Field field : fields) {
			PropertyDescriptorI gridPropertyDescriptor = getGridPropertyDescriptor(field);
			if (gridPropertyDescriptor != null)
				propertyDescriptors.add(gridPropertyDescriptor);

		}

		Method[] methods = getClassMethod(clazz);
		for (Method method : methods) {
			PropertyDescriptorI gridPropertyDescriptor = getGridPropertyDescriptor(method);
			if (gridPropertyDescriptor != null)
				propertyDescriptors.add(gridPropertyDescriptor);
		}
		
		
		List<ClassLink> linkList = model.getReverseClassLinkList(clazz);
		for (ClassLink link : linkList) {
			PropertyDescriptorI formLinkPropertyDescriptor = getFormLinkPropertyDescriptor(link);
			if (formLinkPropertyDescriptor != null)
				propertyDescriptors.add(formLinkPropertyDescriptor);
		}
		

		ClassDescriptor<T> classDescriptor = new ClassDescriptor<T>(PresentationType.GRID, clazz, propertyDescriptors);
		List<Class<?>> childrenClasses = model.getChildClassesList(clazz);
		for (Class<?> cc : childrenClasses) {
			List<PropertyDescriptorI> childPropertyDescriptors =
					getGridDescriptor(cc).getCommonPropertyDescriptors();
			
			for (int i=childPropertyDescriptors.size()-1;i>=0;i--) {
				if (childPropertyDescriptors.get(i).getAccessor().getDeclaringClass().equals(clazz))
					childPropertyDescriptors.remove(i);
			}
			classDescriptor.addChildClass((Class<? extends T>) cc, childPropertyDescriptors);

		}

		return classDescriptor;
	}

	/**
	 * Return a {@link ClassDescriptor} with no properties ...
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <T> ClassDescriptor<T> getListDescriptor(Class<T> clazz) {
		List<PropertyDescriptorI> propertyDescriptors = new ArrayList<>();
		return new ClassDescriptor<T>(PresentationType.LIST, clazz, propertyDescriptors);
	}

}
