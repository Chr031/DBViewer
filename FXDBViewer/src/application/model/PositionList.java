package application.model;

import java.util.ArrayList;
import java.util.List;

public class PositionList {
	public static PositionList getNewInstance(Model model) {
		PositionList positionList = new PositionList();
		positionList.setModelName(model.getModelName());
		List<Position> positions = new ArrayList<>();
		for (Class<?> tableClass : model.getObjectClassList()) {
			positions.add(new Position(tableClass, 40,40));
		}
		positionList.setList(positions);
		return positionList;
	}

	private String modelName;
	private List<Position> list = new ArrayList<>();

	public PositionList() {
		super();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public List<Position> getList() {
		return list;
	}

	public void setList(List<Position> list) {
		this.list = list;
	}

}