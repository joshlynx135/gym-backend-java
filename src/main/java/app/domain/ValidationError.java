package app.domain;

//Input missing or malformed 400
public class ValidationError extends GymException {
    public ValidationError(String message) {
        super(message);
    }
}
