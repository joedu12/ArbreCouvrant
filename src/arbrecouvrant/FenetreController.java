package arbrecouvrant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controleur de la vue FXML
 */
public class FenetreController implements Initializable {
    @FXML
    private Graphe graphe;

    @FXML
    private void trierArete(ActionEvent event) {
       graphe.trierArete();
    }

    @FXML
    private void execKruskal(ActionEvent event) { graphe.execKruskal(); }

    @FXML
    private void execPrim(ActionEvent event) { graphe.execPrim(); }

    @FXML
    private void toutEffacer(ActionEvent event)  {
      graphe.toutEffacer();
    }

    @FXML
    private void ouvrir(ActionEvent event)  { graphe.ouvrir(); }

    @FXML
    private void enregistrer(ActionEvent event)  { graphe.enregistrer(); }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  }
}
