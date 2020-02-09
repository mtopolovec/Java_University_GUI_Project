package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

import hr.java.vjezbe.iznimke.CijenaJePreniskaException;

/**
 * Predstavlja su�elje nekretnine koji je definiran metodom izracunajPorez.
 * 
 * @author Matija Topolovec
 *
 */
public interface Nekretnina {
	
	////////////////////////////////
	/*
	* Konstante
	*/
	////////////////////////////////

	static final BigDecimal FaktorZaPorez = new BigDecimal("0.03");
	/**
	 * Izra�una porez nekretnine ovisno o cijeni same nekretnine ako je cijena manja od 10000 kn baca se iznimka.
	 * 
	 * @param cijenaNekretnine BigDecimal ulazni podatak cijene nekretnine.
	 * @return Vra�a BigDecimal izra�unatu cijenu poreza.
	 *
	 */
	default public BigDecimal izracunajPorez(BigDecimal cijenaNekretnine) throws CijenaJePreniskaException{
		BigDecimal cijena = null;
		if(cijenaNekretnine.compareTo(BigDecimal.valueOf(10000)) < 0) {
			throw new CijenaJePreniskaException();
		} else {
			cijena = cijenaNekretnine.multiply(FaktorZaPorez);
		}
		return cijena;
	}
	
}
