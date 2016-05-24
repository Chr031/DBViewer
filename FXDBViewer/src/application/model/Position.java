package application.model;

public class Position {

	private Class<?> clazz;
	
	private double x;
	private double y;

	public Position() {
		super();
	}

	public Position(Class<?> clazz, double x, double y) {
		super();
		this.setClazz(clazz);
		
		this.setX(x);
		this.setY(y);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getClassName() {
		 return clazz.getSimpleName();
		
	}

	

}