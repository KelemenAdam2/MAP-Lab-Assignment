package model.type;

import model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeTest {
    @Test
    void refTypeVariableGetsDefaultValueWithAddressZeroAndInnerType() {
        RefType refInt = new RefType(new IntType());
        assertEquals(new RefValue(0, new IntType()), refInt.defaultValue());

        RefType refRefInt = new RefType(refInt);
        assertEquals(new RefValue(0, refInt), refRefInt.defaultValue());
    }

    @Test
    void refRefIntEqualsRefRefIntAndDiffersFromRefInt() {
        RefType refInt = new RefType(new IntType());
        RefType refRefInt1 = new RefType(new RefType(new IntType()));
        RefType refRefInt2 = new RefType(new RefType(new IntType()));

        assertEquals(refRefInt1, refRefInt2);
        assertEquals(refRefInt1.hashCode(), refRefInt2.hashCode());
        assertNotEquals(refRefInt1, refInt);
    }

    @Test
    void testToString() {
        RefType refInt = new RefType(new IntType());
        assertEquals("Ref(int)", refInt.toString());
        RefType refRefInt = new RefType(refInt);
        assertEquals("Ref(Ref(int))", refRefInt.toString());
    }
}
