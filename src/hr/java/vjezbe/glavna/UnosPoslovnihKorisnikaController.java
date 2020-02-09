package hr.java.vjezbe.glavna;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.PoslovniKorisnik;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
/**
 * Predstavlja entitet za unos Poslovnih korisnika.
 * 
 * @author Matija Topolovec
 *
 */
public class UnosPoslovnihKorisnikaController {
	@FXML
	private TextField nazivTextField;
	@FXML
	private TextField webTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField telefonTextField;

	boolean editiraj = false;
	Long id = null;
	
	public void unesiGumb() {
		
		String poruka = "";
		
		if(nazivTextField.getText().isEmpty()) {
			poruka += "Niste unjeli naziv!\n";
		} 
		if(webTextField.getText().isEmpty()) {
			poruka += "Niste unjeli web!\n";
		} 
		if(emailTextField.getText().isEmpty()) {
			poruka += "Niste unjeli email!\n";
		} 
		if(telefonTextField.getText().isEmpty()) {
			poruka += "Niste unjeli telefon!\n";
		} 
		if(nazivTextField.getText().isEmpty() || webTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || telefonTextField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Poslovnog korisnika.");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else if(editiraj) {
			
			PoslovniKorisnik noviPoslovniKorisnik = new PoslovniKorisnik(null,nazivTextField.getText(),webTextField.getText(), emailTextField.getText(),telefonTextField.getText());
			try {
				BazaPodataka.izmjeniPoslovnogKorisnika(noviPoslovniKorisnik, id);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Poslovni korisnik je uspješno izmjenjen!");
			alert.showAndWait();
			
		} else {
			
			PoslovniKorisnik noviPoslovniKorisnik = new PoslovniKorisnik(null,nazivTextField.getText(),webTextField.getText(), emailTextField.getText(),telefonTextField.getText());
			try {
				BazaPodataka.pohraniNovogPoslovnogKorisnika(noviPoslovniKorisnik);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Novi poslovni korisnik je uspješno unesen!");
			alert.showAndWait();
		}
		
	}
	
	public void dohvatiKorisnika(PoslovniKorisnik korisnik) {
		nazivTextField.setText(korisnik.getNaziv());
		webTextField.setText(korisnik.getWeb());
		emailTextField.setText(korisnik.getEmail());
		telefonTextField.setText(korisnik.getTelefon());
		id = korisnik.getId();
		editiraj = true;
	}
	
}
