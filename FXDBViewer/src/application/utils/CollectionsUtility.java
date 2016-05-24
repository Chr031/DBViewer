package application.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionsUtility {

	/**
	 * TODO manage all collection interfaces.
	 * @param collectionClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T extends Collection> T getCollectionInstance(Class<T> collectionClass) throws InstantiationException, IllegalAccessException {
		if (collectionClass.equals(Set.class) || collectionClass.equals(Collection.class)) {
			return (T)( new HashSet());
		} else if (collectionClass.equals(List.class)) {
			return (T)(new ArrayList());
		} else {
			return collectionClass.newInstance();
		}
	}
	
	
}
