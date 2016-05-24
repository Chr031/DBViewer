package application.view.saver;

import java.io.IOException;

public interface DataBinder<T> {
	public T getData();
	public void setData(T data, Object... dependingDataArray) throws IOException; 
}