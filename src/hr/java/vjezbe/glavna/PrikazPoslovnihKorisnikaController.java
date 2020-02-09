package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.PoslovniKorisnik;
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
 * Predstavlja entitet za prikaz Poslovnih korisnika.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazPoslovnihKorisnikaController {
	
	@FXML
	private TextField nazivTextField;
	
	@FXML
	private TextField webTextField;
	
	@FXML
	private TextField mailTextField;
	
	@FXML
	private TextField telefonTextField;
	
	@FXML
	private TableView<PoslovniKorisnik> tablicaPoslovnihKorisnika;
	
	@FXML
	private TableColumn<PoslovniKorisnik, String> kolonaTabliceZaNaziv;
	
	@FXML
	private TableColumn<PoslovniKorisnik, String> kolonaTabliceZaWeb;
	
	@FXML
	private TableColumn<PoslovniKorisnik, String> kolonaTabliceZaEmail;
	
	@FXML
	private TableColumn<PoslovniKorisnik, String> kolonaTabliceZaTelefon;
	
	List<PoslovniKorisnik> listaKorisnika = new ArrayList<>();
	
	public void initialize() {
		
		PoslovniKorisnik korisnik = new PoslovniKorisnik(null, nazivTextField.getText(), webTextField.getText(), mailTextField.getText(), telefonTextField.getText());
		
		try {
			
			listaKorisnika = BazaPodataka.dohvatiPoslovneKorisnikePremaKriterijima(korisnik);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaPoslovnihKorisnika.setItems(FXCollections.observableList(listaKorisnika));

		kolonaTabliceZaNaziv.setCellValueFactory(new PropertyValueFactory<PoslovniKorisnik, String>("naziv"));
		
		kolonaTabliceZaWeb.setCellValueFactory(new PropertyValueFactory<PoslovniKorisnik, String>("web"));
		
		kolonaTabliceZaEmail.setCellValueFactory(new PropertyValueFactory<PoslovniKorisnik, String>("email"));
		
		kolonaTabliceZaTelefon.setCellValueFactory(new PropertyValueFactory<PoslovniKorisnik, String>("telefon"));
	}
	
	public void pretraziGumb() {

		PoslovniKorisnik korisnik = new PoslovniKorisnik(
				null,
				nazivTextField.getText().toLowerCase(),
				webTextField.getText().toLowerCase(),
				mailTextField.getText().toLowerCase(),
				telefonTextField.getText().toLowerCase());

		List<PoslovniKorisnik> listaKorisnika = new ArrayList<>();

		try {
		
			listaKorisnika = BazaPodataka.dohvatiPoslovneKorisnikePremaKriterijima(korisnik);
		
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaPoslovnihKorisnika.setItems(FXCollections.observableList(listaKorisnika));
	}
	
	public void izbrisiGumb() {
		
		Integer index = tablicaPoslovnihKorisnika.getSelectionModel().getSelectedIndex();
		
		PoslovniKorisnik korisnik = listaKorisnika.get(index);
		
		try {
			BazaPodataka.izbrisiKorisnika(korisnik);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
	public void izmjeniGumb() {

		PoslovniKorisnik korisnik = tablicaPoslovnihKorisnika.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("unosPoslovnihKorisnika.fxml"));
			Parent root = loader.load();
			
			UnosPoslovnihKorisnikaController upk = loader.getController();
			upk.dohvatiKorisnika(korisnik);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Izmjena");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
