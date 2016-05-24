package application.service;

import java.io.IOException;

import application.CubeApp;
import application.model.ModelService;
import application.objects.ApplicationUser;

public class ActionService {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ActionService.class);
	
	private CubeApp cubeApp;
	private ModelService modelService;

	public ActionService(CubeApp main) {
		this.cubeApp = main;
	}

	public void login(String login, String password) throws Exception {

		ApplicationUser user = new ApplicationUser(login);
		user.checkPassword(password);

		cubeApp.loadMainView();

	}

	public void logout() throws IOException {
		cubeApp.loadLoginView();

	}

	public void loadModel() throws IOException {
		
			cubeApp.loadModel(modelService);
		
	}

	public void exit() {
		System.exit(0);
	}

	public synchronized ModelService getModelService() throws Exception {
		if (modelService == null) {
			modelService = new ModelService();
		}
		return modelService;
	}

}
