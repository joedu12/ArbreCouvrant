package arbrecouvrant;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class Graphe extends Pane {
	   private GraphicsContext calque;
	   private List<Sommet> dessins = new ArrayList<>();

	   public Graphe() {  
		      Canvas dessin = new Canvas(1920, 1024);
		      calque = dessin.getGraphicsContext2D();  
		      Sommet.setCalque(calque);
		      setOnMouseClicked(evt -> { tracerForme(evt.getX(), evt.getY()); } );  
		      getChildren().add(dessin);
	   }

	   public void toutEffacer() { 
	      calque.clearRect(0, 0, getWidth(), getHeight()); 
	      dessins.clear();
	   }
 
	   public void tracerForme(double x, double y) {
	      Sommet sommet = new Sommet(x, y);
	      dessins.add(sommet);
	      sommet.setNom("ok");
	      sommet.affiche();
	   }
}
