package application.view.saver;

import java.io.IOException;

import application.model.dataaccessor.ModelAccessor;

public class ModelAccessorDataBinder<T> implements DataBinder<T> {

	private final ModelAccessor modelAccessor;
	private final T originalData;

	public ModelAccessorDataBinder(ModelAccessor modelAccessor) {
		this(null, modelAccessor);
	}
	
	public  ModelAccessorDataBinder(T data, ModelAccessor modelAccessor) {
		this.originalData = data;
		this.modelAccessor = modelAccessor;
	}

	@Override
	public T getData() {
		return originalData;
	}

	@Override
	public void setData(T data, Object... dependingDataArray) throws IOException {

		modelAccessor.save(dependingDataArray);
		modelAccessor.save(data);

	}

}
