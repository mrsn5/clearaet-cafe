package capprezy.ua.controller.exception.model;

public class NotValidDataException extends Exception {

    String reason;
    public static NotValidDataException createWith(String reason) {
        return new NotValidDataException(reason);
    }
    private NotValidDataException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
