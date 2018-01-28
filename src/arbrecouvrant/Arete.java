/**
 * 
 */
package arbrecouvrant;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * On utilise ici la classe Line
 */
public class Arete extends Line {
	private Sommet precedent;
	private Sommet suivant;
	private double poids;
	
	public Arete(Sommet a, Sommet b) {
		super(a.getX(), a.getY(), b.getX(), b.getY());
		setStroke(new Color(1, 0, 0, 0.1));
		setStrokeWidth(5);
		
		precedent = a;
		suivant = b;
		
		calculerPoids();
	}
	
	private void calculerPoids() {
	       double dX = Math.abs(precedent.getX() - suivant.getX());
	       double dY = Math.abs(precedent.getY() - suivant.getY());
	       poids = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}
	
	public double getPoids() {
		return poids;
	}
}
