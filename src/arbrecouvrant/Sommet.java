package arbrecouvrant;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * On réutilise la classe Circle :
 * - Les muttateurs et accesseurs des coordonnées sont
 * setCenter et getCenter.
 * - Un cercle se déplace avec la méthode relocate.
 */
public class Sommet extends Circle {

	public Sommet(double x, double y) {
		super(x, y, 20, new Color(1, 0, 0, 0.1));
	}

	public double getDistance(Sommet autre) {
       double dX = Math.abs(this.getCenterX() - autre.getCenterX());
       double dY = Math.abs(this.getCenterY() - autre.getCenterY());
       return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}
}
