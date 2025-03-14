package at.bbrz.kaiser.exceptions;

import java.util.ArrayList;
import java.util.List;

public class JWTValidationException extends RuntimeException {
    private List<String> messages = new ArrayList<>();

    public JWTValidationException() {
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public boolean containsMessage(String message) {
        return messages.contains(message);
    }

    public boolean shouldBeThrown() {
        return !messages.isEmpty();
    }

    public List<String> getMessages() {
        return messages;
    }
}
