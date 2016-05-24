package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import application.model.descriptor.objectaccessor.Accessor;
import application.utils.CollectionsUtility;

public class WebCollectionPresenter<T, S> extends WebPresenter<T, Collection<S>> {

	public WebCollectionPresenter(Accessor<T, Collection<S>> fieldAccessor) {
		super(fieldAccessor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.COLLECTION;
	}

	@Override
	public Collection<S> getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	@Override
	public List<ObjectLink> getLinks(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		Collection<S> subObjectList = getObjectValue(t);

		List<ObjectLink> links = new ArrayList<>();
		if (subObjectList != null)
			for (S s : subObjectList) {
				links.add(new ObjectLink(s.toString(), s.getClass(), s.hashCode()));
			}
		return links;

	}

	@Override
	public LinkedValue getLinkedValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		Collection<S> subList = getObjectValue(t);
		if (subList == null)
			return new LinkedValue("0", new ObjectLink[] {});

		String representation = "" + subList.size();
		ObjectLink[] links = new ObjectLink[subList.size()];
		int i = 0;
		for (S s : subList) {
			if (s == null)
				continue;
			links[i++] = new ObjectLink<>(s.toString(), s.getClass(), s.hashCode());
		}

		return new LinkedValue(representation, links);
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, IOException,
			InstantiationException, InvocationTargetException {
		Collection<S> targetCollection = getObjectValue(t);
		if (targetCollection == null) {
			targetCollection = CollectionsUtility.getCollectionInstance((Class<Collection<S>>) accessor.getType());
			accessor.set(t, targetCollection);
		}
		targetCollection.clear();
		for (ObjectLink link : value.getLinks()) {
			if (link == null)
				continue;
			System.out.println(link.getLinkedClass() + " " + link.getLinkedId());
			S s = (S) modelAccessor.getById(link.getLinkedClass(), link.getLinkedId());
			if (s != null)
				targetCollection.add(s);
		}

	}

}
