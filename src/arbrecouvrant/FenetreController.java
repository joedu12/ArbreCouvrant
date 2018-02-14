package arbrecouvrant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controleur de la vue FXML
 */
public class FenetreController implements Initializable {
    @FXML
    private Graphe graphe;

    @FXML
    private CheckBox benchmark;

    @FXML
    private Label barreEtat;

    @FXML
    private void handleBenchmark(ActionEvent event) { graphe.handleBenchmark(benchmark.isSelected()); }

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
    public void initialize(URL url, ResourceBundle rb) {
        barreEtat.textProperty().bind(graphe.barreProperty());
    }
}
