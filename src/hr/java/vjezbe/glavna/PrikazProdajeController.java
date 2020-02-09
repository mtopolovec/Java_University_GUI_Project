package hr.java.vjezbe.glavna;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Artikl;
import hr.java.vjezbe.entitet.Korisnik;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Predstavlja entitet za prikaz Prodaje.
 * 
 * @author Matija Topolovec
 *
 */
public class PrikazProdajeController {
	
	@FXML
	private ComboBox<Artikl> comboBoxArtikl;
	
	@FXML
	private ComboBox<Korisnik> comboBoxKorisnik;
	
	@FXML
	private DatePicker datePickerUnosDatuma;
	
	@FXML
	private TableView<Prodaja> tablicaOglasa;
	
	@FXML
	private TableColumn<Prodaja, String> kolonaTabliceZaOglas;
	
	@FXML
	private TableColumn<Prodaja, String> kolonaTabliceZaKorisnika;
	
	@FXML
	private TableColumn<Prodaja, LocalDate> kolonaTabliceZaDatum;

	List<Prodaja> listaOglasa = new ArrayList<>();

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
		
		Prodaja oglas = new Prodaja(null,null,comboBoxKorisnik.getValue(),datePickerUnosDatuma.getValue());
		
		try {
			
			listaOglasa = BazaPodataka.dohvatiProdajuPremaKriterijima(oglas);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		tablicaOglasa.setItems(FXCollections.observableList(listaOglasa));
		
		kolonaTabliceZaOglas.setCellValueFactory(new PropertyValueFactory<>("artikl"));
		
		kolonaTabliceZaKorisnika.setCellValueFactory(new PropertyValueFactory<>("korisnik"));
		
		kolonaTabliceZaDatum.setCellValueFactory(new PropertyValueFactory<>("datumObjave"));
	
	}
	
	public void pretraziGumb() {
		
		Prodaja oglas = new Prodaja(
							null,
							comboBoxArtikl.getValue(),
							comboBoxKorisnik.getValue(),
							datePickerUnosDatuma.getValue()
							);
		
		List<Prodaja> listaOglasa = new ArrayList<>();
		
		try {
			
			listaOglasa = BazaPodataka.dohvatiProdajuPremaKriterijima(oglas);
			
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		tablicaOglasa.setItems(FXCollections.observableList(listaOglasa));
		
	}
	
	public void izbrisiGumb() {
		
		Integer index = tablicaOglasa.getSelectionModel().getSelectedIndex();
		
		Prodaja oglas = listaOglasa.get(index);
		
		try {
			BazaPodataka.izbrisiOglas(oglas);
		} catch (BazaPodatakaException ex) {
			ex.printStackTrace();
		}
		
		initialize();
	}
	
}
