package application.view.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;

public class FilterEvaluator<T> {

	private static final Logger log = LogManager.getLogger(FilterEvaluator.class);
	
	public boolean evaluate(ClassDescriptor<T> classDescriptor, String filterValue, T t) {
		String[] fieldSearches = filterValue.split("[;,]");
		Boolean found = null;
		for (String fieldSearch : fieldSearches) {

			String[] search = fieldSearch.split("[:=]");
			if (search.length == 2) {
				PropertyDescriptorI pDesc = classDescriptor.getPropertyDescriptor(search[0]);
				if (pDesc != null) {
					try {
						Object propertyValue = pDesc.getAccessor().get(t);

						boolean lFound = propertyValue.toString().toLowerCase().contains(search[1].toLowerCase());
						if (found == null)
							found = lFound;
						else
							found &= lFound;

						if (!found)
							break;
					} catch (Exception e) {
						log.warn("Error during filter evaluation", e);
					}
				}
			}

		}
		if (found != null && found)
			return true;

		return t.toString().toLowerCase().contains(filterValue.toLowerCase());
	}

}
