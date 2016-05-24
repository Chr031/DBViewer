package application.view.components;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public abstract class AutoCompleter {

	public abstract String getContext(String text);

	public abstract String[] getPossibilities(String context);

	public String getAutocompletePossibility(String text, double x, double y) {

		String context = getContext(text);
		String[] choices = getPossibilities(context);

		if (choices == null)
			return null;

		String choice = presentWindow(choices, x, y);
		// revert super position
		if (choice == null ) return "";
		
		
		int i = 1;
		// empty loop 
		while (i < choice.length() && !context.endsWith(choice.substring(0, i))) 
			{i++;}
		
		if (i == choice.length())
			return choice;
		else
			return choice.substring(i);

	}

	private String presentWindow(String[] choices, double x, double y) {

		Stage autoCompleteStage = new Stage(StageStyle.TRANSPARENT);
		ScrollPane scroller = new ScrollPane();
		VBox vbox = new VBox(3);
		final String finalChoice[] = new String[1];
		for (String choice : choices) {
			Label lblChoice = createChoiceLabel(choice, finalChoice, (Void v) -> {
				autoCompleteStage.close();
				return null;
			});
			vbox.getChildren().add(lblChoice);

		}
		scroller.setContent(vbox);
		Scene scene = new Scene(scroller, 324, 200);
		scene.addEventHandler(KeyEvent.ANY, (ke) -> {
			if (ke.getCode().equals(KeyCode.ESCAPE)) {
				autoCompleteStage.close();
			}
		});
		autoCompleteStage.setScene(scene);
		autoCompleteStage.setAlwaysOnTop(true);
		autoCompleteStage.setX(x);
		autoCompleteStage.setY(y);
		autoCompleteStage.showAndWait();

		return finalChoice[0];
	}

	private Label createChoiceLabel(String choice, final String[] finalChoice, Callback<Void, Void> closeCallBack) {
		Label choiceLabel = new Label(choice);
		choiceLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			finalChoice[0] = choiceLabel.getText();
			closeCallBack.call(null);
		});

		return choiceLabel;
	}

}
