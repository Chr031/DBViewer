package application.view.components.uml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.model.ClassLink;
import application.model.Model;
import application.utils.Callback;
import application.utils.Geom;

public abstract class UMLAbstractMapper extends Pane {
	private static final Logger log = LogManager.getLogger(UMLAbstractMapper.class);

	protected Model model;

	protected final Map<Class<?>, Control> classMap;
	protected final Set<ControlLink> controlLinkSet;
	protected final Set<Link<Class<?>>> classLinkSet;

	public UMLAbstractMapper() {
		super();
		this.classMap = new HashMap<>();
		this.controlLinkSet = new HashSet<>();
		this.classLinkSet = new HashSet<>();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {

		this.model = model;

	}

	public void initializeObjects(Callback<Class<?>, Void> classActionCallback) {

		Model model = this.model;
		classMap.clear();
		controlLinkSet.clear();
		classLinkSet.clear();
		this.getChildren().clear();

		// place classes
		for (Class<?> tableClass : model.getObjectClassList()) {
			Button button = new Button(tableClass.getSimpleName());
			button.getStyleClass().add("umlButton");
			classMap.put(tableClass, button);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				classActionCallback.call(tableClass);
			});

			double[] startDrag = new double[2];
			button.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
				startDrag[0] = e.getX();
				startDrag[1] = e.getY();

			});

			button.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
				Point2D coord = button.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());
				button.setLayoutX(-startDrag[0] + coord.getX());
				button.setLayoutY(-startDrag[1] + coord.getY());
			});
			
			button.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
				this.onDragEnd();
			});
			
			this.getChildren().add(button);
		}

		// init links
		// 1 - inheritance
		for (Class<?> tableClass : classMap.keySet()) {
			Control tControl = classMap.get(tableClass);
			for (Class<?> childClass : model.getChildClassesList(tableClass)) {

				Line line = new Line();
				line.setStroke(Color.ORANGE);

				line.startXProperty().bind(Bindings.add(tControl.layoutXProperty(), Bindings.multiply(0.5, tControl.widthProperty())));
				line.startYProperty().bind(Bindings.add(tControl.layoutYProperty(), Bindings.multiply(0.5, tControl.heightProperty())));

				Control cControl = classMap.get(childClass);
				line.endXProperty().bind(Bindings.add(cControl.layoutXProperty(), Bindings.multiply(0.5, cControl.widthProperty())));
				line.endYProperty().bind(Bindings.add(cControl.layoutYProperty(), Bindings.multiply(0.5, cControl.heightProperty())));
				getChildren().add(line);

				Node arrow[] = getArrowDecoration(line, cControl.heightProperty(), cControl.widthProperty());
				getChildren().addAll(arrow);

				controlLinkSet.add(new ControlLink(tControl, cControl));
				controlLinkSet.add(new ControlLink(cControl, tControl));
				classLinkSet.add(new Link<Class<?>>(tableClass, childClass));
				line.toBack();
			}

			for (ClassLink classLink : model.getClassLinkList(tableClass)) {

				Line line = new Line();
				line.setStroke(Color.GRAY);

				line.startXProperty().bind(Bindings.add(tControl.layoutXProperty(), Bindings.multiply(0.5, tControl.widthProperty())));
				line.startYProperty().bind(Bindings.add(tControl.layoutYProperty(), Bindings.multiply(0.5, tControl.heightProperty())));

				Control cControl = classMap.get(classLink.getLinkedClass());
				line.endXProperty().bind(Bindings.add(cControl.layoutXProperty(), Bindings.multiply(0.5, cControl.widthProperty())));
				line.endYProperty().bind(Bindings.add(cControl.layoutYProperty(), Bindings.multiply(0.5, cControl.heightProperty())));
				getChildren().add(line);
				line.toBack();

				Node[] arrowNodes = getArrowLinkDecoration(classLink, line,
						cControl.heightProperty(), cControl.widthProperty(),tControl.heightProperty(), tControl.widthProperty());
				getChildren().addAll(arrowNodes);

				controlLinkSet.add(new ControlLink(tControl, cControl));
				controlLinkSet.add(new ControlLink(cControl, tControl));
				classLinkSet.add(new Link<Class<?>>(tableClass, classLink.getLinkedClass()));

			}
		}

	}

	protected void onDragEnd() {
		// TODO Auto-generated method stub
		
	}

	private Node[] getArrowDecoration(Line line, DoubleExpression heightProperty, DoubleExpression widthProperty) {
		return getArrowLinkDecoration(null, line, heightProperty, widthProperty, null, null);
	}

	/**
	 * Creates an arrow on the line, represented by a polygon and pointing to
	 * the control.
	 * 
	 * @param classLink
	 * 
	 * @param line
	 *            the segment on which the arrow must be drawn.
	 * @param hProp
	 *            the height of the surrounding rectangle
	 * @param wProp
	 *            the width of the surrounding rectangle
	 * @return a bound polygon
	 */
	private Node[] getArrowLinkDecoration(ClassLink classLink, Line line, 
			DoubleExpression hProp, DoubleExpression wProp,
			DoubleExpression hPropOri, DoubleExpression wPropOri) {
		Polygon triangle = new Polygon();
		Label[] lblCardinalities = new Label[2];
		if (classLink != null && classLink.getCardinality() != null) {
			lblCardinalities[0] = new Label(classLink.getCardinality().getBaseCardinalitySymbol());
			lblCardinalities[1] = new Label(classLink.getCardinality().getLinkedCardinalitySymbol());
			lblCardinalities[0].getStyleClass().add("cardinalityLabel");
			lblCardinalities[1].getStyleClass().add("cardinalityLabel");
		}
		
		List<ObservableValue<Number>> obsValues = new ArrayList<>();
		Collections.addAll(obsValues, line.endXProperty(), line.endYProperty(), line.startXProperty(), line.startYProperty());
		if (classLink != null && classLink.getCardinality() != null) {
			Collections.addAll(obsValues,
					lblCardinalities[0].heightProperty(), lblCardinalities[0].widthProperty(),
					lblCardinalities[1].heightProperty(), lblCardinalities[1].widthProperty()
					);
		}

		for (ObservableValue<Number> obsVal : obsValues) {
			if (obsVal != null)
				obsVal.addListener((ChangeListener<Number>) (o, n, n2) -> {

					double theta = computeArrowPosition(line, hProp.get(), wProp.get(), triangle);
					if (classLink != null && classLink.getCardinality() != null) {
						computeLabelPosition(lblCardinalities[0], theta + Math.PI, line.getStartX(), line.getStartY(), hPropOri.get(),
								wPropOri.get());
						computeLabelPosition(lblCardinalities[1], theta, line.getEndX(), line.getEndY(), hProp.get(), wProp.get());

					}

				});
		}

		triangle.setFill(line.getStroke());

		if (classLink != null && classLink.getCardinality() != null)
			return new Node[] { triangle, lblCardinalities[0], lblCardinalities[1] };
		else
			return new Node[] { triangle };
	}

	/**
	 * TODO factorize it with
	 * {@link #getBasePosition(double, DoubleExpression, DoubleExpression)}
	 * 
	 * @param line
	 * @param hProp
	 * @param wProp
	 * @param triangle
	 * @return the theta angle
	 */
	private double computeArrowPosition(Line line, double hProp, double wProp, Polygon triangle) {
		double tb = 8; // triangle base
		double th = 10; // triangle height;

		Point2D T0 = new Point2D(0, 0);
		Point2D T1 = new Point2D(tb / 2, th);
		Point2D T2 = new Point2D(-tb / 2, th);

		double theta = 0;
		double sinL = -(line.getStartX() - line.getEndX());
		double cosL = line.getStartY() - line.getEndY();
		Point2D basePosition = new Point2D(line.getEndX(), line.getEndY());

		// Get the angle
		if (sinL == 0) {
			if (cosL == 0)
				return 0;
			// 0 or PI
			theta = cosL > 0 ? 0 : Math.PI;
			basePosition = basePosition.add(0, Math.signum(cosL) * hProp / 2);
		} else {
			double tanTheta = sinL / cosL;
			theta = Math.atan(tanTheta);
			if (cosL < 0)
				theta += Math.PI;

			double tanCorner = wProp / hProp;
			if (tanCorner > tanTheta && -tanCorner < tanTheta) {
				basePosition = basePosition.add(-Math.signum(cosL) * hProp * tanTheta / 2, Math.signum(cosL) * hProp / 2);
			} else {
				basePosition = basePosition.add(-Math.signum(sinL) * wProp / 2, Math.signum(sinL) * wProp / (tanTheta * 2));
			}

		}
		// log.debug(basePosition);

		// do the rotation and the center translation:
		double[][] rm = Geom.get2DRotationMatix(theta);

		Point2D T0rt = Geom.apply2DTransformation(rm, T0).add(basePosition);
		Point2D T1rt = Geom.apply2DTransformation(rm, T1).add(basePosition);
		Point2D T2rt = Geom.apply2DTransformation(rm, T2).add(basePosition);
		// log.debug(T0rt + " " +T1rt + " " + T2rt);
		// set the points;
		triangle.getPoints().clear();

		triangle.getPoints().addAll(T0rt.getX(), T0rt.getY()); // point T0;
		triangle.getPoints().addAll(T1rt.getX(), T1rt.getY()); // point T1;
		triangle.getPoints().addAll(T2rt.getX(), T2rt.getY()); // point T2;

		return theta;
	}

	
	/**
	 * 
	 * @param lbl
	 * @param theta
	 * @param centerX
	 * @param centerY
	 * @param hProp
	 * @param wProp
	 */
	private void computeLabelPosition(Label lbl, double theta, double centerX, double centerY, double hProp, double wProp) {

		double cosL = Math.cos(theta);
		double sinL = Math.sin(theta);
		double tanTheta = Math.tan(theta);
		Point2D basePosition = new Point2D(0, 0);
		double tanCorner = wProp / hProp;
		if (tanCorner > tanTheta && -tanCorner < tanTheta) {
			// Base position is north or south
			basePosition = basePosition.add(-Math.signum(cosL) * tanTheta* hProp  / 2, Math.signum(cosL) * hProp / 2);
			
			// if north subtract the height of the label
			if (cosL<=0) basePosition = basePosition.subtract(0, lbl.getHeight());
			
			// if east subtract the width of the label
			if (sinL<=0) basePosition = basePosition.subtract(lbl.getWidth(),0);
			
		} else {
			// base position is west or east
			basePosition = basePosition.add(-Math.signum(sinL) * wProp / 2, Math.signum(sinL) * wProp / (tanTheta * 2));
			
			// if west subtract the width of the label;
			if (sinL>=0) basePosition = basePosition.subtract(lbl.getWidth(),0);
			
			// if south subtract the height of the label
			if (cosL>=0) basePosition = basePosition.subtract(0, lbl.getHeight());
		}
		
		log.debug(hProp + " " + wProp);
		
		basePosition = basePosition.add(centerX, centerY);

		lbl.layoutXProperty().set(basePosition.getX());
		lbl.layoutYProperty().set(basePosition.getY());

	}

}
