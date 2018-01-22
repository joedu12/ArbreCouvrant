package arbrecouvrant;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FenetreController implements Initializable {
   @FXML
   private Graphe graphe;
   
   @FXML
   private void toutEffacer(ActionEvent event)  {
	   graphe.toutEffacer();
   }
   
   @Override
   public void initialize(URL url, ResourceBundle rb) {  }
}
