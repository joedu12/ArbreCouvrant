package arbrecouvrant;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.Serializable;

/*
 * On réutilise la classe Circle :
 * - Les muttateurs et accesseurs des coordonnées sont
 * setCenter et getCenter.
 * - Un cercle se déplace avec la méthode relocate.
 */
public class Sommet extends Group implements Serializable {
	private double x, y;
	private String nom;
	private boolean marque;
	private transient Circle cercle;
	private transient Text texte;

	public Sommet(double x, double y, String nom) {
        this.x = x;
        this.y = y;
        this.nom = nom;
        cercle = new Circle(x, y, 20, new Color(1, 0, 0, 0.1));
        texte = new Text(x - 5, y + 3, this.nom);
        texte.setFill(Color.RED);
        getChildren().addAll(cercle, texte);
        marque = false;
    }

    public void setX(double valeur) {
		this.x = valeur;
		setTranslateX(valeur);
	}
	
	public void setY (double valeur) {
		this.y = valeur;
		setTranslateY(valeur);
	}

    public void setNom(String valeur) {
        texte.setText(valeur);
    }

    public void setMarque(boolean marque) {
	    this.marque = marque;
	    texte.setFill(Color.GREEN);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String getNom() {
		return nom;
	}

    public boolean isMarque() { return marque; }

    public boolean equals(Sommet sommet) {
        if(x == sommet.getX() && y == sommet.getY()) { return true; }
        else { return false; }
    }
}
