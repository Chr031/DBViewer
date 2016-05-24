package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.descriptor.objectaccessor.Accessor;

public class WebDatePresenter<T> extends WebPresenter<T, Date> {

	private static final Logger log = LogManager.getLogger(WebDatePresenter.class);
	
	private SimpleDateFormat inFormat;// 2004-04-06T22:00:00.000Z
	private SimpleDateFormat outFormat;

	public WebDatePresenter(Accessor<T, Date> fieldAccessor) {
		super(fieldAccessor);
		outFormat = new SimpleDateFormat("dd/MM/yyyy");
		inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	@Override
	public PropertyTemplate getTemplateType() {
		return PropertyTemplate.DATE;
	}

	@Override
	public Date getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return accessor.get(t);
	}

	public LinkedValue getLinkedValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		Date d = getObjectValue(t);
		if (d != null) {
			return new LinkedValue(outFormat.format(d));
		} else
			return new LinkedValue("");
	}

	@Override
	public void setLinkedValue(T t, LinkedValue value) throws Exception {
		if (value.getValue().trim().length() == 0) {
			accessor.set(t, null);
			return;
		}

		Date d;
		// test the different formats
		log.debug("!!! web format date : " + value.getValue());
		try {
			d = outFormat.parse(value.getValue());
		} catch (ParseException pe) {
			d = inFormat.parse(value.getValue());
			log.error(pe);
		}
		log.debug("real date : " + d);
		accessor.set(t, d);

	}

}
