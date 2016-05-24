package application.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.PointLight;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import application.service.AbstractServiceController;

public class LoginViewController extends AbstractServiceController {

	@FXML
	private PointLight pointLight;

	@FXML
	private Box cube;

	@FXML
	private TextField login;

	@FXML
	private PasswordField password;

	public LoginViewController() {

	}

	@FXML
	public void initialize() {
		cubeShine();
		cube.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {cubeShine();});
	}
	
	
	public void cubeShine() {
		final Color startColor = pointLight.getColor();
		Runnable r = () -> {
			try {
				double t=0;
				double step = 0.07;
				while (t>=0) {
					final Color c = startColor.interpolate(Color.rgb(253, 234, 202),Math.pow(t,4));
					Platform.runLater(() -> {
						pointLight.setColor(c);
					});
					t+=step;
					if (t>=1 ) step = -2*step;
					Thread.sleep(1000/24);
					
				}
				
			} catch (InterruptedException ie) {
				// exit the loop silently!
			}

		};
		new Thread(r).start();
	}

	@FXML
	public void handleLoginProcess() {
		try {
			getActionService().login(login.getText(), password.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Box getCube() {
		return cube;
	}

	public void setCube(Box cube) {
		this.cube = cube;
	}

	public TextField getLogin() {
		return login;
	}

	public void setLogin(TextField login) {
		this.login = login;
	}

	public PasswordField getPassword() {
		return password;
	}

	public void setPassword(PasswordField password) {
		this.password = password;
	}

}
