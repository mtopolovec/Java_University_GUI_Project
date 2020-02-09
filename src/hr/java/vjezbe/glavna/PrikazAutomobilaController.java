package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Automobil;
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
import javafx.stage.Stage;
/**
 * Predstavlja entitet za prikaz Automobila.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazAutomobilaController {
	
	@FXML
	private TextField naslovTextField;
	
	@FXML
	private TextField opisTextField;
	
	@FXML
	private TextField snagaTextField;
	
	@FXML
	private TextField cijenaTextField;
	
	@FXML
	private TableView<Automobil> tablicaAutomobila;
	
	@FXML
	private TableColumn<Automobil, String> kolonaTabliceZaNaslov;
	
	@FXML
	private TableColumn<Automobil, String> kolonaTabliceZaOpis;
	
	@FXML
	private TableColumn<Automobil, BigDecimal> kolonaTabliceZaSnagu;
	
	@FXML
	private TableColumn<Automobil, BigDecimal> kolonaTabliceZaCijenu;
	
	@FXML
	private TableColumn<Automobil, Stanje> kolonaTabliceZaStanje;
	
	List<Automobil> listaAutomobila = new ArrayList<>();
	
	public void initialize() {
		
		Automobil auto = new Automobil(null,naslovTextField.getText().toLowerCase(),opisTextField.getText().toLowerCase(),null,null,null);
		
		try {
			
			listaAutomobila = BazaPodataka.dohvatiAutomobilePremaKriterijima(auto);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaAutomobila.setItems(FXCollections.observableList(listaAutomobila));
		
		kolonaTabliceZaNaslov.setCellValueFactory(new PropertyValueFactory<>("naslov"));
		
		kolonaTabliceZaOpis.setCellValueFactory(new PropertyValueFactory<>("opis"));
		
		kolonaTabliceZaSnagu.setCellValueFactory(new PropertyValueFactory<>("snagaKS"));
		
		kolonaTabliceZaCijenu.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		
		kolonaTabliceZaStanje.setCellValueFactory(new PropertyValueFactory<>("stanje"));
	
	}
	
	public void pretraziGumb() {
		Automobil auto = new Automobil(
							null,
							naslovTextField.getText().toLowerCase(),
							opisTextField.getText().toLowerCase(),
							cijenaTextField.getText().isBlank() || cijenaTextField.getText().isEmpty() ? null : new BigDecimal(cijenaTextField.getText()),
							snagaTextField.getText().isBlank() || snagaTextField.getText().isEmpty() ? null : new BigDecimal(snagaTextField.getText()),
							null);
		
		List<Automobil> listaAutomobila = new ArrayList<>();
		
		try {
			
			listaAutomobila = BazaPodataka.dohvatiAutomobilePremaKriterijima(auto);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaAutomobila.setItems(FXCollections.observableList(listaAutomobila));
		
	}

	public void izbrisiGumb() {
		
		Integer index = tablicaAutomobila.getSelectionModel().getSelectedIndex();
		
		Automobil auto = listaAutomobila.get(index);
		
		try {
			BazaPodataka.izbrisiArtikl(auto);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
	public void izmjeniGumb() {
		
		Automobil auto = tablicaAutomobila.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("unosAutomobila.fxml"));
			Parent root = loader.load();
			
			UnosAutomobilaController uac = loader.getController();
			uac.dohvatiAutomobil(auto);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Izmjena");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
