package hr.java.vjezbe.iznimke;
/**
 * Predstavlja neoznačenu iznimku cijena je preniska koji je definiran naslovom, opisom i cijenom.
 * 
 * @author Matija Topolovec
 *
 */
public class CijenaJePreniskaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2555341954833609776L;
	/**
	 * Ručno napisana poruka iznimke koja se proslijeđuje konstruktoru nadklase.
	 * 
	 *
	 */
	public CijenaJePreniskaException() {
		super("Cijena ne smije biti manja od 10000 kn");
	}
	/**
	 * Poruka iznimke koja se proslijeđuje konstruktoru nadklase.
	 * 
	 * @param message String poruka.
	 *
	 */
	public CijenaJePreniskaException(String message) {
		super(message);
	}
	/**
	 * Poruka i uzrok iznimke koji se proslijeđuju konstruktoru nadklase.
	 * 
	 * @param message String poruka.
	 * @param cause uzrok iznimke.
	 *
	 */
	public CijenaJePreniskaException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Uzrok iznimke koji se proslijeđuje konstruktoru nadklase.
	 * @param cause uzrok iznimke.
	 *
	 */
	public CijenaJePreniskaException(Throwable cause) {
		super(cause);
	}
}
