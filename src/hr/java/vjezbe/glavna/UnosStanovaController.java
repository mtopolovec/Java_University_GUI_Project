package hr.java.vjezbe.glavna;

import java.math.BigDecimal;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Stan;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
/**
 * Predstavlja entitet za unos Stanova.
 * 
 * @author Matija Topolovec
 *
 */
public class UnosStanovaController {
	@FXML
	private TextField naslovTextField;
	@FXML
	private TextField opisTextField;
	@FXML
	private TextField cijenaTextField;
	@FXML
	private TextField kvadraturaTextField;
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
		if(kvadraturaTextField.getText().isEmpty()) {
			poruka += "Niste unjeli kvadraturu!\n";
		} 
		if(cijenaTextField.getText().isEmpty()) {
			poruka += "Niste unjeli cijenu!\n";
		} 
		if(comboBoxStanje.getValue() == null) {
			poruka += "Niste odabrali stanje!\n";
		}
		if(naslovTextField.getText().isEmpty() || opisTextField.getText().isEmpty() || kvadraturaTextField.getText().isEmpty() || cijenaTextField.getText().isEmpty() || comboBoxStanje.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška!");
			alert.setHeaderText("Greška! Neispravan unos Stana.");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else if(editiraj) {
			
			Stan noviStan = new Stan(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()),Integer.parseInt(kvadraturaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.izmjeniStan(noviStan, id);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Stan je uspješno izmjenjen!");
			alert.showAndWait();
			
		} else {
			
			Stan noviStan = new Stan(null,naslovTextField.getText(),opisTextField.getText(), new BigDecimal(cijenaTextField.getText()),Integer.parseInt(kvadraturaTextField.getText()), comboBoxStanje.getValue());
			try {
				BazaPodataka.pohraniNoviStan(noviStan);
			} catch(BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Novi stan je uspješno unesen!");
			alert.showAndWait();
		}
		
	}
	
	public void dohvatiStan(Stan stan) {
		naslovTextField.setText(stan.getNaslov());
		opisTextField.setText(stan.getOpis());
		cijenaTextField.setText(stan.getCijena().toString());
		kvadraturaTextField.setText(stan.getKvadratura().toString());
		comboBoxStanje.setValue(stan.getStanje());
		id = stan.getId();
		editiraj = true;
	}
	
}
