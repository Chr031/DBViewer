package application.restserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import application.model.Model;
import application.model.ModelService;
import application.model.PositionList;
import application.model.dataaccessor.condition.Condition;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.presenter.PresenterType;

@RestController
public class RestAppController {

	private final static Logger log = Logger.getLogger(RestAppController.class);

	private final ModelService modelService;

	private final ObjectBeanFactory factory;

	public RestAppController() throws Exception {
		log.info("Load new instance of " + getClass().getName());
		modelService = new ModelService();
		factory = new ObjectBeanFactory();

	}

	@RequestMapping("/getModels")
	public ModelBean[] getModels() {

		List<ModelBean> beans = new ArrayList<>();
		for (Model model : modelService.getModelList()) {
			beans.add(new ModelBean(model));
		}

		return beans.toArray(new ModelBean[] {});

	}

	@RequestMapping(value = "/getModelPositionBean/{modelName}", method = RequestMethod.GET)
	public PositionList getModelPositionBean(@PathVariable String modelName) throws IOException {
		Model model = modelService.getModel(modelName);
		PositionList positionList = modelService.loadModelPosition(model);
		if (positionList == null)
			positionList = PositionList.getNewInstance(model);
		return positionList;
	}

	@RequestMapping(value = "/setModelPositionBean", method = RequestMethod.POST)
	public boolean setModelPositionBean(@RequestBody PositionList positionList) throws IOException {
		modelService.saveModelPosition(positionList);
		return true;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/getObjectBeanList/{modelName}/{className}", method = RequestMethod.GET)
	public <T> ObjectBeanList<T> getObjectBeanList(@PathVariable String modelName, @PathVariable String className) throws Exception {

		Model model = modelService.getModel(modelName);
		if (model == null)
			throw new Exception("Model is null with modelName " + modelName);
		Class<T> tableClass = (Class<T>) Class.forName(className);
		ClassDescriptor<T> classDescriptor = model.getClassDescriptorFactory().getGridDescriptor(tableClass);
		List<T> dataList = model.getModelAccessor().getAll(tableClass);
		classDescriptor.initPresenters(PresenterType.WEB);
		ObjectBeanList<T> objectBeanList = new ObjectBeanList<T>(classDescriptor, dataList);

		return objectBeanList;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/getObjectBeanFilteredList/{modelName}/{className}", method = RequestMethod.POST)
	public <T> ObjectBeanList<T> getObjectBeanFilteredList(@PathVariable String modelName, @PathVariable String className,
			@RequestBody FilterBean filter) throws Exception {

		Model model = modelService.getModel(modelName);
		if (model == null)
			throw new Exception("Model is null with modelName " + modelName);
		Class<T> tableClass = (Class<T>) Class.forName(className);
		ClassDescriptor<T> classDescriptor = model.getClassDescriptorFactory().getGridDescriptor(tableClass);
		Condition<T> condition = (filter != null ? filter.getCondition(model) : null);
		List<T> dataList = model.getModelAccessor().get(tableClass, condition);
		log.debug(dataList.size() + " " + condition);

		classDescriptor.initPresenters(PresenterType.WEB);
		ObjectBeanList<T> objectBeanList = new ObjectBeanList<T>(classDescriptor, dataList);
		if (condition != null)
			objectBeanList.setTitle(condition.toString());
		return objectBeanList;
	}

	@RequestMapping(value = "/getObjectBean/{modelName}/{className}/{objectId}", method = RequestMethod.GET)
	public <T> ObjectBeanForm<T> getObjectBean(@PathVariable String modelName, @PathVariable String className, @PathVariable int objectId)
			throws Exception {

		Model model = modelService.getModel(modelName);
		if (model == null)
			throw new Exception("Model is null with modelName " + modelName);
		Class<T> tableClass = (Class<T>) Class.forName(className);
		ClassDescriptor<T> classDescriptor = model.getClassDescriptorFactory().getFormDescriptor(tableClass);

		// maybe a bug there ! trigger a new object if not found !
		T t = model.getModelAccessor().getById(tableClass, objectId);
		if (t == null)
			t = classDescriptor.getNewClassInstance();

		ObjectBean<T> objectBean = factory.toObjectBean(t, classDescriptor);

		return new ObjectBeanForm<T>(objectBean, classDescriptor);

	}

	@RequestMapping(value = "/setObjectBean/{modelName}/{className}", method = RequestMethod.POST)
	public int setObjectBean(@PathVariable String modelName, @PathVariable String className, @RequestBody ObjectBean bean)
			throws Exception {

		Model model = modelService.getModel(modelName);
		if (model == null)
			throw new Exception("Model is null with modelName " + modelName);
		ClassDescriptor classDescriptor = model.getClassDescriptorFactory().getFormDescriptor(bean.getObjectClass());

		Object data = model.getModelAccessor().getById(bean.getObjectClass(), bean.getOriginalHashCode());
		if (data == null) {// seems to be a creation;
			data = classDescriptor.getNewClassInstance();
		}

		classDescriptor.initPresenters(PresenterType.WEB);
		classDescriptor.setModelAccessor(model.getModelAccessor());

		factory.fromObjectBean(data, bean, classDescriptor);

		model.getModelAccessor().save(data);

		Collection<PropertyBean> c = bean.getPropertyBeans().values();
		System.out.println(bean.getObjectClass().getName());
		for (Object o : c) {
			PropertyBean p = (PropertyBean) o;
			log.debug(p.getName() + " " + p.getValue());
		}

		return data.hashCode();
	}

	@RequestMapping(value = "/deleteObject/{modelName}/{className}/{objectId}")
	public <T> void deleteObject(@PathVariable String modelName, @PathVariable String className, @PathVariable int objectId)
			throws Exception {
		Model model = modelService.getModel(modelName);
		if (model == null)
			throw new Exception("Model is null with modelName " + modelName);
		Class<T> tableClass = (Class<T>) Class.forName(className);
		T t = model.getModelAccessor().getById(tableClass, objectId);
		model.getModelAccessor().delete(t);
		log.info("object " + t + "@" + objectId + " deleted");
	}

}
