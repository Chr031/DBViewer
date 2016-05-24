package application.restserver;

import java.util.HashMap;
import java.util.Map;

public class ObjectBean<T> {

	private int originalHashCode;
	private Class<T> objectClass;

	private final Map<String, PropertyBean> propertyBeans;

	private ObjectBean() {
		this.propertyBeans = new HashMap<String, PropertyBean>();
	};

	public ObjectBean(int hashCode, Class<T> objectClass) {
		this();
		this.originalHashCode = hashCode;
		this.objectClass = objectClass;

	}

	public int getOriginalHashCode() {
		return originalHashCode;
	}

	public Class<T> getObjectClass() {
		return objectClass;
	}

	public void addPropertyBean(PropertyBean propertyBean) {
		propertyBeans.put(propertyBean.getName(), propertyBean);
	}

	public Map<String, PropertyBean> getPropertyBeans() {
		return propertyBeans;
	}

}
