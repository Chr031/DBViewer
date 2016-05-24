package application.restserver;

import java.util.ArrayList;
import java.util.List;

import application.model.ClassLink;
import application.model.Model;

public class ModelBean {

	private final String modelName;
	private final List<ClassBean> classes;
	private final List<LinkBean> links;

	
	public ModelBean(Model model) {
		this.modelName = model.getModelName();
		this.classes = new ArrayList<ClassBean>();
		this.links = new ArrayList<>();
		for (Class<?> clazz : model.getObjectClassList()) {
			classes.add(new ClassBean(clazz));
			List<Class<?>> childClasses = model.getChildClassesList(clazz);
			if (childClasses != null && childClasses.size()>0) {
				for (Class<?> childClass : childClasses ) links.add(new LinkBean(clazz, childClass));
			}
			List<ClassLink> classLinkList  = model.getClassLinkList(clazz);
			if (classLinkList != null && classLinkList.size()>0) {
				for (ClassLink link : classLinkList ) links.add(new LinkBean(link));
			}
		}

	}

	public List<ClassBean> getClasses() {
		return classes;
	}

	public String getModelName() {
		return modelName;
	}
	
	public List<LinkBean> getLinks() {
		return links;
	}


}
