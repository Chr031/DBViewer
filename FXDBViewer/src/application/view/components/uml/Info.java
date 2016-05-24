package application.view.components.uml;

import javafx.geometry.Point3D;

class Info {
	Point3D speed;
	Point3D position;

	public Info(Point3D position) {
		this.speed = new Point3D(0, 0, 0);
		this.position = position;
	}
}