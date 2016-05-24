package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MessageViewController {
	@FXML
	private TextArea textArea;

	@FXML
	public void initialize() {

		/*
		 * for (Node toolbar : htmlEditor.lookupAll(".tool-bar")) {
		 * toolbar.setVisible(false); toolbar.setManaged(false); }
		 */

		/*textArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("text Changed");
				System.out.println(textArea.getText());
				
			}
		});*/
		
	}

	public void handleTextChanged() {

		System.out.println("Key typed");
		System.out.println(textArea.getText());
		
		
		
		
	}

}
