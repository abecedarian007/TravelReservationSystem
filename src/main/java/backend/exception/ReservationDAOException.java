package backend.exception;

public class ReservationDAOException extends Exception {
    public ReservationDAOException (String message, Throwable cause) {
        super(message, cause);
    }
}
