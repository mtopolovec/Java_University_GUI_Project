package hr.java.vjezbe.glavna;

import hr.java.vjezbe.niti.DatumObjaveNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Predstavlja entitet izgleda glavnog GUI tj glavne stranice.
 * 
 * @author Matija Topolovec
 *
 */
public class Main extends Application {
	
	private static Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) {
		
		mainStage = primaryStage;
		
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png")));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Timeline prikazZadnjegOglasa = new Timeline(new
				KeyFrame(Duration.seconds(10),
						new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Platform.runLater(new DatumObjaveNit());
				}
			}));
		prikazZadnjegOglasa.setCycleCount(Timeline.INDEFINITE);
		prikazZadnjegOglasa.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getMainStage() {
		return mainStage;
	}
	
	public static void setMainPage(BorderPane main, BorderPane ekran) {
		Scene scene = new Scene(main, 600, 600);
		
		mainStage.setScene(scene);
		main.setCenter(ekran);
		mainStage.show();
	}
}
