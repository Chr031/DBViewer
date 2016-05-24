package application.service;


public abstract class AbstractServiceController {

	private AbstractServiceController baseServiceController;

	private ActionService actionService;

	public ActionService getActionService() {
		if (baseServiceController != null)
			return baseServiceController.getActionService();
		else
			return actionService;
	}

	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}

	public void setBaseServiceController(AbstractServiceController baseServiceController) {
		this.baseServiceController = baseServiceController;

	}

	
}
