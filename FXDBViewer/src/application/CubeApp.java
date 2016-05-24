package application;

import java.io.IOException;

import application.model.ModelService;
import application.service.ActionService;
import application.view.LoadModelController;
import application.view.LoginViewController;
import application.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CubeApp extends Application {

	private Stage primaryStage;
	private ActionService actionService;

	@Override
	public void init() throws Exception {
		actionService = new ActionService(this);
	}

	@Override
	public void start(Stage primaryStage) {
		try {

			this.primaryStage = primaryStage;
			primaryStage.setTitle("DB Viewer");

			loadLoginView();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadLoginView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/LoginView.fxml"));
		
		BorderPane root = loader.load();

		LoginViewController controller = loader.getController();
		controller.setActionService(actionService);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		if (primaryStage.getScene() != null) {
			primaryStage.hide();
		}
		
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();

	}

	public void loadMainView() throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/MainView.fxml"));
		loader.setController( new MainViewController(actionService));		
		BorderPane mainPane = loader.load();
			
		
		Scene scene = new Scene(mainPane, 700, 700);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.hide();
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void loadModel(ModelService modelService) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/LoadModelView.fxml"));
		BorderPane mainPane = loader.load();
		
		Stage stage = new Stage(StageStyle.UTILITY);
		LoadModelController controller = loader.getController();
		controller.setCloseCallback((Void v)->{stage.close(); return null;});
		controller.setModelService(modelService);
		
		Scene scene = new Scene(mainPane, mainPane.getPrefWidth(), mainPane.getPrefHeight());
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setTitle("Load an object model");
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		
		stage.showAndWait();
		
		
	}

}
