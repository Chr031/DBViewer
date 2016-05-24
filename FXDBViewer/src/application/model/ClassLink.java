package application.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import application.model.descriptor.annotations.Link01;
import application.model.descriptor.annotations.Link0N;
import application.model.descriptor.annotations.Link10;
import application.model.descriptor.annotations.Link11;
import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.LinkN0;
import application.model.descriptor.annotations.LinkN1;
import application.model.descriptor.annotations.LinkNN;

public class ClassLink {

	public enum Cardinality {
		C0_1(true, "0", "1"),
		C1_0(true, "1", "0"),
		C1_1(true, "1", "1"),
		C0_N(false, "0", "n"),
		CN_0(true, "n", "0"),
		C1_N(false, "1", "n"),
		CN_1(true, "n", "1"),
		CN_N(false, "n", "n");

		public static Cardinality getCardinality(Field f) {

			if (f.getAnnotation(LinkNN.class) != null)
				return CN_N;
			else if (f.getAnnotation(Link1N.class) != null)
				return C1_N;
			else if (f.getAnnotation(LinkN1.class) != null)
				return CN_1;
			else if (f.getAnnotation(Link0N.class) != null)
				return C0_N;
			else if (f.getAnnotation(LinkN0.class) != null)
				return CN_0;
			else if (f.getAnnotation(Link11.class) != null)
				return C1_1;
			else if (f.getAnnotation(Link10.class) != null)
				return C1_0;
			else if (f.getAnnotation(Link01.class) != null)
				return C0_1;
			else
				return null;

		}

		private final boolean linkSingle;
		private final String baseCardinality;
		private final String linkCardinality;

		private Cardinality(boolean linkSingle, String baseCardinality, String linkCardinality) {
			this.linkSingle = linkSingle;
			this.baseCardinality = baseCardinality;
			this.linkCardinality = linkCardinality;
		}

		public boolean isLinkSingle() {
			return linkSingle;
		}

		public boolean isLinkMultiple() {
			return !linkSingle;
		}

		public String getBaseCardinalitySymbol() {
			return baseCardinality;
		}

		public String getLinkedCardinalitySymbol() {
			return linkCardinality;
		}

	}

	private final Class<?> baseClass;
	private final Class<?> linkedClass;

	/**
	 * <p>
	 * This is the field that links the two classes {@link #baseClass} and
	 * {@link #linkedClass} This field belong to baseClass, and only this field
	 * can be modified.
	 * </p>
	 * <p>
	 * No "physical" fields are present in the linked class and any access to
	 * the linked data to display the links are made with the use of the base
	 * class.
	 * </p>
	 * 
	 */
	private final Field linkField;

	private final Cardinality cardinality;

	public ClassLink(Class<?> baseClass, Class<?> linkedClass, Field linkField, Cardinality cardinality) {
		super();
		this.baseClass = baseClass;
		this.linkedClass = linkedClass;
		this.linkField = linkField;
		this.cardinality = cardinality;
		
	}

	public Class<?> getBaseClass() {
		return baseClass;
	}

	public Class<?> getLinkedClass() {
		return linkedClass;
	}

	public Field getLinkField() {
		return linkField;
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

	private String  reverseName;
	public synchronized String getReverseName() {
		if (reverseName != null ) return reverseName;
		for (Annotation a : linkField.getAnnotations()) {
			Method m;
			 try {
				if ( (m =a.getClass().getMethod("reverseName", null))!= null) {
					 m.setAccessible(true);
					 reverseName = (String)m.invoke(a, null);
					 return reverseName;
				 }
			} catch (Throwable e) {
				// method do not exist ...
			}
		}
		return reverseName;
	}

}
