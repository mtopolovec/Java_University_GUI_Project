package hr.java.vjezbe.glavna;

import java.math.BigDecimal;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Automobil;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
/**
 * Predstavlja entitet za unos Automobila.
 * 
 * @author Matija Topolovec
 *
 */
public class UnosAutomobilaController {
	
	@FXML
	private TextField naslovTextField;
	@FXML
	private TextField opisTextField;
	@FXML
	private TextField snagaTextField;
	@FXML
	private TextField cijenaTextField;
	@FXML
	private ComboBox<Stanje> comboBoxStanje;
	
	public void initialize() {
		comboBoxStanje.getItems().addAll(Stanje.values());
	}
	
	boolean editiraj = false;
	Long id = null;
	
	public void unesiGumb() {
		
		String poruka = "";
			
		if(naslovTextField.getText().isEmpty()) {
			poruka += "Niste unjeli naslov!\n";
		} 
		if(opisTextField.getText().isEmpty()) {
			poruka += "Niste unjeli opis!\n";
		} 
		if(snagaTextField.getText().isEmpty()) {
			poruka += "Niste unjeli snagu!\n";
		} 
		if(cijenaTextField.getText().isEmpty()) {
			poruka += "Niste unjeli cijenu!\n";
		} 
		if(comboBoxStanje.getValue() == null) {
			poruka += "Niste odabrali stanje!\n";
		}
		if(naslovTextField.getText().isEmpty() || opisTextField.getText().isEmpty() || snagaTextField.getText().isEmpty() || cijenaTextField.getText().isEmpty() || comboBoxStanje.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Automobila.");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else if(editiraj) {
			Automobil noviAuto = new Automobil(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()),new BigDecimal(snagaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.izmjeniAutomobil(noviAuto, id);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Automobil je uspješno izmjenjen!");
			alert.showAndWait();
		}
		else {
			Automobil noviAuto = new Automobil(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()),new BigDecimal(snagaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.pohraniNoviAutomobil(noviAuto);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Novi automobil je uspješno unesen!");
			alert.showAndWait();
		}
		
	}
	
	public void dohvatiAutomobil(Automobil auto) {
		naslovTextField.setText(auto.getNaslov());
		opisTextField.setText(auto.getOpis());
		snagaTextField.setText(auto.getSnagaKS().toString());
		cijenaTextField.setText(auto.getCijena().toString());
		comboBoxStanje.setValue(auto.getStanje());
		id = auto.getId();
		editiraj = true;
	}
	
}
