package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
@Deprecated
public class OneToOneReverseAccessor<T, R> implements ReverseAccessorApplier<T, R> {

	private final Field originalField;
	private final Field reverseField;

	public OneToOneReverseAccessor(Field originalField, Field reverseField) {
		this.originalField = originalField;
		this.reverseField = reverseField;
	}

	@Override
	public void applyReverse(T targetInstance, R reverseInstance) throws IllegalArgumentException, IllegalAccessException {
		reverseField.setAccessible(true);
		T old = (T) reverseField.get(reverseInstance);
		if (old != null) {
			originalField.setAccessible(true);
			originalField.set(old, null);
		}
		reverseField.set(reverseInstance, targetInstance);
	}

	@Override
	public void unApplyReverse(T targetInstance, R oldReverseInstance) throws IllegalArgumentException, IllegalAccessException {
		reverseField.setAccessible(true);
		reverseField.set(oldReverseInstance, null);

	}

}