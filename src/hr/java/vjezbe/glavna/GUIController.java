package hr.java.vjezbe.glavna;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
/**
 * Predstavlja entitet za GUI kontroler.
 * 
 * @author Matija Topolovec
 *
 */
public class GUIController {
	// fxml fajl od glavnog menija
	private static String GLAVNIPROZOR = "GUI.fxml";
	
	public void prikaziEkranZaPrikazAutomobila() {

		prikazEkrana("prikazAutomobila.fxml");

	}
	
	public void prikaziEkranZaPrikazStanova() {
		
		prikazEkrana("prikazStanova.fxml");

	}
	
	public void prikaziEkranZaPrikazUsluga() {
		
		prikazEkrana("prikazUsluga.fxml");

	}
	
	public void prikaziEkranZaPrikazPrivatnihKorisnika() {
		
		prikazEkrana("prikazPrivatnihKorisnika.fxml");

	}
	
	public void prikaziEkranZaPrikazPoslovnihKorisnika() {
		
		prikazEkrana("prikazPoslovnihKorisnika.fxml");

	}
	
	public void prikaziEkranZaUnosAutomobila() {
		
		prikazEkrana("unosAutomobila.fxml");

	}
	
	public void prikaziEkranZaUnosStanova() {
		
		prikazEkrana("unosStanova.fxml");

	}
	
	public void prikaziEkranZaUnosUsluga() {
		
		prikazEkrana("unosUsluge.fxml");

	}
	
	public void prikaziEkranZaUnosPrivatnihKorisnika() {
		
		prikazEkrana("unosPrivatnihKorisnika.fxml");

	}
	
	public void prikaziEkranZaUnosPoslovnihKorisnika() {
		
		prikazEkrana("unosPoslovnihKorisnika.fxml");

	}
	
	public void prikaziEkranZaPretraguProdaje() {
		
		prikazEkrana("prikazProdaje.fxml");
		
	}
	
	public void prikaziEkranZaUnosProdaje() {
		
		prikazEkrana("unosProdaje.fxml");
	
	}
	/**
	 * Predstavlja metodu koja mjenja sadržaj ekrana.
	 * 
	 * @param file 	naziv fxml fajla èiji se sadržaj želi prikazati.
	 *
	 */
	private void prikazEkrana(String file) {
		BorderPane main;
		BorderPane ekran;
		try {
			main = (BorderPane) FXMLLoader.load(getClass().getResource(GLAVNIPROZOR));
			ekran = (BorderPane) FXMLLoader.load(getClass().getResource(file));
			Main.setMainPage(main, ekran);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
