package es.uclm.iso2.irrigation.domain;

/**
 * Thrown when the data of the greenhouse are not valid (Q1 of the README).
 */
public class InvalidReadingException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with the reason why the data are not valid.
     *
     * @param message the reason, for example which value is out of range
     */
    public InvalidReadingException(String message) {
        super(message);
    }
}
