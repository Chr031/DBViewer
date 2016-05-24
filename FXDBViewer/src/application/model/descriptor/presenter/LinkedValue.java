package application.model.descriptor.presenter;

import java.util.Arrays;


/**
 * This class is intended to support simple values, link or multiple links.
 * 
 * @author Christophe
 *
 */
public class LinkedValue<F> {

	private String value;
	private ObjectLink<F>[] links;

	public LinkedValue() {
	}

	public LinkedValue(String value, ObjectLink<F>... links) {
		super();
		this.value = value;
		this.links = links;
	}

	public String getValue() {
		return value;
	}

	public ObjectLink<F>[] getLinks() {
		return links;
	}

	@Override
	public String toString() {
		return "LinkedValue [value=" + value + ", links=" + Arrays.toString(links) + "]";
	}

}
