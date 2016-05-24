package application.model.descriptor.presenter;

public class ObjectLink<T> {

	private String value;
	// may be not necessary there 
	private Class<T> linkedClass;
	private int linkedId;

	public ObjectLink() {
		;
	}

	public ObjectLink(String value, Class<T> linkedClass, int linkedId) {
		super();
		this.value = value;
		this.linkedClass = linkedClass;
		this.linkedId = linkedId;
	}

	public String getValue() {
		return value;
	}

	public Class<T> getLinkedClass() {
		return linkedClass;
	}

	public int getLinkedId() {
		return linkedId;
	}

	@Override
	public String toString() {
		return "ObjectLink [value=" + value + ", linkedClass=" + linkedClass + ", linkedId=" + linkedId + "]";
	}

}
