package application.model.descriptor.objectaccessor;

/**
 * 
 * @author Bleu
 *
 * @param <T>
 *            the class that is modified
 * @param <R>
 *            the reverse class that need to be modified.
 */
@Deprecated
public interface ReverseAccessorApplier<T, R> {
	void applyReverse(T targetInstance, R reverseInstance) throws IllegalArgumentException, IllegalAccessException, InstantiationException;

	void unApplyReverse(T targetInstance, R oldReverseInstance) throws IllegalArgumentException, IllegalAccessException, InstantiationException;
}