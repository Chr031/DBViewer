package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
import java.util.Collection;

import application.utils.CollectionsUtility;
@Deprecated
public class ManyToManyReverseAccessor<T, R> implements ReverseAccessorApplier<T, Collection<R>> {
	private final Field originalField;
	private final Field reverseField;

	public ManyToManyReverseAccessor(Field originalField, Field reverseField) {
		this.originalField = originalField;
		this.reverseField = reverseField;
	}

	@Override
	public void applyReverse(T targetInstance, Collection<R> reverseInstanceCollection) throws IllegalArgumentException,IllegalAccessException,InstantiationException {
		reverseField.setAccessible(true);
		for (R reverseInstance : reverseInstanceCollection) {
			Collection<T> tCollection = (Collection<T>) reverseField.get(reverseInstance);
			Collection<T> tCollectionClone;
			if (tCollection != null) {
				tCollectionClone = tCollection.getClass().newInstance();
				tCollectionClone.addAll(tCollection);
			} else {
				tCollectionClone = CollectionsUtility.getCollectionInstance((Class<Collection<T>>) reverseField.getType());
			}
			tCollectionClone.add(targetInstance);
			reverseField.set(reverseInstance, tCollectionClone);
		}
	}

	@Override
	public void unApplyReverse(T targetInstance, Collection<R> oldReverseInstanceCollection) throws IllegalArgumentException,IllegalAccessException,InstantiationException {
		reverseField.setAccessible(true);
		for (R oldReverseInstance : oldReverseInstanceCollection) {
			Collection<T> oldRCollection = (Collection<T>) reverseField.get(oldReverseInstance);

			if (oldRCollection != null) {
				Collection<T> newOldRCollection = (Collection<T>) oldRCollection.getClass().newInstance();
				newOldRCollection.addAll(oldRCollection);
				newOldRCollection.remove(targetInstance);
				reverseField.set(oldReverseInstance, newOldRCollection);

			}
		}

	}

}