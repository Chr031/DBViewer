package application.view.components.uml;

import javafx.geometry.Point3D;

public class UMLSpherePane extends UMLAbstractPane {



	protected Point3D calculateInitialControlPosition() {
		return new Point3D(random.nextDouble() * 10, random.nextDouble() * 10, getRadius() == 0 ? -100
				: -getRadius());
	}


	/**
	 * Method that makes the form of the sphere.To be overridden to change the form... 
	 * @param radius
	 * @param newPosition
	 * @return
	 */
	protected Point3D normalizePosition(double radius, Point3D newPosition) {
		final Point3D normalizedPosition = newPosition.multiply(radius / newPosition.distance(0, 0, 0));
		return normalizedPosition;
	}

}