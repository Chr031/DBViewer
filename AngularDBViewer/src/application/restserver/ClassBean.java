package application.restserver;

public class ClassBean {

	private final String name;

	private final String simpleName;

	public ClassBean(String name, String simpleName) {
		super();
		this.name = name;
		this.simpleName = simpleName;
	}

	public ClassBean(Class<?> clazz) {
		this(clazz.getName(), clazz.getSimpleName());
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		return simpleName;
	}

}
