package application.model.descriptor.presenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import application.model.descriptor.annotations.PresentationType;
import application.model.descriptor.objectaccessor.Accessor;
import application.utils.Callback;
import application.view.builder.FormBuilder;

public class JFXStringPresenter<T> extends JFXPresenter<T, String, TextInputControl> {

	private Pane resizableParentPane;
	
	public JFXStringPresenter(Accessor<T, String> fieldAccessor) {
		super(fieldAccessor);
		
		if (( fieldAccessor.getAnnotation(application.model.descriptor.annotations.TextArea.class)) != null) {
			TextArea textArea = new TextArea();
			this.fxControl = textArea;
			textArea.setWrapText(true);
			new FormBuilder().initSizeChangeListener(fxControl, ()->{return resizableParentPane;});
		} else {
			this.fxControl = new TextField();
		}
		
		if (accessor.isReadOnly()) { 
			fxControl.setEditable(false);
			fxControl.setBackground(Background.EMPTY);
			fxControl.setBorder(Border.EMPTY);
			fxControl.setFocusTraversable(false);
		}

	}

	public void setNodeContent(T data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		if (data == null)
			return;
		fxControl.setText(accessor.get(data));

	}

	public void setPropertyValue(T data) throws IllegalArgumentException, IllegalAccessException, InstantiationException {

		accessor.set(data, fxControl.getText());

	}

	@Override
	public void addChangeCallback(Callback<Void, Void> voidCallback) {
		fxControl.textProperty().addListener((s) -> {
			voidCallback.call(null);
		});

	}

	public Pane getResizableParentPane() {
		return resizableParentPane;
	}

	public void setResizableParentPane(Pane resizableParentPane) {
		this.resizableParentPane = resizableParentPane;
	}

}
