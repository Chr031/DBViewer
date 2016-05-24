package application.view;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.Model;
import application.model.ModelFactory;
import application.model.ModelService;
import application.view.components.JarAutoCompleter;

public class LoadModelController {

	private static final Logger log = LogManager.getLogger(LoadModelController.class);

	@FXML
	private TextField modelNameTextField;
	@FXML
	private TextField fileTextField;
	@FXML
	private TextArea packageTextArea;
	@FXML
	private ModelService modelService;
	private Callback<Void, Void> closeCallback;

	private Stage getStageFromEvent(Event e) {
		Node source = (Node) e.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		return stage;
	}

	protected Callback<Void, Void> getCloseCallback() {
		return closeCallback;
	}

	public void setCloseCallback(Callback<Void, Void> closeCallback) {
		this.closeCallback = closeCallback;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	@FXML
	public void BrowseFile(ActionEvent actionEvent) {
		final Stage stage = getStageFromEvent(actionEvent);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.setTitle("Open Model jar");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Jar Files", "*.jar", "*.war"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			fileTextField.setText(selectedFile.getAbsolutePath());
			if (modelNameTextField.getText() == null || modelNameTextField.getText().length() == 0)
				modelNameTextField.setText(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf(".")));
		}
	}

	@FXML
	public void ctrlSpace(KeyEvent ke) throws IOException {
		if (ke.isControlDown() && ke.getCode().equals(KeyCode.SPACE)) {
			log.debug("autocompletion called");
			String textBefore = packageTextArea.getText(0, packageTextArea.getCaretPosition());
			Path caretPath = findCaret(packageTextArea);
			if (caretPath != null) {
				Point2D caretPosition = findScreenLocation(caretPath);
				String textToAdd = new JarAutoCompleter(new File(fileTextField.getText())).getAutocompletePossibility(textBefore,
						caretPosition.getX(), caretPosition.getY()
								+ caretPath.getBoundsInLocal().getHeight());
				String textToSet = textBefore + textToAdd;
				if (packageTextArea.getCaretPosition() + 1 < packageTextArea.getLength()) {
					textToSet += packageTextArea.getText(packageTextArea.getCaretPosition() + 1, packageTextArea.getLength());
				}
				packageTextArea.setText(textToSet);
			}
		}
	}

	private Path findCaret(Parent parent) {
		// Warning: this is an ENORMOUS HACK
		for (Node n : parent.getChildrenUnmodifiable()) {
			if (n instanceof Path) {
				return (Path) n;
			} else if (n instanceof Parent) {
				Path p = findCaret((Parent) n);
				if (p != null) {
					return p;
				}
			}
		}
		return null;
	}

	private Point2D findScreenLocation(Node node) {
		double x = 0;
		double y = 0;
		for (Node n = node; n != null; n = n.getParent()) {
			Bounds parentBounds = n.getBoundsInParent();
			x += parentBounds.getMinX();
			y += parentBounds.getMinY();
		}
		Scene scene = node.getScene();
		x += scene.getX();
		y += scene.getY();
		Window window = scene.getWindow();
		x += window.getX();
		y += window.getY();
		Point2D screenLoc = new Point2D(x, y);
		return screenLoc;
	}

	@FXML
	public void loadModel() throws Exception {
		
		String modelName = modelNameTextField.getText();
		File jarFile = new File(fileTextField.getText());
		String [] packages = packageTextArea.getText().split("[;,]");		
		for (int i = 0;i<packages.length;i++) {
			packages[i] = packages[i].trim();
		}
		Model newModel = ModelFactory.createModel(modelName, jarFile, packages );
		
		modelService.addModel(newModel);
		
		closeCallback.call(null);
	}

	@FXML
	public void cancel() {
		closeCallback.call(null);

	}

}
