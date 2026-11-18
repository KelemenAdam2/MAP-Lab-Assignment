package model.adt;

import exceptions.ADTException;
import model.value.IntValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyHeapTest {
    private MyHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsSequentialAddressesStartingAtOne() {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void getReturnsAllocatedValue() throws ADTException {
        int addr = heap.allocate(new StringValue("hello"));
        assertEquals(new StringValue("hello"), heap.get(addr));
    }

    @Test
    void getOfMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(99));
        assertThrows(ADTException.class, () -> heap.get(0));
    }

    @Test
    void updateModifiesExistingAddress() throws ADTException {
        int addr = heap.allocate(new IntValue(10));
        heap.update(addr, new IntValue(20));
        assertEquals(new IntValue(20), heap.get(addr));
    }

    @Test
    void updateOfMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(99, new IntValue(10)));
        assertThrows(ADTException.class, () -> heap.update(0, new IntValue(10)));
    }

    @Test
    void isDefinedReturnsTrueForAllocatedAddressAndFalseOtherwise() {
        assertFalse(heap.isDefined(1));
        int addr = heap.allocate(new IntValue(5));
        assertTrue(heap.isDefined(addr));
        assertFalse(heap.isDefined(addr + 1));
    }

    @Test
    void setContentAndGetContent() throws ADTException {
        Map<Integer, model.value.IValue> content = Map.of(1, new IntValue(100), 2, new IntValue(200));
        heap.setContent(content);
        assertEquals(content, heap.getContent());
        assertEquals(new IntValue(100), heap.get(1));
        assertEquals(new IntValue(200), heap.get(2));
    }
}
