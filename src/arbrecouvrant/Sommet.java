package arbrecouvrant;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.HashSet;

/*
 * On réutilise la classe Circle et Text
 */
public class Sommet extends Group implements Serializable {
	private double x, y;
	private String nom;
	private HashSet<String> listeCycle;
	private boolean marque;
	private transient Circle cercle;
	private transient Text texte;

	public Sommet(double x, double y, String nom) {
        this.x = x;
        this.y = y;
        this.nom = nom;
        this.listeCycle = new HashSet<>();
        cercle = new Circle(x, y, 20, new Color(1, 0, 0, 0.1));
        texte = new Text(x - 5, y + 3, this.nom);
        texte.setFill(Color.RED);
        getChildren().addAll(cercle, texte);
        marque = false;
    }

    public void setX(double valeur) { this.x = valeur; }
	public void setY (double valeur) { this.y = valeur; }
    public void setNom(String valeur) {
        texte.setText(valeur);
    }
    public void setMarque(boolean marque) {
		this.marque = marque;
		texte.setFill(Color.GREEN);
	}
	public void setListeCycle(HashSet<String> listeCycle) { this.listeCycle = listeCycle; }
	
	public double getX() { return x; }
	public double getY() { return y; }
	public String getNom() { return nom; }
    public boolean isMarque() { return marque; }
	public HashSet<String> getListeCycle() { return listeCycle; }

	public boolean equals(Sommet sommet) {
		if(x == sommet.getX() && y == sommet.getY()) { return true; }
		else { return false; }
	}
	@Override
	public String toString() {
		String statut = (marque) ? "marqué" : "non-marqué";
		return "Sommet n°" + nom + ": ["+x+", "+y+"]: "
               + "(" + statut + ", cycle : " + listeCycle + ")";
	}
}
