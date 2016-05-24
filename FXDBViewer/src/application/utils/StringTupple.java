package application.utils;

public class StringTupple {

	private String[] strings;

	public StringTupple(String... strings) {
		this.strings = strings;

	}

	public String toString() {
		return getMainString();
	}

	public String getMainString() {
		return strings[0];
	}

	public String getString(int index) {
		if (index < strings.length)
			return strings[index];
		return null;
	}

}
