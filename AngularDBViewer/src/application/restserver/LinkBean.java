package application.restserver;

import application.model.ClassLink;

public class LinkBean {

	private String type;
	private String startClassName;
	private String endClassName;
	private String startCardinality;
	private String endCardinality;

	public LinkBean(String type, String startClassName, String endClassName, String startCardinality, String endCardinality) {
		super();
		this.type = type;
		this.startClassName = startClassName;
		this.endClassName = endClassName;
		this.startCardinality = startCardinality;
		this.endCardinality = endCardinality;
	}

	/**
	 * Creates a link of the type foreign key
	 * 
	 * @param classLink
	 */
	public LinkBean(ClassLink classLink) {
		this.type = "key";
		this.startClassName = classLink.getBaseClass().getSimpleName();
		this.endClassName = classLink.getLinkedClass().getSimpleName();
		if (classLink.getCardinality() != null) {
			this.startCardinality = classLink.getCardinality().getBaseCardinalitySymbol();
			this.endCardinality = classLink.getCardinality().getLinkedCardinalitySymbol();
		}
	}

	public LinkBean(Class<?> parentClass, Class<?> childClass) {
		this("herit", parentClass.getSimpleName(), childClass.getSimpleName(), null, null);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartClassName() {
		return startClassName;
	}

	public void setStartClassName(String startClassName) {
		this.startClassName = startClassName;
	}

	public String getEndClassName() {
		return endClassName;
	}

	public void setEndClassName(String endClassName) {
		this.endClassName = endClassName;
	}

	public String getStartCardinality() {
		return startCardinality;
	}

	public void setStartCardinality(String startCardinality) {
		this.startCardinality = startCardinality;
	}

	public String getEndCardinality() {
		return endCardinality;
	}

	public void setEndCardinality(String endCardinality) {
		this.endCardinality = endCardinality;
	}

}
