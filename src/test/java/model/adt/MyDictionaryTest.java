package model.adt;

import exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private MyDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putAndLookupReturnsValue() throws ADTException {
        dict.put("a", 10);
        assertEquals(10, dict.lookup("a"));
    }

    @Test
    void lookupMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.lookup("nonexistent"));
    }

    @Test
    void updateModifiesExistingKey() throws ADTException {
        dict.put("a", 10);
        dict.update("a", 20);
        assertEquals(20, dict.lookup("a"));
    }

    @Test
    void updateMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("nonexistent", 20));
    }

    @Test
    void isDefinedReturnsTrueForExistingKey() {
        dict.put("a", 10);
        assertTrue(dict.isDefined("a"));
    }

    @Test
    void isDefinedReturnsFalseForMissingKey() {
        assertFalse(dict.isDefined("b"));
    }
}
