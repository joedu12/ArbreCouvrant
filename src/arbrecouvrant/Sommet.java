package arbrecouvrant;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
 * Classe qui modélise un sommet représenté par un cercle de rayon 20px dans le Graphe
 */
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
	
	public double getX() { return x; }
	public double getY() { return y; }

	public int getDistance(Sommet autre) {
       int dX = (int) Math.abs(this.x - autre.getX());
       int dY = (int) Math.abs(this.y - autre.getY());
       return (int) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}
      
	public void affiche() {
		calque.strokeOval(x-rayon, y-rayon, 2*rayon, 2*rayon);
		calque.fillText(nom, x-6, y+3);
	}
}
