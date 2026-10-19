package model.adt;

import exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {
    private MyStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
    }

    @Test
    void pushAndPopFollowsLifoOrder() throws ADTException {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void popOnEmptyStackThrows() {
        assertThrows(ADTException.class, () -> stack.pop());
    }

    @Test
    void isEmptyReturnsTrueWhenStackIsEmpty() {
        assertTrue(stack.isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenStackHasElements() {
        stack.push(42);
        assertFalse(stack.isEmpty());
    }
}
