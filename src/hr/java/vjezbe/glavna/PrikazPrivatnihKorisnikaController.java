package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.PrivatniKorisnik;
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
 * Predstavlja entitet za prikaz Privatnih korisnika.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazPrivatnihKorisnikaController {
	
	@FXML
	private TextField imeTextField;
	
	@FXML
	private TextField prezimeTextField;
	
	@FXML
	private TextField mailTextField;
	
	@FXML
	private TextField telefonTextField;
	
	@FXML
	private TableView<PrivatniKorisnik> tablicaPrivatnihKorisnika;
	
	@FXML
	private TableColumn<PrivatniKorisnik, String> kolonaTabliceZaIme;
	
	@FXML
	private TableColumn<PrivatniKorisnik, String> kolonaTabliceZaPrezime;
	
	@FXML
	private TableColumn<PrivatniKorisnik, String> kolonaTabliceZaEmail;
	
	@FXML
	private TableColumn<PrivatniKorisnik, String> kolonaTabliceZaTelefon;
	
	List<PrivatniKorisnik> listaKorisnika = new ArrayList<>();
	
	public void initialize() {
		
		PrivatniKorisnik korisnik = new PrivatniKorisnik(null, imeTextField.getText(), prezimeTextField.getText(), mailTextField.getText(), telefonTextField.getText());
		
		try {
			
			listaKorisnika = BazaPodataka.dohvatiPrivatneKorisnikePremaKriterijima(korisnik);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaPrivatnihKorisnika.setItems(FXCollections.observableList(listaKorisnika));
		
		kolonaTabliceZaIme.setCellValueFactory(new PropertyValueFactory<PrivatniKorisnik, String>("ime"));
		
		kolonaTabliceZaPrezime.setCellValueFactory(new PropertyValueFactory<PrivatniKorisnik, String>("prezime"));
		
		kolonaTabliceZaEmail.setCellValueFactory(new PropertyValueFactory<PrivatniKorisnik, String>("email"));
		
		kolonaTabliceZaTelefon.setCellValueFactory(new PropertyValueFactory<PrivatniKorisnik, String>("telefon"));
	}
		
	public void pretraziGumb() {

		PrivatniKorisnik korisnik = new PrivatniKorisnik(
				null,
				imeTextField.getText().toLowerCase(),
				prezimeTextField.getText().toLowerCase(),
				mailTextField.getText().toLowerCase(),
				telefonTextField.getText().toLowerCase());

		List<PrivatniKorisnik> listaKorisnika = new ArrayList<>();

		try {
		
			listaKorisnika = BazaPodataka.dohvatiPrivatneKorisnikePremaKriterijima(korisnik);
		
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaPrivatnihKorisnika.setItems(FXCollections.observableList(listaKorisnika));
		
	}
	
	public void izbrisiGumb() {
		
		Integer index = tablicaPrivatnihKorisnika.getSelectionModel().getSelectedIndex();
		
		PrivatniKorisnik korisnik = listaKorisnika.get(index);
		
		try {
			BazaPodataka.izbrisiKorisnika(korisnik);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
	public void izmjeniGumb() {
		
		PrivatniKorisnik korisnik = tablicaPrivatnihKorisnika.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("unosPrivatnihKorisnika.fxml"));
			Parent root = loader.load();
			
			UnosPrivatnihKorisnikaController upk = loader.getController();
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
