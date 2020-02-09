package hr.java.vjezbe.entitet;

/**
 * Predstavlja entitet poslovnog korisnika koji je definiran nazivom, web adresom, mail adresom i telefonskim brojem.
 * 
 * @author Matija Topolovec
 *
 */
public class PoslovniKorisnik extends Korisnik {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213650871775617772L;
	
	private String naziv;
	private String web;
	/**
	 * Inicijalizira podatak o nazivu, web adresi, mail adresi i telefonskom broju.
	 * 
	 * @param id 		podatak o id-u.
	 * @param naziv		podatak o nazivu.
	 * @param web 		podatak o web adresi.
	 * @param email		podatak o mail adresi.
	 * @param telefon	podatak o telefonskom broju.
	 *
	 */
	public PoslovniKorisnik(Long id, String naziv, String web, String email, String telefon) {
		super(id, email, telefon);
		this.naziv = naziv;
		this.web = web;
	}
	/**
	 * Dohva�a parametar String naziv iz PoslovniKorisnik objekta.
	 * 
	 * @return Vra�a String vrijednost naziva iz PoslovniKorisnik objekta.
	 * 
	 */
	public String getNaziv() {
		return naziv;
	}
	/**
	 * Postavlja parametar String naziv iz PoslovniKorisnik objekta.
	 * 
	 * @param naziv String podatak o nazivu.
	 * 
	 */
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	/**
	 * Dohva�a parametar String web iz PoslovniKorisnik objekta.
	 * 
	 * @return Vra�a String vrijednost web adrese iz PoslovniKorisnik objekta.
	 * 
	 */
	public String getWeb() {
		return web;
	}
	/**
	 * Postavlja parametar String web iz PoslovniKorisnik objekta.
	 * 
	 * @param web String podatak o web adresi.
	 * 
	 */
	public void setWeb(String web) {
		this.web = web;
	}
	/**
	 * Dohva�a formatirani tekst sa kontakt podacima poslovnog korisnika.
	 * 
	 * @return Vra�a String formatiranog teksta kontakt podataka poslovnog korisnika.
	 * 
	 */
	@Override
	public String dohvatiKontakt() {
		return "Naziv tvrtke: " + naziv + ", mail: " + super.getEmail() + ", tel: " + super.getTelefon() + ", web: " + web;
	}
	@Override
	public String toString() {
		return naziv + ", " + web + ", email: " + super.getEmail() + ", tel: " + super.getTelefon();
	}
	
}
