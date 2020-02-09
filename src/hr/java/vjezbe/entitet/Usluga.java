package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
/**
 * Predstavlja entitet usluge koji je definiran naslovom, opisom i cijenom.
 * 
 * @author Matija Topolovec
 *
 */
public class Usluga extends Artikl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8418379135128057504L;
	/**
	 * Inicijalizira podatak o naslovu, opisu, cijeni i stanju.
	 * 
	 * @param id 			podatak o id-u.
	 * @param naslov		podatak o naslovu.
	 * @param opis 			podatak o opisu.
	 * @param cijena		podatak o cijeni.
	 * @param stanje 		podatak o stanju usluge.
	 *
	 */
	public Usluga(Long id, String naslov, String opis, BigDecimal cijena, Stanje stanje) {
		super(id, naslov, opis, cijena, stanje);
	}
	/**
	 * Dohva�a formatirani tekst sa tekstom oglasa usluge.
	 * 
	 * @return Vra�a String formatiranog teksta oglasa usluge.
	 * 
	 */
	@Override
	public String tekstOglasa() {
		return "Naslov usluge: " + super.getNaslov() + "\nOpis usluge: " + super.getOpis() + "\nStanje usluge: " + super.getStanje() + "\nCijena usluge: " + super.getCijena() + " kn";
	}
	@Override
	public String toString() {
		return super.getNaslov() + ", " + super.getOpis() + ", Cijena: " + super.getCijena() + "kn, Stanje: " + super.getStanje();
	}
	
}
