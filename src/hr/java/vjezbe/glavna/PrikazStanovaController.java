package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Stan;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**
 * Predstavlja entitet za prikaz Stanova.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazStanovaController {
	
	@FXML
	private TextField naslovTextField;
	
	@FXML
	private TextField opisTextField;
	
	@FXML
	private TextField kvadraturaTextField;
	
	@FXML
	private TextField cijenaTextField;
	
	@FXML
	private TableView<Stan> tablicaStanova;
	
	@FXML
	private TableColumn<Stan, String> kolonaTabliceZaNaslov;
	
	@FXML
	private TableColumn<Stan, String> kolonaTabliceZaOpis;
	
	@FXML
	private TableColumn<Stan, Integer> kolonaTabliceZaKvadraturu;
	
	@FXML
	private TableColumn<Stan, BigDecimal> kolonaTabliceZaCijenu;
	
	@FXML
	private TableColumn<Stan, Stanje> kolonaTabliceZaStanje;
	
	List<Stan> listaStanova = new ArrayList<>();
	
	public void initialize() {

		Stan stan = new Stan(null, naslovTextField.getText().toLowerCase(), opisTextField.getText().toLowerCase(), null, null, null);
		
		try {
			
			listaStanova = BazaPodataka.dohvatiStanovePremaKriterijima(stan);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaStanova.setItems(FXCollections.observableList(listaStanova));
		
		kolonaTabliceZaNaslov.setCellValueFactory(new PropertyValueFactory<Stan, String>("naslov"));
		
		kolonaTabliceZaOpis.setCellValueFactory(new PropertyValueFactory<Stan, String>("opis"));
		
		kolonaTabliceZaKvadraturu.setCellValueFactory(new PropertyValueFactory<Stan, Integer>("kvadratura"));
		
		kolonaTabliceZaCijenu.setCellValueFactory(new PropertyValueFactory<Stan, BigDecimal>("cijena"));
		
		kolonaTabliceZaStanje.setCellValueFactory(new PropertyValueFactory<Stan, Stanje>("stanje"));
	}
	
	public void pretraziGumb() {
		
		Stan stan = new Stan(
				null,
				naslovTextField.getText().toLowerCase(),
				opisTextField.getText().toLowerCase(),
				cijenaTextField.getText().isBlank() || cijenaTextField.getText().isEmpty() ? null : new BigDecimal(cijenaTextField.getText()),
				kvadraturaTextField.getText().isBlank() || kvadraturaTextField.getText().isEmpty() ? null : Integer.parseInt(kvadraturaTextField.getText()),
				null);

		List<Stan> listaStanova = new ArrayList<>();
		
		try {
		
			listaStanova = BazaPodataka.dohvatiStanovePremaKriterijima(stan);
		
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaStanova.setItems(FXCollections.observableList(listaStanova));
		
	}
	
	public void prikaziPocetniEkran() {
		
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getMainStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void izbrisiGumb() {
		
		Integer index = tablicaStanova.getSelectionModel().getSelectedIndex();
		
		Stan stan = listaStanova.get(index);
		
		try {
			BazaPodataka.izbrisiArtikl(stan);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
	public void izmjeniGumb() {
		
		Stan stan = tablicaStanova.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("unosStanova.fxml"));
			Parent root = loader.load();
			
			UnosStanovaController usc = loader.getController();
			usc.dohvatiStan(stan);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Izmjena");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
