package hr.java.vjezbe.glavna;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.PrivatniKorisnik;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
/**
 * Predstavlja entitet za unos Privatnih korisnika.
 * 
 * @author Matija Topolovec
 *
 */
public class UnosPrivatnihKorisnikaController {

	@FXML
	private TextField imeTextField;
	@FXML
	private TextField prezimeTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField telefonTextField;
	
	boolean editiraj = false;
	Long id = null;
	
	public void unesiGumb() {
		
		String poruka = "";
		
		if(imeTextField.getText().isEmpty()) {
			poruka += "Niste unjeli ime!\n";
		} 
		if(prezimeTextField.getText().isEmpty()) {
			poruka += "Niste unjeli prezime!\n";
		} 
		if(emailTextField.getText().isEmpty()) {
			poruka += "Niste unjeli email!\n";
		} 
		if(telefonTextField.getText().isEmpty()) {
			poruka += "Niste unjeli telefon!\n";
		} 
		if(imeTextField.getText().isEmpty() || prezimeTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || telefonTextField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Privatnog korisnika.");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else if(editiraj) {
			
			PrivatniKorisnik noviPrivatniKorisnik = new PrivatniKorisnik(null,imeTextField.getText(),prezimeTextField.getText(), emailTextField.getText(),telefonTextField.getText());
			try {
				BazaPodataka.izmjeniPrivatnogKorisnika(noviPrivatniKorisnik, id);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Privatni korisnik je uspješno izmjenjen!");
			alert.showAndWait();
			
		} else {
			
			PrivatniKorisnik noviPrivatniKorisnik = new PrivatniKorisnik(null,imeTextField.getText(),prezimeTextField.getText(), emailTextField.getText(),telefonTextField.getText());
			try {
				BazaPodataka.pohraniNovogPrivatnogKorisnika(noviPrivatniKorisnik);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Novi privatni korisnik je uspješno unesen!");
			alert.showAndWait();
		}
		
	}
	
	public void dohvatiKorisnika(PrivatniKorisnik korisnik) {
		imeTextField.setText(korisnik.getIme());
		prezimeTextField.setText(korisnik.getPrezime());
		emailTextField.setText(korisnik.getEmail());
		telefonTextField.setText(korisnik.getTelefon());
		id = korisnik.getId();
		editiraj = true;
	}
	
}
