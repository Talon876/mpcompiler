package compiler.io;

public class NoCharException extends RuntimeException {
	private static final long serialVersionUID = 3974744929059917129L;

	/**
	 * Constructs a NoCharException will null as its error messsage string.
	 */
	public NoCharException() {
	}

	/**
	 * Constructs a NoCharException, with the error message defined in s
	 * 
	 * @param s
	 *            - the detail message
	 */
	public NoCharException(String s) {
		super(s);
	}
}
