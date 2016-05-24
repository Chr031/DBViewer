package application.model.descriptor.presenter;


public interface PresenterCallBack<T> {

	void call(PresenterI<T> presenter, Class<? extends T> objectClass) throws Exception;
	
}
