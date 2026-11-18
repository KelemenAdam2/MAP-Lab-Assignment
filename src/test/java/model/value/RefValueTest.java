package model.value;

import model.type.IntType;
import model.type.RefType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefValueTest {
    @Test
    void refValueGetTypeReturnsRefTypeWithLocationType() {
        RefValue refVal = new RefValue(1, new IntType());
        assertEquals(new RefType(new IntType()), refVal.getType());
    }

    @Test
    void refValueEqualityAndHashCode() {
        RefValue val1 = new RefValue(1, new IntType());
        RefValue val2 = new RefValue(1, new IntType());
        RefValue val3 = new RefValue(2, new IntType());
        RefValue val4 = new RefValue(1, new RefType(new IntType()));

        assertEquals(val1, val2);
        assertEquals(val1.hashCode(), val2.hashCode());
        assertNotEquals(val1, val3);
        assertNotEquals(val1, val4);
    }

    @Test
    void testToString() {
        RefValue refVal = new RefValue(1, new IntType());
        assertEquals("(1, int)", refVal.toString());
    }
}
