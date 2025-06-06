package at.bbrz.kaiser.exceptions;


public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(String message) {
        super(message);
    }
}
