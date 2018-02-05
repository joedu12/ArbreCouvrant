package arbrecouvrant;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/*
 * On réutilise la classe Circle :
 * - Les muttateurs et accesseurs des coordonnées sont
 * setCenter et getCenter.
 * - Un cercle se déplace avec la méthode relocate.
 */
public class Sommet extends Group {
	private double x, y;
	boolean marque;
	private final Circle cercle;
	private final Text texte;
	
	public Sommet(double x, double y, String nom) {
		cercle = new Circle(x, y, 20, new Color(1, 0, 0, 0.1));
		texte = new Text(x-5, y+3, nom);
		texte.setFill(Color.RED);
		getChildren().addAll(cercle, texte);
		marque = false;
		this.x = x;
		this.y = y;
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
		return texte.getText();
	}

    public boolean isMarque() { return marque; }
}
