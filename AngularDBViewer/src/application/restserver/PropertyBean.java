package application.restserver;

import application.model.descriptor.presenter.LinkedValue;

public class PropertyBean<F> {
	private String name;
	private LinkedValue<F> value;
	private String templateType;
	private String[] templateOptions;

	public PropertyBean() {
	};

	public PropertyBean(String name, LinkedValue<F> value, String templateType) {
		this();
		this.name = name;
		this.value = value;
		this.templateType = templateType;
	}

	public PropertyBean(String name, LinkedValue<F> value, String templateType, String[] templateOptions) {
		this(name, value, templateType);
		this.templateOptions = templateOptions;
	}

	public String getName() {
		return name;
	}

	public LinkedValue<F> getValue() {
		return value;
	}

	public String getTemplateType() {
		return templateType;
	}

	public String[] getTemplateOptions() {
		return templateOptions;
	}

}
