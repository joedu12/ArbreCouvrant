package arbrecouvrant;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Classe qui contiendra l'ensemble des points et arêtes
 */
public class Graphe extends Pane {
	   private final ArrayList<Sommet> listSommet = new ArrayList<>();
	   private final ArrayList<Arete>  listArete  = new ArrayList<>();
	   
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
					   if(!couple.get(0).getNom().equals(couple.get(1).getNom())) {
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
	   private void rafraichir() {
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
	    * Trie les arêtes via un évènement onAction sur un bouton dans le FXML
	    */
	   public void trierArete() {
		   Collections.sort(listArete);

           System.out.println("Arêtes triées:");
           for(Arete arete: listArete) {
               System.out.println(arete.getPoids());
           }

	   }

        /*
         * Exécute l'algorithme de Kruskal
         */
        public void execKruskal() {

            // 1er étape : Marquer les arêtes
            new Thread(() -> {
                for(Arete arete: listArete) {
                    try {
                        // Attends 0.5s entre chaque marquage
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        // Marque les arêtes depuis le Thread JavaFX
                        arete.marquer();
                        rafraichir();
                    });
                }
            }).start();

            // 2ème étape : Vérifier les cycles

            // 3ème étape : S'arrêter quand tous les sommets ont été parcourus

        }

    /*
     * Exécute l'algorithme de Prim
     */
    public void execPrim() {
        // 1 : Marquer le premier sommet
        Sommet premier = listArete.get(0).getPrecedent();
        premier.setMarque(true);

        // Tant que tous les sommets ne sont pas marqués
        while(!listSommet.stream().allMatch(Sommet::isMarque)) {

            // 2.1 : Enregistrer dans une liste l'ensemble des arêtes adjacentes au premier sommet
            ArrayList<Arete> listAreteAdjacent = new ArrayList<>();
            for (Arete arete : listArete) {
                if (arete.getPrecedent().equals(premier)) {
                    listAreteAdjacent.add(arete);
                }
                else if (arete.getSuivant().equals(premier)) {
                    listAreteAdjacent.add(arete);
                }
            }

            // 2.2 : Trier les arêtes adjacentes par poids
            Collections.sort(listAreteAdjacent);
            Arete areteMarque = listAreteAdjacent.get(0);
            listAreteAdjacent.clear();

            // 2.3 : Marquer l'arête de poids minimal
            areteMarque.marquer();

            // 3 : Marquer son deuxième sommet
            Sommet sommetMarque = null;
            if(areteMarque.getPrecedent().isMarque()) {
                sommetMarque = areteMarque.getSuivant();
            }
            if(areteMarque.getSuivant().isMarque()) {
                sommetMarque = areteMarque.getPrecedent();
            }
            sommetMarque.setMarque(true);

            // 4: Choisir un nouveau sommet non marqué
            for(Sommet sommet: listSommet) {
                if(!sommet.isMarque()) {
                    premier = sommet;
                    break;
                }
            }
        }
    }

	   /*
	    * Dessine un cercle dans lequel on a son numéro
	    */
	   private void tracerSommet(double x, double y) {
            Sommet sommet = new Sommet(x, y, ""+listSommet.size());
            listSommet.add(sommet);
            getChildren().add(sommet);
	   }
	   
	   /*
	    * Dessine une arête entre deux sommets
	    */
	   private void tracerArete(Sommet a, Sommet b) {
            Arete arete = new Arete(a, b);
            listArete.add(arete);
            getChildren().add(arete);
            System.out.println(a.getNom() + " lié avec " + b.getNom() + ", poids : " + arete.getPoids());
	   }
}
