package exceptions;

/**
 * Created by shund on 07.06.2017.
 */
public class CalculatorException extends Exception {
    private String message;

    public CalculatorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
