package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;

public class WebButtonPresenter<T, F> extends WebPresenter<T, F> {

	
	public WebButtonPresenter(Accessor<T, F> fieldAccessor,ModelAccessor modelAccessor, PresenterType presenterType) {
		super(fieldAccessor);
		super.modelAccessor = modelAccessor;
		// TODO Auto-generated constructor stub
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.BUTTON;
	}

	@Override
	public F getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public List<ObjectLink> getLinks(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {

		F f = getObjectValue(t);

		List<ObjectLink> link = new ArrayList<>();
		if (f != null)
			link.add(new ObjectLink(f.toString(), f.getClass(), f.hashCode()));
		return link;
	}

	public LinkedValue getLinkedValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		F f = getObjectValue(t);
		String representation = (f != null ? f.toString() : "pick");
		return new LinkedValue(representation, new ObjectLink(representation, accessor.getType(),
				f != null ? f.hashCode() : 0));
	}

	@Override
	public void setLinkedValue( T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException {
		
		Class<?> tableClass =value.getLinks()[0].getLinkedClass();
		int objectId = value.getLinks()[0].getLinkedId();
		Object o = modelAccessor.getById(tableClass, objectId);
		accessor.set(t,(F) o);
	}

}
