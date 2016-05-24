package application.view.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.ModelException;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.model.descriptor.presenter.JFXPresenter;
import application.model.descriptor.presenter.PresenterType;
import application.utils.Callback;

public class ComputeCalculatedFieldsHandler<T> implements Callback<Void,Void>{

	private static final Logger log = LogManager.getLogger(ComputeCalculatedFieldsHandler.class);
	
	private final ClassDescriptor<T> classDescriptor;
	
	private final List<PropertyDescriptorI> calculatedProperties;
	
	public ComputeCalculatedFieldsHandler(ClassDescriptor<T> descriptor) {

		classDescriptor = descriptor;
		calculatedProperties = descriptor.getCalculatedProperties();
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Void call(Void a) {
		
		try {
			T data = classDescriptor.getNewClassInstance();
			classDescriptor.activateReverseFieldSetters(false);
			for (PropertyDescriptorI pDesc : classDescriptor.getAllPropertyDescriptors()) {
				if (!calculatedProperties.contains(pDesc) && pDesc.getAccessor().getDeclaringClass().isAssignableFrom(data.getClass())) {
					JFXPresenter p = (JFXPresenter) pDesc.getPresenter(PresenterType.JFX);
					p.setPropertyValue(data);
				}
			}
			
			for (PropertyDescriptorI calculatedProperty : calculatedProperties) {
				if (calculatedProperty.getAccessor().getDeclaringClass().isAssignableFrom(data.getClass())) {
					JFXPresenter p = (JFXPresenter) calculatedProperty.getPresenter(PresenterType.JFX);
					p.setNodeContent(data);
				}
			}
			classDescriptor.activateReverseFieldSetters(true);
			
			
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ModelException | NoSuchFieldException | SecurityException | IOException e) {
			//TODO should be thrown ? 
			
			log.error ("Unable to set calculated fields", e.getMessage());
		}
		
		
		return null;
		
		
	}

	
	
}
