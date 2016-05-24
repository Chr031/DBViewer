package application.view;

import application.model.dataaccessor.ModelAccessor;
import application.model.dataaccessor.condition.Condition;
import application.model.descriptor.ClassDescriptor;
import application.view.saver.DataBinder;
import application.view.saver.FormSaver;

public interface TableAndFormService {

	public abstract <T> void loadList(Class<T> tableClass) throws Exception;
	
	public abstract <T> void loadList(Class<T> tableClass, Condition<T> where) throws Exception;

	public abstract <T> void loadForm(DataBinder<T> binder, Class<T> tableObject, FormSaver<T> saver) throws Exception;
	
	public abstract <T> void loadForm(DataBinder<T> binder, ClassDescriptor<T> formClassDescriptor, ModelAccessor modelAccessor, FormSaver<T> saver ) throws Exception ;

	public void removeActiveTab();
}



