package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.entitet.Usluga;
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
import javafx.stage.Stage;
/**
 * Predstavlja entitet za prikaz Usluga.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazUslugaController {
	
	@FXML
	private TextField naslovTextField;
	
	@FXML
	private TextField opisTextField;
	
	@FXML
	private TextField cijenaTextField;
	
	@FXML
	private TableView<Usluga> tablicaUsluga;
	
	@FXML
	private TableColumn<Usluga, String> kolonaTabliceZaNaslov;
	
	@FXML
	private TableColumn<Usluga, String> kolonaTabliceZaOpis;
	
	@FXML
	private TableColumn<Usluga, BigDecimal> kolonaTabliceZaCijenu;
	
	@FXML
	private TableColumn<Usluga, Stanje> kolonaTabliceZaStanje;
	
	List<Usluga> listaUsluga = new ArrayList<>();
	
	public void initialize() {
		
		Usluga usluga = new Usluga(null, naslovTextField.getText(), opisTextField.getText(), null, null);
		
		try {
			
			listaUsluga = BazaPodataka.dohvatiUslugePremaKriterijima(usluga);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaUsluga.setItems(FXCollections.observableList(listaUsluga));
		
		kolonaTabliceZaNaslov.setCellValueFactory(new PropertyValueFactory<Usluga, String>("naslov"));
		
		kolonaTabliceZaOpis.setCellValueFactory(new PropertyValueFactory<Usluga, String>("opis"));
		
		kolonaTabliceZaCijenu.setCellValueFactory(new PropertyValueFactory<Usluga, BigDecimal>("cijena"));
		
		kolonaTabliceZaStanje.setCellValueFactory(new PropertyValueFactory<Usluga, Stanje>("stanje"));
		
	}
	
	public void pretraziGumb() {
		
		Usluga usluga = new Usluga(
				null,
				naslovTextField.getText().toLowerCase(),
				opisTextField.getText().toLowerCase(),
				cijenaTextField.getText().isBlank() || cijenaTextField.getText().isEmpty() ? null : new BigDecimal(cijenaTextField.getText()),
				null);

		List<Usluga> listaUsluga = new ArrayList<>();
		
		try {
		
			listaUsluga = BazaPodataka.dohvatiUslugePremaKriterijima(usluga);
		
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaUsluga.setItems(FXCollections.observableList(listaUsluga));
	}
	
	public void izbrisiGumb() {
		
		Integer index = tablicaUsluga.getSelectionModel().getSelectedIndex();
		
		Usluga usluga = listaUsluga.get(index);
		
		try {
			BazaPodataka.izbrisiArtikl(usluga);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
	public void izmjeniGumb() {
		
		Usluga usluga = tablicaUsluga.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("unosUsluge.fxml"));
			Parent root = loader.load();
			
			UnosUslugeController uuc = loader.getController();
			uuc.dohvatiUslugu(usluga);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Izmjena");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
