package application.model.dataaccessor;

import java.io.IOException;
import java.util.List;

import application.model.dataaccessor.condition.Condition;

public interface ModelAccessor {
	
	public <T> List<T> get(Class<T> tableClass, Condition<T> where) throws IOException;

	public <T> List<T> getAll(Class<T> tableClass) throws IOException;
	
	public <T> T getById(Class<T> tableClass, int id) throws IOException;

	@Deprecated
	public <T> void save(Class<T> tableClass, T data) throws IOException;

	@Deprecated
	public <T> void delete(Class<T> tableClass, T data) throws IOException;

	public void save(Object... dataArray) throws IOException;
	
	public void delete(Object... dataArray) throws IOException;
}
