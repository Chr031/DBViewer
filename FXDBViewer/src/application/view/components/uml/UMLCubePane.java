package application.view.components.uml;

import javafx.geometry.Point3D;

public class UMLCubePane extends UMLAbstractPane {

	@Override
	protected Point3D calculateInitialControlPosition() {
		return new Point3D(random.nextDouble() * 10, random.nextDouble() * 10, getRadius() == 0 ? -100
				: -getRadius());
	}

	@Override
	protected Point3D normalizePosition(double radius, Point3D position) {
		// the position is outside of the cube...outside let's put it inside.
		// define first where is the closer face of the cube :
		double dMax = Math.max(Math.abs(position.getX()), Math.max(Math.abs(position.getY()), Math.abs(position.getZ())));
		return position.multiply(radius/dMax);
		
		
	}

}
