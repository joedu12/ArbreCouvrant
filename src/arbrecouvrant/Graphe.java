package arbrecouvrant;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Classe qui contiendra l'ensemble des points et arêtes
 */
public class Graphe extends Pane {
    private final ArrayList<Sommet> listSommet = new ArrayList<>();
    private final ArrayList<Arete>  listArete  = new ArrayList<>();

    // gestion des fichiers
    private FileChooser selecteur = new FileChooser();
    private Alert alerte = new Alert(Alert.AlertType.WARNING);

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
        // on démarque les arêtes et sommets
        listSommet.forEach(s -> s.setMarque(false));
        listArete.forEach(a -> a.setMarque(false));

        // 1: Trier les arêtes
        Collections.sort(listArete);

        new Thread(() -> {
            for(Arete arete: listArete) {
                try {
                    // Attends 0.5s entre chaque marquage
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    // 2 : Marquer les arêtes (depuis le Thread JavaFX)
                    arete.setMarque(true);
                    rafraichir();

                    // 3 : Vérifier les cycles

                    // 4 : S'arrêter quand tous les sommets ont été parcourus
                });
            }
        }).start();
    }

    /*
    * Exécute l'algorithme de Prim
    */
    public void execPrim() {
        // on démarque les arêtes et sommets
        listSommet.forEach(s -> s.setMarque(false));
        listArete.forEach(a -> a.setMarque(false));

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

            // S'il n'y a pas d'arête adjacente
            if(listAreteAdjacent.size() == 0) {
                for (Sommet sommet : new ArrayList<>(listSommet)) {
                    if (sommet.equals(premier)) {
                        // On supprime le sommet
                        listSommet.remove(sommet);
                    }
                }
                rafraichir();
            }
            else {
                // 2.3 : On sélectionne l'arête de poids optimal
                Arete areteMarque = listAreteAdjacent.get(0);
                listAreteAdjacent.clear();

                // 2.4 : Marquer l'arête de poids minimal
                areteMarque.setMarque(true);

                // 3 : Marquer son deuxième sommet
                Sommet sommetMarque = null;
                if(areteMarque.getPrecedent().isMarque()) {
                    sommetMarque = areteMarque.getSuivant();
                }
                if(areteMarque.getSuivant().isMarque()) {
                    sommetMarque = areteMarque.getPrecedent();
                }

                if(sommetMarque == null) {
                    System.out.println("Erreur de marquage :");
                    System.out.println(areteMarque.toString());
                    break;
                }
                else { sommetMarque.setMarque(true); rafraichir(); }
            }

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

	/*
	 * Efface le l'ensemble du graphe via un évènement onAction sur un bouton dans le FXML
	 */
	public void toutEffacer() {
		getChildren().clear();
		listSommet.clear();
		listArete.clear();
	}


    /*
     * Ouvre le graphe depuis un fichier
     * Les objets n'étant pas correctement sérialisées, il n'y a plus la relation
     * entre listSommet et les précédent/suivants des arêtes après l'ouverture :
     * -> les algorithmes de fonctionnent pas sur un graphe chargé depuis le disque.
     */
    public void ouvrir() {
        selecteur.setTitle("Ouvrir un fichier de graphe");
        File fichier = selecteur.showOpenDialog(null);
        if (fichier != null) {
            try {
                ObjectInputStream fluxObjets = new ObjectInputStream(new FileInputStream(fichier));
                toutEffacer();
                int nombre = fluxObjets.readInt();
                for (int i=0; i<nombre; i++) {
                    Sommet sommetFic = (Sommet) fluxObjets.readObject();
                    tracerSommet(sommetFic.getX(), sommetFic.getY());
                }
                nombre = fluxObjets.readInt();
                for (int i=0; i<nombre; i++) {
                    Arete areteFic = (Arete) fluxObjets.readObject();
                    Sommet a = new Sommet(areteFic.getPrecedent().getX(), areteFic.getPrecedent().getY(), areteFic.getPrecedent().getNom());
                    Sommet b = new Sommet(areteFic.getSuivant().getX(), areteFic.getSuivant().getY(), areteFic.getSuivant().getNom());

                    // chercher dans listSommet la référence du nouveau sommet à ajouter
                    for(Sommet sommet : listSommet) {
                        if(sommet.equals(a)) {
                            a = sommet;
                        }
                        if(sommet.equals(b)) {
                            b = sommet;
                        }
                    }
                    tracerArete(a, b);
                }
                fluxObjets.close();
            }
            catch (Exception ex) {
                alerte.setHeaderText("Impossible de lire le fichier");
                alerte.setContentText("Le fichier : "+fichier.getName()+" n'est pas un fichier de graphes.");
                alerte.showAndWait();
            }
        }
    }

    /*
     * Enregistre le graphe depuis dans un fichier
     */
    public void enregistrer() {
        selecteur.setTitle("Enregister le graphe");
        File fichier = selecteur.showSaveDialog(null);
        if (fichier != null) {
            try {
                ObjectOutputStream fluxObjets = new ObjectOutputStream(new FileOutputStream(fichier));

                fluxObjets.writeInt(listSommet.size());
                for (Sommet sommet : listSommet) {
                    fluxObjets.writeObject(sommet);
                }

                fluxObjets.writeInt(listArete.size());
                for (Arete arete : listArete) {
                    fluxObjets.writeObject(arete);
                }
                fluxObjets.close();
            }
            catch (IOException ex) {
                alerte.setHeaderText("Impossible de générer le fichier");
                alerte.setContentText("Le fichier : "+fichier.getName()+" n'a pas été créé.");
                alerte.showAndWait();
            }
        }
    }
}
