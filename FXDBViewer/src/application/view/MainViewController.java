package application.view;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.Model;
import application.model.ModelService;
import application.model.dataaccessor.ModelAccessor;
import application.model.dataaccessor.condition.Condition;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.ClassDescriptorFactory;
import application.service.AbstractServiceController;
import application.service.ActionService;
import application.utils.Callback;
import application.utils.Caller;
import application.view.components.HistoryPane;
import application.view.components.HistoryPane.HistoryData;
import application.view.components.uml.UMLAnnealingMapper;
import application.view.components.uml.UMLCubePane;
import application.view.components.uml.UMLMapper;
import application.view.components.uml.UMLPlanMapPane;
import application.view.components.uml.UMLSpherePane;
import application.view.saver.DataBinder;
import application.view.saver.FormSaver;

public class MainViewController extends AbstractServiceController implements TableAndFormService {

	private static final Logger log = LogManager.getLogger(MainViewController.class);

	private ModelService modelService;
	private Caller modelServiceUpdateCaller = this::initModelCombo;

	@FXML
	public MenuViewController menuController;

	@FXML
	public ComboBox<Model> modelCombo;

	@FXML
	public StackPane stackPane;
	@FXML
	public ScrollPane scroller;

	@FXML
	public VBox tableBox;

	@FXML
	public UMLSpherePane spherePane;
	@FXML
	public UMLPlanMapPane planPane;
	@FXML
	public UMLCubePane cubePane;
	@FXML
	UMLAnnealingMapper annealingPane;
	@FXML
	UMLMapper umlPane;

	@FXML
	public HistoryPane historyPane;
	@FXML
	public StackPane tabs;

	public MainViewController(ActionService actionService) throws Exception {
		setActionService(actionService);
		modelService = actionService.getModelService();
	}

	@FXML
	public void initialize() throws Exception {

		menuController.setBaseServiceController(this);
		umlPane.setModelService(modelService);
		
		initModelCombo();
		modelService.addModelListUpdateListener(modelServiceUpdateCaller);
	}

	private void initModelCombo() {

		Collection<Model> models = modelService.getModelList();
		modelCombo.getItems().clear();
		modelCombo.getItems().addAll(models);
		
	}

	public Model model;
	public ModelAccessor modelAccessor;

	@FXML
	private void handleComboAction() throws Exception {

		tableBox.getChildren().clear();

		model = modelCombo.getSelectionModel().getSelectedItem();
		List<Class<?>> classes = model.getObjectClassList();
		modelAccessor = model.getModelAccessor();

		log.info("Model selected : " + model.getModelName());

		Callback<Class<?>, Void> classActionCallback = new Callback<Class<?>, Void>() {

			@Override
			public Void call(Class<?> c) {
				try {
					loadList(c);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				return null;
			}
		};
		
		spherePane.setModel(model, classActionCallback);
		planPane.setModel(model, classActionCallback);
		cubePane.setModel(model, classActionCallback);
		// annealingPane.setModel(model);
		// annealingPane.initializeObjects(classActionCallback);
		// annealingPane.runAnnealingAlgorithm();
		umlPane.setModel(model);
		umlPane.initializeObjects(classActionCallback);

		for (Class<?> tableClass : classes) {

			Button button = new Button(tableClass.getSimpleName());
			button.setPrefWidth(120);
			tableBox.getChildren().add(button);

			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				try {
					loadList(tableClass);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			});
		}

		log.trace(tableBox.isVisible() + " " + tableBox.isManaged());
		log.trace(spherePane.isVisible() + " " + spherePane.isManaged());

	}

	public <T> void loadList(Class<T> tableClass) throws Exception {
		loadList(tableClass, null);
	}

	public <T> void loadList(Class<T> tableClass, Condition<T> where) throws Exception {
		ClassDescriptorFactory classDescriptorFactory = model.getClassDescriptorFactory();
		ClassDescriptor<T> tableDescriptor = classDescriptorFactory.getGridDescriptor(tableClass);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("TableView.fxml"));
		TableViewController<T> controller = new TableViewController<T>(this, tableDescriptor, modelAccessor, where);
		loader.setController(controller);
		Pane table = loader.load();

		if (where != null)
			historyPane.addHistoryNewPane(where.toString(), table);
		else
			historyPane.addHistoryFirstPane(tableDescriptor.getObjectName() + " list", table);

	}

	public <T> void loadForm(DataBinder<T> binder, Class<T> tableObject, FormSaver<T> saver) throws Exception {
		final ClassDescriptorFactory classDescriptorFactory = model.getClassDescriptorFactory();
		ClassDescriptor<T> formClassDescriptor = classDescriptorFactory.getFormDescriptor(tableObject);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("FormView.fxml"));
		FormViewController<T> controller = new FormViewController<T>(this, formClassDescriptor, modelAccessor, saver, binder);
		loader.setController(controller);

		Node form = loader.load();
		T data = binder.getData();

		historyPane.addHistoryNewPane(data == null ? "new " + tableObject.getSimpleName() : data.toString(), form);
	}

	public <T> void loadForm(DataBinder<T> binder, ClassDescriptor<T> formClassDescriptor, ModelAccessor modelAccessor, FormSaver<T> saver)
			throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("FormView.fxml"));
		FormViewController<T> controller = new FormViewController<T>(this, formClassDescriptor, modelAccessor, saver, binder);
		loader.setController(controller);

		Node form = loader.load();
		T data = binder.getData();

		historyPane.addHistoryNewPane(data == null ? "new " + formClassDescriptor.getObjectName() : data.toString(), form);
	}

	private HistoryData addTab(String tabName, URL fxmlRessource) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(fxmlRessource);
		Pane form = loader.load();

		return historyPane.addHistoryFirstPane(tabName, form);
	}

	public void removeActiveTab() {

		historyPane.removeActiveHistory();

	}

}
