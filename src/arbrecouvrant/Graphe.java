package arbrecouvrant;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

/*
 * Classe qui contiendra l'ensemble des points et arêtes
 */
public class Graphe extends Pane {
	   private ArrayList<Sommet> listSommet = new ArrayList<>();
	   private ArrayList<Arete>  listArete  = new ArrayList<>();
	   
		/**
		 * Un graphe contiens l'ensemble des sommets et aretes 
		 */
	   public Graphe() {
		   // évènement du Pane qui crée le sommet
		   setOnMouseClicked(evt -> {
			   // sélectionne le Sommet (élément parrent d'un Text ou d'un Circle)
			   Node element = evt.getPickResult().getIntersectedNode().getParent();
			   
			   // ajoute un sommet à chaque clic gauche
			   if (evt.getButton() == MouseButton.PRIMARY) {
				   if(!(element instanceof Sommet)) {
					   tracerSommet(evt.getX(), evt.getY());
				   }
			   }
			   
			   // supprime un sommet à chaque clic droit
			   if (evt.getButton() == MouseButton.SECONDARY) {
			       if (element instanceof Sommet) {
			           listSommet.remove(element);
			           rafraichir();
			       }
			       if (evt.getPickResult().getIntersectedNode() instanceof Arete) {
			           listArete.remove(evt.getPickResult().getIntersectedNode());
			           rafraichir();
			       }
			   }
		   });
		   

		   // Ajout une arête à chaque clic sur l'un des sommets
		   ArrayList<Sommet> couple = new ArrayList<>();
		   setOnMousePressed(evt -> {
			   Node element = evt.getPickResult().getIntersectedNode().getParent();
			   if(element instanceof Sommet) {
				   couple.add((Sommet) element);
			   }
		   });
		   
		   setOnMouseReleased(evt -> {
			   Node element = evt.getPickResult().getIntersectedNode().getParent();
			   if(element instanceof Sommet) {
				   couple.add((Sommet) element);
				   if(couple.size() == 2) {
					   if(couple.get(0).getNom() != couple.get(1).getNom()) {
						   // on cherche deux sommets différents avant de tracer
						   tracerArete(couple.get(0), couple.get(1));
					   }
				   }
				   couple.clear();
			   }
		   });
	   }

	   /*
	    * Efface le l'ensemble du graphe via un évènement onAction sur un bouton dans le FXML
	    */
	   public void toutEffacer() {
		  getChildren().clear(); 
	      listSommet.clear();
	      listArete.clear();
	   }
	   
	   /*
	    * Efface le Pane puis ré-affiche les arêtes ainsi que
	    * l'ensemble des sommets en mettant à jour leurs noms
	    */
	   public void rafraichir() {
		   getChildren().clear();
		  
		   for(int i=0; i<listSommet.size(); i++) {
			   listSommet.get(i).setNom(""+i);
			   getChildren().add(listSommet.get(i));
		   }
		   
		   for(Arete arete : listArete) {
			   getChildren().add(arete);
		   }
	   }
	   
	   /*
	    * Calcule la distance entre les deux premiers sommets via un évènement onAction sur un bouton dans le FXML
	    */
	   public void calculDistance() {
		   System.out.println("Distance : " + listSommet.get(0).getDistance(listSommet.get(1)));
	   }

	   /*
	    * Dessine un cercle dans lequel on a son numéro
	    */
	   public void tracerSommet(double x, double y) {
		      Sommet sommet = new Sommet(x, y, ""+listSommet.size());	      
		      listSommet.add(sommet);
		      getChildren().add(sommet);
	   }
	   
	   /*
	    * Dessine une arête entre deux sommets
	    */
	   public void tracerArete(Sommet a, Sommet b) {
		   Arete arete = new Arete(a, b);
		   listArete.add(arete);
		   getChildren().add(arete);
		   System.out.println(a.getNom() +" lié avec "+ b.getNom() + ", poids : "+arete.getPoids());
	   }
}
