package application.model.dataaccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.dataaccessor.condition.Condition;

import com.thoughtworks.xstream.XStream;

public class FileModelAccessor implements ModelAccessor {
	private Map<Class, List> dataStore;
	private final File dataFile;
	private final XStream xstream;

	
	public FileModelAccessor(String name) throws FileNotFoundException {
		this(name, FileModelAccessor.class.getClassLoader());
	}
	
	public FileModelAccessor(String name, ClassLoader classloader) throws FileNotFoundException {

		xstream = new XStream();
		xstream.setClassLoader(classloader);
		
		dataFile = new File(".", name + ".dat");
		if (dataFile.exists()) {
			readDataStore();
		} else {
			dataStore = new HashMap<>();

			writeDataStore();

		}
		
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readDataStore() {
		
		dataStore = (Map<Class, List>) xstream.fromXML(dataFile);
	}

	private void writeDataStore() throws FileNotFoundException {
		xstream.toXML(dataStore, new FileOutputStream(dataFile));
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getList(Class<T> tableClass) throws FileNotFoundException {

		List<T> dataList = dataStore.get(tableClass);
		if (dataList == null) {
			synchronized (dataStore) {
				dataList = dataStore.get(tableClass);
				if (dataList == null) {
					dataStore.put(tableClass, dataList = new ArrayList<T>());

					writeDataStore();

				}
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
	public <T> List<T> getAll(Class<T> tableClass) throws FileNotFoundException {

		return new ArrayList<T>(getList(tableClass));
	}

	@Override
	public <T> T getById(Class<T> tableClass, int id) throws IOException {
		List<T> list = getList(tableClass);
		for (T t : list) {
			if (t.hashCode() == id)
				return t;
		}
		return null;
	}

	@Override
	public synchronized <T> void save(Class<T> tableClass, T data) throws FileNotFoundException {
		List<T> dataList = getList(tableClass);
		if (!dataList.contains(data)) {
			dataList.add(data);
		}
		// data may have been modified and then need to be saved into the
		// underlying storage file.
		writeDataStore();
	}

	@Override
	public synchronized <T> void delete(Class<T> tableClass, T data) throws FileNotFoundException {
		List<T> dataList = getList(tableClass);
		dataList.remove(data);
		writeDataStore();
	}

	@Override
	public synchronized void save(Object... dataArray) throws IOException {
		for (Object data : dataArray) {
			if (data == null)
				continue;
			List dataList = getList(data.getClass());
			if (!dataList.contains(data)) {
				dataList.add(data);
			}
		}
		writeDataStore();
	}

	@Override
	public synchronized void delete(Object... dataArray) throws IOException {
		for (Object data : dataArray) {
			List dataList = getList(data.getClass());
			dataList.remove(data);
		}
		writeDataStore();
	}
}
