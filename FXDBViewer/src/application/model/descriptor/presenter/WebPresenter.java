package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;

public abstract class WebPresenter<T, F> implements PresenterI<T> {

	public enum PropertyTemplate {
		TEXTFIELD,TEXTAREA, DATE, BUTTON,COLLECTION, ENUM, BOOLEAN
	}
	
	
	protected ModelAccessor modelAccessor;

	public void setModelAccessor(ModelAccessor modelAccessor) {
		this.modelAccessor = modelAccessor;
	}

	protected final Accessor<T, F> accessor;

	public WebPresenter(Accessor<T, F> fieldAccessor) {
		this.accessor = fieldAccessor;
	}

	public abstract PropertyTemplate getTemplateType();
	
	public String[] getTemplateOptions() {return null;};

	public abstract F getObjectValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;

	public List<ObjectLink> getLinks(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		return Collections.emptyList();
	}
	
	public LinkedValue getLinkedValue(T t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		F f = getObjectValue(t);
		if (f != null) {
			return new LinkedValue(f.toString());
		} else return new LinkedValue("");
	}
	
	public abstract void setLinkedValue(T t, LinkedValue value) throws IllegalArgumentException, IllegalAccessException, IOException, Exception;
	
	
	@Override
	public boolean isTypeOf(PresenterType presenterType) {
		return presenterType == PresenterType.WEB;
	}

	@Override
	public Collection<? extends Object> getDependingData() {
		return Collections.emptyList();
	}
}
