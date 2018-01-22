package arbrecouvrant;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Sommet {
	private double x, y;
	private String nom;
	private final double rayon=20;
	private static GraphicsContext calque;
	
	public Sommet(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static void setCalque(GraphicsContext contexte) {
		calque = contexte;
		calque.setStroke(Color.RED);
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void affiche() {
		calque.strokeOval(x-rayon, y-rayon, 2*rayon, 2*rayon);
		calque.fillText(nom, x-6, y+3);
	}
}