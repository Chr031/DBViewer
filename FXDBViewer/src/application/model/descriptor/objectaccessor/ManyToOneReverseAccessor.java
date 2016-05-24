package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
import java.util.Collection;

import application.utils.CollectionsUtility;
@Deprecated
public class ManyToOneReverseAccessor<T, R> implements ReverseAccessorApplier<T, R> {
	//private final Field originalField;
	private final Field reverseField;

	public ManyToOneReverseAccessor(Field originalField, Field reverseField) {
		//this.originalField = originalField;
		this.reverseField = reverseField;
	}

	@Override
	public void applyReverse(T targetInstance, R reverseInstance) throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		reverseField.setAccessible(true);
		Collection<T> reverseCollectionInstance = (Collection<T>) reverseField.get(reverseInstance);
		Collection<T> newReverseCollectionInstance;
		if (reverseCollectionInstance != null) {
			newReverseCollectionInstance = (Collection<T>) reverseCollectionInstance.getClass().newInstance();
			newReverseCollectionInstance.addAll(reverseCollectionInstance);
		} else {
			newReverseCollectionInstance = CollectionsUtility
					.getCollectionInstance((Class<? extends Collection<T>>) reverseField.getType());
		}
		if (!newReverseCollectionInstance.contains(targetInstance))
			newReverseCollectionInstance.add(targetInstance);
		reverseField.set(reverseInstance, newReverseCollectionInstance);

	}

	@Override
	public void unApplyReverse(T targetInstance, R oldReverseInstance) throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		reverseField.setAccessible(true);
		Collection<T> oldRCollection = (Collection<T>) reverseField.get(oldReverseInstance);

		if (oldRCollection != null) {
			Collection<T> newOldRCollection = (Collection<T>) oldRCollection.getClass().newInstance();
			newOldRCollection.addAll(oldRCollection);
			newOldRCollection.remove(targetInstance);
			reverseField.set(oldReverseInstance, newOldRCollection);

		}

	}
}