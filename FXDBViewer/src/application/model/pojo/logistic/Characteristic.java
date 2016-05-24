package application.model.pojo.logistic;

public abstract class Characteristic {

	private String name;
	private String value;
	private Distribution variability;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Distribution getVariability() {
		return variability;
	}

	public void setVariability(Distribution variability) {
		this.variability = variability;
	}
}
