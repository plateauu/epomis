package net.elenx.exceptions;

/**
 * Exception throws when noData exists when fetching data from provider
 */
public class ParseException extends RuntimeException {

    private static String MESSAGE = "[ParseException] No data transferred from %s in %s: [#s]";

    private ParseException(String message) {
        super(message, null);
    }

    public static ParseException noData(String provider, String className, String message) {
        return new ParseException(String.format(MESSAGE, provider, className, message));
    }

    public static ParseException noData(String provider, String className) {
        return noData(provider, className, null);
    }

}
