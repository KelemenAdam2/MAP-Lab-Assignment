package model.exp;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.MyDictionary;
import model.value.IValue;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void evaluatesDefinedVariable() throws MyException {
        symTable.put("x", new IntValue(42));
        IExp e = new VarExp("x");
        assertEquals(new IntValue(42), e.eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        IExp e = new VarExp("x");
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("variable x is not defined", ex.getMessage());
    }
}
