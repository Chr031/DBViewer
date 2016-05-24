package application.model.dataaccessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.dataaccessor.condition.Condition;

public class MemoryModelAccessor implements ModelAccessor {

	private Map<Class, List> dataStore;

	public MemoryModelAccessor() {
		dataStore = new HashMap<>();
	}

	private <T> List<T> getList(Class<T> tableClass) {
		List dataList = dataStore.get(tableClass);
		if (dataList == null) {
			synchronized (dataStore) {
				dataList = dataStore.get(tableClass);
				if (dataList == null)
					dataStore.put(tableClass, dataList = new ArrayList());
			}
		}
		return dataList;
	}

	@Override
	public <T> List<T> get(Class<T> tableClass, Condition<T> where) throws IOException{
		List<T> resultList = new ArrayList<>();
		for (T t: getList(tableClass)) {
			try {
				if (where == null || where.verify(t)) 
					resultList.add(t);
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				throw new IOException(e);
			}
		}
		return resultList;
	}

	@Override
	public <T> List<T> getAll(Class<T> tableClass) {

		return new ArrayList<>(getList(tableClass));
	}

	@Override
	public <T> T getById(Class<T> tableClass, int id) throws IOException {
		List<T> list = getList(tableClass);
		for (T t:list) {
			if (t.hashCode() == id) 
				return t;
		}
		return null;
	}

	@Override
	public <T> void save(Class<T> tableClass, T data) {
		List<T> dataList = getList(tableClass);
		if (!dataList.contains(data))
			dataList.add(data);

	}

	@Override
	public <T> void delete(Class<T> tableClass, T data) {
		List<T> dataList = getList(tableClass);
		dataList.remove(data);
	}

	@Override
	public void save(Object... data) throws IOException {
		synchronized (dataStore) {
			for (Object o : data) {
				List dataList = getList(o.getClass());
				if (!dataList.contains(data)) {
					dataList.add(o);
				}
			}
		}

	}

	@Override
	public void delete(Object... data) throws IOException {
		synchronized (dataStore) {
			for (Object o : data) {
				List dataList = getList(o.getClass());
				dataList.remove(data);
			}
		}

	}

}
