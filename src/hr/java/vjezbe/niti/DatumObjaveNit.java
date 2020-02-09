package hr.java.vjezbe.niti;

import java.time.format.DateTimeFormatter;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatumObjaveNit implements Runnable {
	
	static final String DATUM = "dd.MM.yyyy.";
	
	public void run() {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Zadnji oglas!");
			alert.setHeaderText(null);
			Prodaja prodaja = null;
			try {
				prodaja = BazaPodataka.dohvatiZadnjuProdaju();
			} catch (BazaPodatakaException ex) {
				ex.printStackTrace();
			}
			alert.setContentText("Oglas: " + prodaja.getArtikl() + "\nProdavatelj: " + prodaja.getKorisnik() + "\nDatum objave: " + prodaja.getDatumObjave().format(DateTimeFormatter.ofPattern(DATUM)));
			alert.showAndWait();
		
	}
	
}
