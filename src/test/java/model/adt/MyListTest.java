package model.adt;

import exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private MyList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void addAppendsElementsToList() throws ADTException {
        list.add("first");
        assertEquals("first", list.get(0));
    }

    @Test
    void sizeReturnsCorrectNumberOfElements() {
        assertEquals(0, list.size());
        list.add("a");
        assertEquals(1, list.size());
        list.add("b");
        assertEquals(2, list.size());
    }

    @Test
    void getReturnsElementAtGivenIndex() throws ADTException {
        list.add("elem0");
        list.add("elem1");
        assertEquals("elem0", list.get(0));
        assertEquals("elem1", list.get(1));
    }

    @Test
    void getWithNegativeIndexThrows() {
        assertThrows(ADTException.class, () -> list.get(-1));
    }

    @Test
    void getWithIndexOutOfBoundsThrows() {
        assertThrows(ADTException.class, () -> list.get(0));
    }
}
