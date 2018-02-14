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
import java.util.HashSet;

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
     * Un graphe contiens l'ensemble des sommets et arêtes
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
            System.out.println(arete.toString());
        }

    }

    /*
     * Exécute l'algorithme de Kruskal
     */
    public void execKruskal() {

        new Thread(() -> {

            // on démarque les arêtes et sommets
            listSommet.forEach(s -> s.setMarque(false));
            listSommet.forEach(s -> s.getListeCycle().clear());
            listArete.forEach(a -> a.setMarque(false));
            listArete.forEach(a -> a.setErreur(false));

            // 1 : Marquer le premier sommet
            listArete.get(0).getPrecedent().setMarque(true);

            // 2 : Trier les arêtes
            Collections.sort(listArete);

            // 3 : Tant que tous les sommets ne sont pas marqués
            for(Arete arete : listArete) {
                try {
                    // Attends 0.5s entre chaque marquage
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {

                    // 6 : On regarde si on peut la marquer ou si elle forme un cercle
                    // Aucun des deux sommets n'a encore été utilisé
                    if (arete.getPrecedent().getListeCycle().size() == 0
                            && arete.getSuivant().getListeCycle().size() == 0) {
                        // On marque l'arête
                        arete.setMarque(true);
                        // On marque les sommets et on donne un nom de cycle
                        arete.getSuivant().setMarque(true);
                        arete.getPrecedent().setMarque(true);
                        arete.getSuivant().getListeCycle().add(arete.getPrecedent().getNom());
                        arete.getPrecedent().getListeCycle().add(arete.getPrecedent().getNom());
                    }
                    // Les deux sommets utilisés, l'arête ne peut pas être utilisée
                    else if (containsOne(arete.getPrecedent().getListeCycle(), arete.getSuivant().getListeCycle())){
                        arete.setErreur(true);
                    }
                    // Un des deux sommets utilisé, on ajoute l'autre sommet dans le cycle
                    else {
                        // On marque l'arête
                        arete.setMarque(true);
                        // On marque les sommets
                        arete.getSuivant().setMarque(true);
                        arete.getPrecedent().setMarque(true);
                        // On met à jour nos listes
                        // Si une des deux liste est vide on lui ajoute l'autre
                        if (arete.getPrecedent().getListeCycle().size() == 0) {
                            arete.getPrecedent().getListeCycle().addAll(arete.getSuivant().getListeCycle());
                        } else if (arete.getSuivant().getListeCycle().size() == 0) {
                            arete.getSuivant().getListeCycle().addAll(arete.getPrecedent().getListeCycle());
                        } else {
                            HashSet<String> aChercher = arete.getPrecedent().getListeCycle();
                            aChercher.addAll(arete.getSuivant().getListeCycle());
                            for (Sommet sommet : listSommet) {
                                for (String nom : aChercher) {
                                    if (sommet.getListeCycle().contains(nom)) {
                                        sommet.getListeCycle().addAll(aChercher);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 8 : Rafraîchir l'interface
                    rafraichir();
                });
            }
        }).start();
    }

    /*
     * Exécute l'algorithme de Prim
     */
    public void execPrim() {

        new Thread(() -> {

            ArrayList<Arete> listAreteAdjacent = new ArrayList<>();

            // on démarque les arêtes et sommets
            listSommet.forEach(s -> s.setMarque(false));
            listArete.forEach(a -> a.setMarque(false));

            // 1 : Marquer le premier sommet
            listArete.get(0).getPrecedent().setMarque(true);

            // 2 : Tant que tous les sommets ne sont pas marqués
            while(!listSommet.stream().allMatch(Sommet::isMarque)) {
                try {
                    // Attends 0.5s entre chaque marquage
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    // 3 : Enregistrer dans une liste l'ensemble des arêtes adjacentes
                    listAreteAdjacent.clear();
                    for (Arete arete : listArete) {
                        // on cherche une arête non marquée
                        if(!arete.isMarque()) {
                            // ayant uniqument un seul sommet déjà marqué
                            if((arete.getSuivant().isMarque() || arete.getPrecedent().isMarque())
                                    && !(arete.getSuivant().isMarque() && arete.getPrecedent().isMarque())){
                                listAreteAdjacent.add(arete);
                            }
                        }
                    }

                    if (listAreteAdjacent.size() > 0) {
                        // 4 : Trier les arêtes adjacentes par poids
                        Collections.sort(listAreteAdjacent);

                        // 5 : On sélectionne l'arête de poids optimal
                        Arete areteMarque = listAreteAdjacent.get(0);

                        // 6 : Marquer l'arête de poids minimal
                        areteMarque.setMarque(true);

                        // 7 : Marquer ses sommets
                        areteMarque.getSuivant().setMarque(true);
                        areteMarque.getPrecedent().setMarque(true);

                        // 8 : Rafraîchir l'interface
                        rafraichir();
                    }
                });
            }
        }).start();
    }

    /*
     * Permet de comparer deux listes, retourne vrai si au moins une chaine apparait dans les deux listes
     */
    private boolean containsOne(HashSet<String> list1, HashSet<String> list2) {

        if (list1.size() > 0 && list2.size() > 0) {
            for (String s1 : list1) {
                for (String s2 : list2) {
                    if (s1.equals(s2))
                        return true;
                }
            }
        }
        return false;
    }

    /*
     * Dessine un cercle dans lequel on a son numéro
     */
    private void tracerSommet(double x, double y) {
        Sommet sommet = new Sommet(x, y, ""+listSommet.size());
        listSommet.add(sommet);
        getChildren().add(sommet);
        System.out.println("Dessin -> "+sommet.toString());
    }

    /*
     * Dessine une arête entre deux sommets
     */
    private void tracerArete(Sommet a, Sommet b) {
        Arete arete = new Arete(a, b);
        listArete.add(arete);
        getChildren().add(arete);
        System.out.println("Dessin -> "+arete.toString());
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
