package hr.java.vjezbe.iznimke;

public class BazaPodatakaException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5957358737736255428L;

	public BazaPodatakaException(String message) {
		super(message);
	}

	public BazaPodatakaException(String message, Throwable cause) {
		super(message, cause);
	}

	public BazaPodatakaException(Throwable cause) {
		super(cause);
	}
}
