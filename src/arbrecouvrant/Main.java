package arbrecouvrant;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Fenetre.fxml"));
			
			BorderPane root = (BorderPane) loader.load();
			Scene fenetre = new Scene(root,600,600);
			fenetre.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle("Arbre couvrant de poids optimal");
			primaryStage.setScene(fenetre);
			primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
