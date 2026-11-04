package model.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {
    @Test
    void twoEqualStringValuesAreEqualAndHaveSameHashCode() {
        StringValue s1 = new StringValue("test");
        StringValue s2 = new StringValue("test");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void twoDifferentStringValuesAreNotEqual() {
        StringValue s1 = new StringValue("a");
        StringValue s2 = new StringValue("b");
        assertNotEquals(s1, s2);
    }
}
