package model.exp;

import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.MyDictionary;
import model.adt.MyHeap;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;


    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void evaluatesIntValue() throws MyException {
        IExp e = new ValueExp(new IntValue(10));
        assertEquals(new IntValue(10), e.eval(symTable, heap));
    }

    @Test
    void evaluatesBoolValue() throws MyException {
        IExp e = new ValueExp(new BoolValue(true));
        assertEquals(new BoolValue(true), e.eval(symTable, heap));
    }
}
