package arbrecouvrant;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/*
 * Classe qui contiendra l'ensemble des points et arêtes
 */
public class Graphe extends Pane {
	   private ArrayList<Sommet> listSommet = new ArrayList<>();
	   private ArrayList<Text> listText = new ArrayList<>();
	   public static boolean isDragging;
	   MouseGestures mg = new MouseGestures();
	   
		/**
		 * Ajoute un sommet à chaque clic de la souris
		 */
	   public Graphe() {
		   // évènement du Pane qui crée le sommet
		   setOnMouseClicked(evt -> {
			   if(!isDragging) {
				   tracerSommet(evt.getX(), evt.getY());
			   }
		   } );
	   }

	   /*
	    * Efface le l'ensemble du graphe via un évènement onAction sur un bouton dans le FXML
	    */
	   public void toutEffacer() {
		  getChildren().clear(); 
	      listSommet.clear();
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
		      Sommet sommet = new Sommet(x, y);
		      listSommet.add(sommet);
		      getChildren().addAll(sommet);	      
		      
		      Text text = new Text(x-5, y+3,""+listSommet.size());
		      text.setFill(Color.RED);
		      listText.add(text);
		      getChildren().addAll(text);
		      
		      // TODO: lier text et sommet dans un overlay
		      
		      mg.makeDraggable(sommet);
	   }
	   
	    public static class MouseGestures {

	        double orgSceneX, orgSceneY; // position d'origine
	        double orgTranslateX, orgTranslateY; // position après drag'n'drop

	        public void makeDraggable(Node node) {
	            node.setOnMousePressed(sommetOnMousePressed);
	            node.setOnMouseDragged(sommetOnMouseDragged);
	        }

	        EventHandler<MouseEvent> sommetOnMousePressed = new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent evt) {
	                orgSceneX = evt.getSceneX();
	                orgSceneY = evt.getSceneY();

	                Sommet s = ((Sommet) (evt.getSource()));
	                orgTranslateX = s.getCenterX();
	                orgTranslateY = s.getCenterY();
	            }
	        };

	        EventHandler<MouseEvent> sommetOnMouseDragged = new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent evt) {
	                double offsetX = evt.getSceneX() - orgSceneX;
	                double offsetY = evt.getSceneY() - orgSceneY;

	                double newTranslateX = orgTranslateX + offsetX;
	                double newTranslateY = orgTranslateY + offsetY;

	                Sommet s = ((Sommet) (evt.getSource()));
	                s.setCenterX(newTranslateX);
	                s.setCenterY(newTranslateY);
	                
	                if(evt.getSource() instanceof Sommet) {
	                	Graphe.isDragging = true;
	                }
	                else {
	                	Graphe.isDragging = false;
	                }
	            }
	        };

	    }
}
