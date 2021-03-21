package capprezy.ua.controller.exception.model;

public class AlreadyExistsException extends Exception {
    String reason;

    public static AlreadyExistsException createWith(String reason) {
        return new AlreadyExistsException(reason);
    }
    private AlreadyExistsException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}