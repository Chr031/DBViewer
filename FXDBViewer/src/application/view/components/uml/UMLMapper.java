package application.view.components.uml;

import java.io.IOException;
import java.util.Map.Entry;

import javafx.scene.control.Control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.Model;
import application.model.ModelService;
import application.model.Position;
import application.model.PositionList;
import application.utils.Callback;

public class UMLMapper extends UMLAbstractMapper {

	private static final Logger log = LogManager.getLogger(UMLMapper.class);

	private ModelService modelService;

	public UMLMapper() {
		super();

	}

	private PositionList loadModelPosition() throws IOException {
		return modelService.loadModelPosition(model);
	}

	private void saveModelPosition() throws IOException {

		PositionList pl = new PositionList();
		pl.setModelName(model.getModelName());
		for (Entry<Class<?>, Control> e : classMap.entrySet()) {
			pl.getList().add(new Position(e.getKey(), e.getValue().getLayoutX(), e.getValue().getLayoutY()));
		}
		modelService.saveModelPosition(pl);
	}

	protected void onDragEnd() {
		try {
			saveModelPosition();
		} catch (IOException e) {
			log.error("Unable to save model position",e );
		}
	};
	
	
	@Override
	public void initializeObjects(Callback<Class<?>, Void> classActionCallback) {
		super.initializeObjects(classActionCallback);
		try {
			PositionList pl = loadModelPosition();
			if (pl != null) {
				for (Position p : pl.getList()) {
					Control control = classMap.get(p.getClazz());
					control.setLayoutX(p.getX());
					control.setLayoutY(p.getY());
				}
			}

		} catch (IOException e) {
			log.error("Unable to initialize Objects", e);
		}

	}

	@Override
	public void setModel(Model model) {
		if (this.model != null)
			try {
				saveModelPosition();
			} catch (IOException e) {
				log.error("Unable to save model position", e);
			}
		super.setModel(model);
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}
