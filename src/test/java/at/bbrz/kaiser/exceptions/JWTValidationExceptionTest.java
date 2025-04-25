package at.bbrz.kaiser.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JWTValidationExceptionTest {

    public static final String TEST_MESSAGE = "Test Message";
    JWTValidationException exception;
    @BeforeEach
    void setUp() {
        exception = new JWTValidationException();
    }

    @Test
    void aMessageShouldBeThrown() {
        exception.addMessage(TEST_MESSAGE);
        assertTrue(exception.shouldBeThrown());
    }


    @Test
    void noMessageShouldBeThrown() {
        assertFalse(exception.shouldBeThrown());
    }

    @Test
    void shouldContainMessage() {
        exception.addMessage(TEST_MESSAGE);
        assertTrue(exception.containsMessage(TEST_MESSAGE));
    }

    @Test
    void shouldGetListOfMessages() {
        exception.addMessage(TEST_MESSAGE);
        exception.addMessage(TEST_MESSAGE);
        assertTrue(exception.getMessages().containsAll(List.of(TEST_MESSAGE, TEST_MESSAGE)));
    }
}