package application.utils;

import  javafx.geometry.Point2D;

import javafx.geometry.Point3D;

public class Geom {
	
	
	public static double[][] get2DRotationMatix(double angle_x) {
		
		double cos = Math.cos(angle_x);
		double sin = Math.sin(angle_x);
		double m[][] = {{ cos, -sin},{sin,cos}};
		return m;
		
	}
	
	public static Point2D apply2DTransformation(double[][] transformationMatrix, Point2D point) {
		double[][] result = multiplyMatrix(transformationMatrix, new double[][] { { point.getX() }, { point.getY() } });
		return new Point2D(result[0][0], result[1][0]);
	}
	
	/**
	 * Optimized algorithm to get a 3d rotation matrix...
	 * @param angle_x
	 * @param angle_y
	 * @param angle_z
	 * @return
	 */
	public static double[][] get3DRotationMatrix(double angle_x, double angle_y, double angle_z) {

		double A = Math.cos(angle_x);
		double B = Math.sin(angle_x);
		double C = Math.cos(angle_y);
		double D = Math.sin(angle_y);
		double E = Math.cos(angle_z);
		double F = Math.sin(angle_z);

		double AD = A * D;
		double BD = B * D;

		double mat[][] = new double[3][3];

		mat[0][0] = C * E;
		mat[0][1] = -C * F;
		mat[0][2] = D;
		mat[1][0] = BD * E + A * F;
		mat[1][1] = -BD * F + A * E;
		mat[1][2] = -B * C;
		mat[2][0] = -AD * E + B * F;
		mat[2][1] = AD * F + B * E;
		mat[2][2] = A * C;

		return mat;
	}

	public static Point3D apply3DTransformation(double[][] transformationMatrix, Point3D point) {
		double[][] result = multiplyMatrix(transformationMatrix, new double[][] { { point.getX() }, { point.getY() }, { point.getZ() } });
		return new Point3D(result[0][0], result[1][0], result[2][0]);
	}

	/**
	 * Should be private since the implementation is made with no checks ...
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] multiplyMatrix(double[][] a, double[][] b) {

		double[][] result = new double[a.length][b[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				for (int k = 0; k < b.length; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return result;

	}
}
