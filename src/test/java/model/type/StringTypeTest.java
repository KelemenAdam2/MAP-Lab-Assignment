package model.type;

import model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringTypeTest {
    @Test
    void stringVariableGetsDefaultValueEmptyString() {
        StringType type = new StringType();
        assertEquals(new StringValue(""), type.defaultValue());
    }

    @Test
    void testToString() {
        assertEquals("string", new StringType().toString());
    }
}
