package application.view;

import java.io.IOException;

import javafx.fxml.FXML;
import application.service.AbstractServiceController;

public class MenuViewController extends AbstractServiceController {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(MenuViewController.class);

	@FXML
	public void handleLoadModel() {
		try {
			getActionService().loadModel();
		} catch (IOException e) {
			log.error("Unable to load the model ", e);
		}
	}

	@FXML
	public void handleExit() {
		getActionService().exit();
	}

	@FXML
	public void handleLogout() {
		try {
			getActionService().logout();
		} catch (IOException e) {
			log.error("Logout error ", e);
			getActionService().exit();
		}
	}

}
