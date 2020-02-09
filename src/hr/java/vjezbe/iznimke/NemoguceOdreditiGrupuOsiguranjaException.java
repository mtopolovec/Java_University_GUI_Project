package hr.java.vjezbe.iznimke;
/**
 * Predstavlja ozna�enu iznimku nemogu�e odrediti grupu osiguranja koji je definiran naslovom, opisom i cijenom.
 * 
 * @author Matija Topolovec
 *
 */
public class NemoguceOdreditiGrupuOsiguranjaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2291535264076831445L;
	/**
	 * Ru�no napisana poruka iznimke koja se proslije�uje konstruktoru nadklase.
	 * 
	 *
	 */
	public NemoguceOdreditiGrupuOsiguranjaException() {
		super("Previ�e kw, ne mogu odrediti grupu osiguranja.");
	}
	/**
	 * Poruka iznimke koja se proslije�uje konstruktoru nadklase.
	 * 
	 * @param message String poruka.
	 *
	 */
	public NemoguceOdreditiGrupuOsiguranjaException(String message) {
		super(message);
	}
	/**
	 * Poruka i uzrok iznimke koji se proslije�uju konstruktoru nadklase.
	 * 
	 * @param message String poruka.
	 * @param cause uzrok iznimke.
	 *
	 */
	public NemoguceOdreditiGrupuOsiguranjaException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Uzrok iznimke koji se proslije�uje konstruktoru nadklase.
	 * @param cause uzrok iznimke.
	 *
	 */
	public NemoguceOdreditiGrupuOsiguranjaException(Throwable cause) {
		super(cause);
	}
}
