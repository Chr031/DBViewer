package application.view.components.uml;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import application.model.Model;
import application.utils.Callback;
import application.utils.Geom;

public abstract class UMLAbstractPane extends BorderPane implements Runnable {

	protected final Pane plane;

	protected Thread runner;
	protected volatile boolean action;

	protected final Map<Class<?>, Control> classMap;
	protected final Map<Control, Info> infoMap;
	protected final Set<ControlLink> controlLinkSet;
	protected double stiffnessG = 0.001;
	protected double stiffnessL = 0.005;
	protected double breaksFactor = 0.95;

	protected int frameNumber = 16;
	protected Random random;

	protected Point3D rotationAngle = new Point3D(0, 0, 0);
	protected Point3D lastRotationModification;
	protected double[][] rotationMatrix = Geom.get3DRotationMatrix(rotationAngle.getX(), rotationAngle.getY(), rotationAngle.getZ());;

	protected Model model;

	public UMLAbstractPane() {
		super();

		random = new Random(System.currentTimeMillis());

		plane = new Pane();
		this.setCenter(plane);

		this.classMap = new ConcurrentHashMap<>();
		this.infoMap = new ConcurrentHashMap<>();
		this.controlLinkSet = new HashSet<>();

		plane.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			lastRotationModification = new Point3D(e.getX() - plane.getWidth() / 2, e.getY() - plane.getHeight() / 2, 0);
		});

		plane.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
			Point3D rotationModification = new Point3D(e.getX() - plane.getWidth() / 2, e.getY() - plane.getHeight() / 2, 0);
			Point3D rotationGradiant = rotationModification.subtract(lastRotationModification);
			lastRotationModification = rotationModification;

			double dPhi = Math.asin(rotationGradiant.getX() / getRadius());
			double dTheta = Math.asin(rotationGradiant.getY() / getRadius());

			rotationAngle = rotationAngle.add(new Point3D(dTheta, dPhi, 0));
			rotationMatrix = Geom.get3DRotationMatrix(rotationAngle.getX(), rotationAngle.getY(), rotationAngle.getZ());
		});

		plane.widthProperty().addListener((n) -> {
			if (runner == null && plane.getWidth() > 0)
				launch();
		});

	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model, Callback<Class<?>, Void> classActionCallback) {
		this.model = model;
		clearNodes();
		// place classes
		for (Class<?> tableClass : model.getObjectClassList()) {
			Label label = new Label(tableClass.getSimpleName());
			classMap.put(tableClass, label);
			label.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				classActionCallback.call(tableClass);
			});
			addControl(label);
		}

		// init links
		// 1 - inheritance
		for (Class<?> tableClass : classMap.keySet()) {
			Control tControl = classMap.get(tableClass);
			for (Class<?> childClass : model.getChildClassesList(tableClass)) {

				Line l = new Line();
				l.setStroke(Color.ORANGE);

				l.startXProperty().bind(Bindings.add(Bindings.divide(plane.widthProperty(), 2), tControl.translateXProperty()));
				l.startYProperty().bind(Bindings.add(Bindings.divide(plane.heightProperty(), 2), tControl.translateYProperty()));

				Control cControl = classMap.get(childClass);
				l.endXProperty().bind(Bindings.add(Bindings.divide(plane.widthProperty(), 2), cControl.translateXProperty()));
				l.endYProperty().bind(Bindings.add(Bindings.divide(plane.heightProperty(), 2), cControl.translateYProperty()));
				plane.getChildren().add(l);

				controlLinkSet.add(new ControlLink(tControl, cControl));
				controlLinkSet.add(new ControlLink(cControl, tControl));

			}

			for (Class<?> childClass : model.getLinkedClassesList(tableClass)) {

				Line l = new Line();
				l.setStroke(Color.GREY);

				l.startXProperty().bind(Bindings.add(Bindings.divide(plane.widthProperty(), 2), tControl.translateXProperty()));
				l.startYProperty().bind(Bindings.add(Bindings.divide(plane.heightProperty(), 2), tControl.translateYProperty()));

				Control cControl = classMap.get(childClass);
				l.endXProperty().bind(Bindings.add(Bindings.divide(plane.widthProperty(), 2), cControl.translateXProperty()));
				l.endYProperty().bind(Bindings.add(Bindings.divide(plane.heightProperty(), 2), cControl.translateYProperty()));
				plane.getChildren().add(l);

				controlLinkSet.add(new ControlLink(tControl, cControl));
				controlLinkSet.add(new ControlLink(cControl, tControl));

			}
		}

	}

	protected void addControl(Control control) {
		plane.getChildren().add(control);

		control.layoutXProperty().bind(
				Bindings.subtract(Bindings.multiply(0.5d, plane.widthProperty()), Bindings.divide(control.widthProperty(), 2)));
		control.layoutYProperty().bind(
				Bindings.subtract(Bindings.multiply(0.5d, plane.heightProperty()), Bindings.divide(control.heightProperty(), 2)));

		Point3D initialPosition = calculateInitialControlPosition();
		infoMap.put(control, new Info(initialPosition));

	}

	protected abstract Point3D calculateInitialControlPosition();

	protected void clearNodes() {
		plane.getChildren().clear();
		infoMap.clear();
		classMap.clear();

	}

	public void launch() {
		action = !action;

		if (action) {
			runner = new Thread(this);
			runner.setDaemon(true);
			// action = true;
			runner.start();
		}
	}

	@Override
	public void run() {
		while (action) {

			double radius = getRadius();

			// calculates the new speed of each present control
			for (Control control : infoMap.keySet()) {
				Point3D acceleration = new Point3D(0, 0, 0);
				Info controlInfo = infoMap.get(control);

				for (Control other : infoMap.keySet()) {
					if (other == control)
						continue;

					Point3D otherPosition = infoMap.get(other).position;
					Point3D vectPO = otherPosition.subtract(controlInfo.position);
					// length of the vector;
					double n = vectPO.distance(0, 0, 0);

					// Force value
					double f = stiffnessG * (n - radius * 3);

					// force vector =
					Point3D force = vectPO.multiply(f);
					acceleration = acceleration.add(force);

					// look for links
					if (controlLinkSet.contains(new ControlLink(control, other))) {
						double lf = stiffnessL * (n - radius / 2);
						force = vectPO.multiply(lf);
						acceleration = acceleration.add(force);
					}

				}

				controlInfo.speed = controlInfo.speed.add(acceleration).multiply(breaksFactor);

			}

			// resolves the new position and apply the modification
			for (Control control : infoMap.keySet()) {
				// Point3D position = new Point3D(control.getTranslateX(),
				// control.getTranslateY(), control.getTranslateZ());
				// position = position.add(speedMap.get(control).multiply(1d /
				// frameNumber));
				Info controlInfo = infoMap.get(control);
				Point3D newPosition = controlInfo.position.add(controlInfo.speed.multiply(1d / frameNumber));
				final Point3D normalizedPosition = normalizePosition(radius, newPosition);

				// rebuild speed according to the normalized position
				Point3D normalizedSpeed = normalizedPosition.subtract(controlInfo.position);
				controlInfo.speed = normalizedSpeed.multiply(frameNumber);

				controlInfo.position = normalizedPosition;

				Platform.runLater(() -> {
					applyControlPosition(radius, control, normalizedPosition);
				});
			}

			try {
				// System.out.println(".");
				Thread.sleep(1000 / frameNumber);
			} catch (InterruptedException e) {
				e.printStackTrace();
				action = false;
				break;
			}
		}
	}

	/**
	 * Method that makes the form of the sphere. To be overridden to change the
	 * form...
	 * 
	 * @param radius
	 * @param newPosition
	 * @return
	 */
	protected abstract Point3D normalizePosition(double radius, Point3D newPosition);

	protected double getRadius() {
		return Math.min(plane.getWidth(), plane.getHeight()) / 3;
	}

	protected void applyControlPosition(double radius, Control control, final Point3D normalizedPosition) {

		Point3D rotatedPosition = Geom.apply3DTransformation(rotationMatrix, normalizedPosition);

		control.setTranslateX(rotatedPosition.getX());
		control.setTranslateY(rotatedPosition.getY());
		control.setTranslateZ(rotatedPosition.getZ());

		control.setScaleX(1 - rotatedPosition.getZ() * 0.46d / radius);
		control.setScaleY(1 - rotatedPosition.getZ() * 0.46d / radius);
	}

	
}