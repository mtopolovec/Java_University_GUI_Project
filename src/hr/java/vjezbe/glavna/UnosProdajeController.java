package hr.java.vjezbe.glavna;

import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Artikl;
import hr.java.vjezbe.entitet.Korisnik;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class UnosProdajeController {
	
	@FXML
	private ComboBox<Artikl> comboBoxArtikl;
	
	@FXML
	private ComboBox<Korisnik> comboBoxKorisnik;
	
	@FXML
	private DatePicker datePickerUnosDatuma;
	
	boolean editiraj = false;
	
	public void initialize() {
		
		List<Artikl> artikli = new ArrayList<>();
		
		try {
			
			artikli = BazaPodataka.dohvatiSveArtikle();
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		
		List<Korisnik> korisnici = new ArrayList<>();
		
		try {
			
			korisnici = BazaPodataka.dohvatiSveKorisnike();
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		comboBoxArtikl.getItems().addAll(artikli);
		
		comboBoxKorisnik.getItems().addAll(korisnici);
		
	}
	
	public void unesiGumb() {
		
		String poruka = "";
			
		if(comboBoxArtikl.getSelectionModel().isEmpty()) {
			poruka += "Niste odabrali Artikl!\n";
		} 
		if(comboBoxKorisnik.getSelectionModel().isEmpty()) {
			poruka += "Niste odabrali Korisnika!\n";
		} 
		if(datePickerUnosDatuma.getValue() == null) {
			poruka += "Niste odabrali datum!\n";
		} 
		if(comboBoxArtikl.getSelectionModel().isEmpty() || comboBoxKorisnik.getSelectionModel().isEmpty() || datePickerUnosDatuma.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Oglasa.");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else {
			
			Prodaja oglas = new Prodaja(null,comboBoxArtikl.getSelectionModel().getSelectedItem(),comboBoxKorisnik.getSelectionModel().getSelectedItem(),datePickerUnosDatuma.getValue());
			
			try {
				
				BazaPodataka.pohraniNoviOglas(oglas);
				
			} catch (BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Novi oglas je uspješno unesen!");
			alert.showAndWait();
		}
		
	}
	
}
