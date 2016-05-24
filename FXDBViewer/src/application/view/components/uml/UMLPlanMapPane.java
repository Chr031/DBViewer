package application.view.components.uml;

import javafx.geometry.Point3D;


public class UMLPlanMapPane extends UMLAbstractPane {

	@Override
	protected Point3D calculateInitialControlPosition() {
		return new Point3D(random.nextDouble()*10,random.nextDouble()*10, 0);
	}

	@Override
	protected Point3D normalizePosition(double radius, Point3D newPosition) {
		return new Point3D(Math.max(Math.min(radius, newPosition.getX()),-radius),
				Math.max(-radius,Math.min(radius, newPosition.getY())),0);
	}

}
