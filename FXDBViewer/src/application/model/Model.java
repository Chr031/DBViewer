package application.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.dataaccessor.FileModelAccessor;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.ClassDescriptorFactory;
import application.utils.ClassFinder;

public class Model {
	private static final Logger log = LogManager.getLogger(Model.class);
	/**
	 * The name of the model
	 */
	private final String modelName;
	
	
	/**
	 * The underlying class loader ....
	 */
	private final ClassLoader classLoader;

	/**
	 * The list of all the classes present in the model
	 */
	private final List<Class<?>> objectClassList;

	/**
	 * The {@link ModelAccessor} instance responsible of the data access on the
	 * persistent layer
	 */
	private final ModelAccessor modelAccessor;

	/**
	 * the list of all the child classes according to one class
	 */
	private final Map<Class<?>, List<Class<?>>> childClassesMap;

	/**
	 * The list of all the links according to one class
	 */
	private final Map<Class<?>, List<ClassLink>> classLinkMap;

	/**
	 * An instance of a {@link ClassDescriptorFactory}
	 */
	private final ClassDescriptorFactory classDescriptorFactory;

	/**
	 * Creates a model from a package list... Adds all the classes present in
	 * the packages inside this model.
	 * 
	 * @param modelName
	 * @param packageNames
	 * @throws Exception
	 */
	protected Model(String modelName, ClassLoader classLoader, List<Class<?> > classes) throws Exception {

		log.info("Load model " + modelName + " from classes " + Arrays.toString(classes.toArray()));

		this.modelName = modelName;
		this.classLoader = classLoader;
		objectClassList = new ArrayList<>();
		childClassesMap = new HashMap<>();
		classLinkMap = new HashMap<>();
		for (Class<?> objectClass : classes) {			
			addClassObject(objectClass);			
		}
		modelAccessor = new FileModelAccessor(modelName, classLoader);

		this.classDescriptorFactory = new ClassDescriptorFactory(this);

		processHeritanceList();
		processLinkedClasses();
	}

	private void addClassObject(Class<?> classToAdd) {
		objectClassList.add(classToAdd);
	}

	private void processHeritanceList() {
		for (Class<?> c : objectClassList) {
			for (Class<?> h : objectClassList) {
				if (c.isAssignableFrom(h) && !c.equals(h)) {
					List<Class<?>> hList = childClassesMap.get(c);
					if (hList == null) {
						hList = new ArrayList<>();
						childClassesMap.put(c, hList);
					}
					hList.add(h);
				}
			}
		}
	}

	private void processLinkedClasses() {
		for (Class<?> c : objectClassList) {
			Field[] fields = classDescriptorFactory.getSingleClassFields(c);
			for (Field f : fields) {
				if (objectClassList.contains(f.getType())) {
					List<ClassLink> linkList = classLinkMap.get(c);
					if (linkList == null) {
						linkList = new ArrayList<>();
						classLinkMap.put(c, linkList);
					}
					// linkList.add(f.getType());
					linkList.add(new ClassLink(c, f.getType(), f, ClassLink.Cardinality.getCardinality(f)));
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					ParameterizedType pt = (ParameterizedType) f.getGenericType();
					Type[] types = pt.getActualTypeArguments();
					if (objectClassList.contains(types[0])) {
						List<ClassLink> lList = classLinkMap.get(c);
						if (lList == null) {
							lList = new ArrayList<>();
							classLinkMap.put(c, lList);
						}
						lList.add(new ClassLink(c, (Class<?>) types[0], f, ClassLink.Cardinality.getCardinality(f)));
					}
				}
			}
		}
	}

	public List<Class<?>> getChildClassesList(Class<?> c) {
		List<Class<?>> childClasses = childClassesMap.get(c);
		return childClasses == null ? Collections.emptyList() : new ArrayList<>(childClasses);
	}

	public List<Class<?>> getLinkedClassesList(Class<?> c) {
		List<ClassLink> linkList = classLinkMap.get(c);
		if (linkList == null)
			return Collections.emptyList();

		List<Class<?>> childClasses = new ArrayList<>();
		for (ClassLink link : linkList) {
			childClasses.add(link.getLinkedClass());
		}

		return childClasses;
	}

	public List<ClassLink> getClassLinkList(Class<?> c) {
		List<ClassLink> linkList = classLinkMap.get(c);
		return linkList == null ? Collections.emptyList() : new ArrayList<>(linkList);
	}

	public List<ClassLink> getReverseClassLinkList(Class<?> c) {
		List<ClassLink> reverseLinkList = new ArrayList<>();
		for (List<ClassLink> linkList : classLinkMap.values()) {
			for (ClassLink link : linkList) {
				if (link.getLinkedClass().equals(c)) {
					reverseLinkList.add(link);
				}
			}
		}
		return reverseLinkList;
	}

	public String getModelName() {
		return modelName;
	}

	public List<Class<?>> getObjectClassList() {
		return objectClassList;
	}

	public ModelAccessor getModelAccessor() {
		return modelAccessor;
	}

	public ClassDescriptorFactory getClassDescriptorFactory() {
		return classDescriptorFactory;
	}

	@Override
	public String toString() {
		return modelName;
	}

}
