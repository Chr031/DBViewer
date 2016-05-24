package application.restserver;

import java.util.ArrayList;
import java.util.List;

import application.model.descriptor.ClassDescriptor;

public class ObjectBeanList<T> {
	
	private String title;

	private final List<ObjectBean<T>> dataList;
	private final ClassDescriptor<T> classDescriptor;

	public ObjectBeanList(ClassDescriptor<T> classDescriptor, List<T> dataList) throws Exception {		
		this.classDescriptor = classDescriptor;
		this.dataList = getObjectBeanList(dataList);
		title = classDescriptor.getObjectName() + "'s list";
	}
	
	
	private List<ObjectBean<T>> getObjectBeanList(List<T> dataList) throws Exception {
		List<ObjectBean<T> > beans = new ArrayList<>();
		ObjectBeanFactory obFactory = new ObjectBeanFactory() ;
		
		for (T t : dataList) {
			beans.add(obFactory.toObjectBean(t,classDescriptor));
		}
		
		return beans;
	}

	public List<ObjectBean<T>> getDataList() {
		return dataList;
	}

	public ClassDescriptor<T> getClassDescriptor() {
		return classDescriptor;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

}
