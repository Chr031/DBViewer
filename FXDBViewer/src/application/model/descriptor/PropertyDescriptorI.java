package application.model.descriptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.TableView;
import application.model.Model;
import application.model.dataaccessor.ModelAccessor;
import application.model.descriptor.objectaccessor.Accessor;
import application.model.descriptor.presenter.PresenterI;
import application.model.descriptor.presenter.PresenterType;

/**
 * TODO a new design is necessary in order to cut the FX control dependency and
 * to add a new Web way ! For example a representation interface that could act
 * as a marker, or something like this.
 * 
 * 
 * @author Bleu
 *
 * @param <D>
 * @param <P>
 */
public interface PropertyDescriptorI<D, T> {

	/**
	 * Can be seen as the constructor of the class !
	 * 
	 * @param model
	 *            TODO
	 * @param field
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void initialize(Model model, Accessor<D, T> fieldAccessor) throws InstantiationException,
			IllegalAccessException;

	public Accessor<D, T> getAccessor();

	/**
	 * TODO Should be part of the initialize method !!!
	 * 
	 * @param modelAccessor
	 */
	public void setModelAccessor(ModelAccessor modelAccessor);

	/**
	 * 
	 * @return the name of the underlying property
	 */
	public String getName();

	/**
	 * Extract the value of the field present in data and returns it as a
	 * String. Used by {@link TableView}s to present the data in a read only
	 * mode.
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public String getPropertyValueAsString(D data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IOException;

	/**
	 * TODO a good pattern, I think, should be to split this method into several one like
	 * : <li>getJFXFormPresenter</li> <li>getWebFromPresenter</li> <li>
	 * getJFXGridPresenter</li> <li>...</li> XXX To be discussed
	 * 
	 * @param presenterType
	 * @return
	 */
	public <P extends PresenterI<D>> P getPresenter(PresenterType presenterType);

	public boolean isCalculated();

	/**
	 * See the TODO of the method {@link #getPresenter(PresenterType)}
	 * @return
	 */
	public boolean isLink();

}