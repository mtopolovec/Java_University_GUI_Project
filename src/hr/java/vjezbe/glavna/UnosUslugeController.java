package hr.java.vjezbe.glavna;

import java.math.BigDecimal;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.entitet.Usluga;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
/**
 * Predstavlja entitet za unos Usluga.
 * 
 * @author Matija Topolovec
 *
 */
public class UnosUslugeController {
	@FXML
	private TextField naslovTextField;
	@FXML
	private TextField opisTextField;
	@FXML
	private TextField cijenaTextField;
	@FXML
	private ComboBox<Stanje> comboBoxStanje;
	
	boolean editiraj = false;
	Long id = null;
	
	public void initialize() {
		comboBoxStanje.getItems().addAll(Stanje.values());
	}
	
	public void unesiGumb() {

		String poruka = "";
		
		if(naslovTextField.getText().isEmpty()) {
			poruka += "Niste unjeli naslov!\n";
		} 
		if(opisTextField.getText().isEmpty()) {
			poruka += "Niste unjeli opis!\n";
		} 
		if(cijenaTextField.getText().isEmpty()) {
			poruka += "Niste unjeli cijenu!\n";
		} 
		if(comboBoxStanje.getValue() == null) {
			poruka += "Niste odabrali stanje!\n";
		}
		if(naslovTextField.getText().isEmpty() || opisTextField.getText().isEmpty() || cijenaTextField.getText().isEmpty() || comboBoxStanje.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Usluge.");
			alert.setContentText(poruka);
			alert.showAndWait();
		} else if(editiraj) {
			
			Usluga novaUsluga = new Usluga(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.izmjeniUslugu(novaUsluga, id);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Usluga je uspješno izmjenjena!");
			alert.showAndWait();
		} else {
			
			Usluga novaUsluga = new Usluga(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.pohraniNovuUslugu(novaUsluga);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Nova usluga je uspješno unesena!");
			alert.showAndWait();
		}
		
	}
	
	public void dohvatiUslugu(Usluga usluga) {
		naslovTextField.setText(usluga.getNaslov());
		opisTextField.setText(usluga.getOpis());
		cijenaTextField.setText(usluga.getCijena().toString());
		comboBoxStanje.setValue(usluga.getStanje());
		id = usluga.getId();
		editiraj = true;
	}
	
}
