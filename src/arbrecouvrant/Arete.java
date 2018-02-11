package arbrecouvrant;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

/**
 * On utilise ici la classe Line
 */
public class Arete extends Line implements Comparable<Arete>, Serializable {
    private Sommet precedent;
    private Sommet suivant;
    private double poids;
    private boolean marque;

    public Arete(Sommet a, Sommet b) {
        super(a.getX(), a.getY(), b.getX(), b.getY());
        setStroke(new Color(1, 0, 0, 0.2));
        setStrokeWidth(5);

        precedent = a;
        suivant = b;
        marque = false;

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

    public Sommet getPrecedent() { return precedent; }
    public Sommet getSuivant()   { return suivant; }

    @Override
    public int compareTo(Arete a2) {
        return Double.compare(this.getPoids(), a2.getPoids());
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
        if(marque) {
            setStroke(new Color(0, 1, 0, 0.2));
        } else {
            setStroke(new Color(1, 0, 0, 0.2));
        }
    }

    public boolean isMarque() { return marque; }

    @Override
    public String toString() {
        String statut = (marque) ? "marqué" : "non-marqué";
        return "Arête ["+precedent.getNom()+" vers "+suivant.getNom()+"] "
               + "(poids="+Math.round(poids) + ", " + statut + ")";
    }
}
