package app.domain;

public class RuleViolationError extends GymException {
    public RuleViolationError(String message) {
        super(message);
    }    
}
