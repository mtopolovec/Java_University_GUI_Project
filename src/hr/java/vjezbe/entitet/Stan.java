package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.CijenaJePreniskaException;
/**
 * Predstavlja entitet stana koji je definiran naslovom, opisom, cijenom i kvadraturom.
 * 
 * @author Matija Topolovec
 *
 */
public class Stan extends Artikl implements Nekretnina {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4251936291546694530L;

	private static final Logger logger = LoggerFactory.getLogger(Stan.class);

	private Integer kvadratura;
	/**
	 * Inicijalizira podatak o naslovu, opisu, cijeni i kvadraturi.
	 * 
	 * @param id 			podatak o id-u.
	 * @param naslov		podatak o naslovu.
	 * @param opis 			podatak o opisu.
	 * @param cijena		podatak o cijeni.
	 * @param kvadratura	podatak o kvadraturi.
	 * @param stanje 		podatak o stanju stana.
	 *
	 */
	public Stan(Long id, String naslov, String opis, BigDecimal cijena, Integer kvadratura, Stanje stanje) {
		super(id, naslov, opis, cijena, stanje);
		this.kvadratura = kvadratura;
	}
	/**
	 * Dohva�a parametar kvadratura iz Stan objekta.
	 * 
	 * @return Vra�a int vrijednost kvadrature iz Stan objekta.
	 * 
	 */
	public Integer getKvadratura() {
		return kvadratura;
	}
	/**
	 * Postavlja parametar kvadratura iz Stan objekta.
	 * 
	 * @param kvadratura podataci o kvadraturi.
	 * 
	 */
	public void setKvadratura(Integer kvadratura) {
		this.kvadratura = kvadratura;
	}
	/**
	 * Dohva�a formatirani tekst za ispis teksta oglasa.
	 * <p>
	 * Ukoliko se ne mo�e odrediti porez na nekretninu zbog premale cijene nekretnine.
	 * Onda se proslije�uje iznimka dalje te ispisuje korisniku poruka.
	 * 
	 * @return Vra�a String formatiranog teksta za ispis oglasa artikla. 
	 * 
	 *
	 */
	@Override
	public String tekstOglasa() {
		BigDecimal porezNekretnine = null;
		String tekst = null;
		try {
			porezNekretnine = izracunajPorez(super.getCijena()).setScale(2, RoundingMode.HALF_EVEN);
			tekst = "Naslov nekretnine: " + super.getNaslov() + "\nOpis nekretnine: " + super.getOpis() + "\nKvadratura nekretnine: " + kvadratura + " m2" + "\nStanje nekretnine: " + super.getStanje() + "\nPorez na nekretnine: " + porezNekretnine + " kn" + "\nCijena nekretnine: " + super.getCijena() + " kn";
		} catch(CijenaJePreniskaException ex1) {
			logger.error("Pogre�ka prilikom odre�ivanja iznosa poreza!", ex1);
			tekst = "Naslov nekretnine: " + super.getNaslov() + "\nOpis nekretnine: " + super.getOpis() + "\nKvadratura nekretnine: " + kvadratura + " m2" + "\nStanje nekretnine: " + super.getStanje() + "\nPorez na nekretnine: " +  "Cijena ne smije biti manja od 10000 kn" + "\nCijena nekretnine: " + super.getCijena() + " kn";
		}
		return tekst;
	}
	@Override
	public String toString() {
		return super.getNaslov() + ", " + super.getOpis() + ", " + kvadratura + "m2, Cijena: " + super.getCijena() + "kn, " + "Stanje: " + super.getStanje() ;
	}

	
	
}
