package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
@Deprecated
public class OneToManyReverseAccessor<T, R> implements ReverseAccessorApplier<T, Collection<R>> {
	private final Field originalField;
	private final Field reverseField;

	public OneToManyReverseAccessor(Field originalField, Field reverseField) {
		this.originalField = originalField;
		this.reverseField = reverseField;
	}

	@Override
	public void applyReverse(T targetInstance, Collection<R> reverseInstanceCollection) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		reverseField.setAccessible(true);
		// clone the reverseInstance during the loop 
		Collection<R> reverseInstanceCollectionClone = new ArrayList<>(reverseInstanceCollection);
		for (R reverseInstance : reverseInstanceCollectionClone) {
			T old = (T) reverseField.get(reverseInstance);
			if (old != null) {
				originalField.setAccessible(true);
				Collection<R> oldRCollection = (Collection<R>) originalField.get(old);
				if (oldRCollection != null) {
					Collection<R> newOldRCollection = (Collection<R>)oldRCollection.getClass().newInstance();
					newOldRCollection.addAll(oldRCollection);
					newOldRCollection.remove(reverseInstance);
					originalField.set(old, newOldRCollection);
				}
					
			}
			reverseField.set(reverseInstance, targetInstance);
		}

	}

	@Override
	public void unApplyReverse(T targetInstance, Collection<R> reverseInstanceCollection) throws IllegalArgumentException, IllegalAccessException {
		reverseField.setAccessible(true);
		for (R reverse : reverseInstanceCollection) {
			reverseField.set(reverse, null);
		}

	}
}