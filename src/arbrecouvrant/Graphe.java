package arbrecouvrant;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

/*
 * Classe qui contiendra l'ensemble des points et arêtes
 */
public class Graphe extends StackPane {
	   private GraphicsContext calque;
	   private ArrayList<Sommet> sommets = new ArrayList<>();
	   
	    /**
	     * Ajoute un sommet à chaque clic de la souris
	     */
	   public Graphe() {  
		      Canvas dessin = new Canvas(1920, 1024);
		      calque = dessin.getGraphicsContext2D();  
		      Sommet.setCalque(calque);
		      setOnMouseClicked(evt -> { tracerSommet(evt.getX(), evt.getY()); } );
		      getChildren().add(dessin);
	   }

	   /*
	    * Efface le l'ensemble du graphe via un évènement onAction sur un bouton dans le FXML
	    */
	   public void toutEffacer() { 
	      calque.clearRect(0, 0, getWidth(), getHeight()); 
	      sommets.clear();
	   }
	   
	   /*
	    * Calcule la distance entre les deux premiers sommets via un évènement onAction sur un bouton dans le FXML
	    */
	   public void calculDistance() {
		   System.out.println("Distance : " + sommets.get(0).getDistance(sommets.get(1)));
	   }

	   /*
	    * Dessine un cercle dans lequel on a son numéro
	    */
	   public void tracerSommet(double x, double y) {
	      Sommet sommet = new Sommet(x, y);
	      sommets.add(sommet);
	      sommet.setNom(""+sommets.size());
	      sommet.affiche();
	   }
}
