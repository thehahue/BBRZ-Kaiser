package at.bbrz.kaiser.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UUIDWrapperTest {

    UUIDWrapper uuidWrapper;

    @BeforeEach
    void setup(){
        uuidWrapper = new UUIDWrapper();
    }

    @Test
    void uuidShouldBe36Characters(){
        String uuid = uuidWrapper.createUUID();
        System.out.println(uuid);

        assertEquals(36, uuid.length());
    }

    @Test
    void checkUuidSegmentLengths(){
        String uuid = uuidWrapper.createUUID();
        String[] uuidSplit = uuid.split("-");

        assertEquals(5, uuidSplit.length);
        assertEquals(8, uuidSplit[0].length());
        assertEquals(4, uuidSplit[1].length());
        assertEquals(4, uuidSplit[2].length());
        assertEquals(4, uuidSplit[3].length());
        assertEquals(12, uuidSplit[4].length());
    }

    @Test
    void checkUuidOnlyContainHexadecimalDigitsAndMinus(){
        String regex = "[0-9a-f-]{36}";
        String uuid = uuidWrapper.createUUID();

        assertTrue(uuid.matches(regex));
    }
}