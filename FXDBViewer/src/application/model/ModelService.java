package application.model;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import application.utils.Caller;

public class ModelService {

	private final Map<String ,Model> modelMap;
	
	private final WeakHashMap<Caller,Void> updateModelListeners; 
	
	

	public ModelService() throws Exception {
		modelMap = new HashMap<>();
		updateModelListeners = new WeakHashMap<>();
	
		addModel(ModelFactory.createModel("Ecom", "application.model.pojo.ecom"));
		addModel(ModelFactory.createModel("Tasks", "application.model.pojo.tasks"));
		addModel(ModelFactory.createModel("Cinema", "application.model.pojo.cinema"));
		addModel(ModelFactory.createModel("Logistic", "application.model.pojo.logistic"));
		addModel(ModelFactory.createModel("TimeSheet", "application.model.pojo.timesheet"));
		addModel(ModelFactory.createModel("ACL", "application.model.pojo.acl"));
		addModel(ModelFactory.createModel("Hotel", "application.model.pojo.hotel"));
		
	}
	
	public void addModel(Model model) {
		modelMap.put(model.getModelName(), model);
		
		for (Caller caller : updateModelListeners.keySet()) {
			caller.call();
		}
	}


	public Collection<Model> getModelList() {
		return modelMap.values();
	}

	
	public Model getModel(String modelName) {
		return modelMap.get(modelName);
	}
	
	public void saveModelPosition(PositionList positionList) throws IOException {
		Model model = getModel(positionList.getModelName());
		List<PositionList> pList = model.getModelAccessor().getAll(PositionList.class);
		model.getModelAccessor().delete(pList.toArray());
		model.getModelAccessor().save(positionList);
	}
	
	public PositionList loadModelPosition(Model model) throws IOException {
		List<PositionList> pList = model.getModelAccessor().getAll(PositionList.class);
		for (PositionList pl: pList) {
			if (pl.getModelName() .equals( model.getModelName()))
					return pl;
		}
		return null;
	}

	public void addModelListUpdateListener(Caller call) {
		updateModelListeners.put(call, null);
		
	}

	
	
		
	

}
