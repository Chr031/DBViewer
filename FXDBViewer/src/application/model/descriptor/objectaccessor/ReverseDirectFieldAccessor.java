package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.descriptor.annotations.Reverse;

/**
 * <p>
 * Manage a dual modification of Fields. Unfortunately, this is very complex to
 * manage on the UI side : one modification of a field implies a dual modification
 * of objects and in this sense forms and transaction are not autonomous !
 * </p>
 * <p>This should not be used on a production purpose.</p>
 * 
 * @author Can
 *
 * @param <D>
 * @param <F>
 */
@Deprecated
public class ReverseDirectFieldAccessor<D, F> extends DirectFieldAccessor<D, F> {

	private static final Logger log = LogManager.getLogger(ReverseDirectFieldAccessor.class);

	public enum LinType {
		OneToOne, OneToMany, ManyToOne, ManyToMany;

	}

	private static final int _11Link = 0;
	private static final int _1NLink = 1;
	private static final int _N1Link = 2;
	private static final int _NNLink = 3;

	private ReverseAccessorApplier reverseAccessor;
	private boolean activateReverseAccessor;

	public ReverseDirectFieldAccessor(Field field) throws NoSuchFieldException, SecurityException {
		super(field);
		Reverse reverse;
		if ((reverse = field.getAnnotation(Reverse.class)) != null) {
			analyseReverse(reverse);
			log.info("Reverse field " + field.getDeclaringClass().getSimpleName() + "." + field.getName() + " bound with "
					+ reverse.reverseField() + " initialized");

		}
		activateReverseAccessor = reverse != null;
	}

	private void analyseReverse(Reverse reverse) throws NoSuchFieldException, SecurityException {

		Class<?> targetClass = null;
		int linkType = 0;
		if (Collection.class.isAssignableFrom(getType())) {
			targetClass = getActualTypeArgument();
			linkType += 1;
		} else {
			targetClass = getType();
		}

		Field reverseField = targetClass.getDeclaredField(reverse.reverseField());

		if (Collection.class.isAssignableFrom(reverseField.getType())) {
			targetClass = (Class<?>) ((ParameterizedType) reverseField.getGenericType()).getActualTypeArguments()[0];
			linkType += 0b10;
		} else {
			targetClass = getType();
		}

		switch (linkType) {
		case _11Link:
			reverseAccessor = new OneToOneReverseAccessor(getField(), reverseField);
			break;
		case _1NLink:
			reverseAccessor = new OneToManyReverseAccessor(getField(), reverseField);
			break;
		case _N1Link:
			reverseAccessor = new ManyToOneReverseAccessor(getField(), reverseField);
			break;
		case _NNLink:
			reverseAccessor = new ManyToManyReverseAccessor(getField(), reverseField);
			break;
		}
	}

	public boolean isActivateReverseAccessor() {
		return activateReverseAccessor;
	}

	public void setActivateReverseAccessor(boolean activateReverseAccessor) {
		this.activateReverseAccessor = activateReverseAccessor;
	}

	@Override
	public void set(D dataObject, F fieldTypeValue) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		if (reverseAccessor == null || !activateReverseAccessor) {
			super.set(dataObject, fieldTypeValue);
			return;
		}

		// with reverse setting.
		F oldReverseInstance = this.get(dataObject);
		if (oldReverseInstance != null)
			reverseAccessor.unApplyReverse(dataObject, oldReverseInstance);
		super.set(dataObject, fieldTypeValue);
		if (fieldTypeValue != null)
			reverseAccessor.applyReverse(dataObject, fieldTypeValue);
	}

	@Override
	public boolean isReverse() {
		return true;
	}

}
