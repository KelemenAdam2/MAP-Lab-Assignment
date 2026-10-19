package model.exp;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.MyDictionary;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void additionComputesSum() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(3), num(5));
        assertEquals(new IntValue(8), e.eval(symTable));
    }

    @Test
    void subtractionComputesDifference() throws MyException {
        IExp e = new ArithExp(ArithOp.SUB, num(10), num(4));
        assertEquals(new IntValue(6), e.eval(symTable));
    }

    @Test
    void multiplicationComputesProduct() throws MyException {
        IExp e = new ArithExp(ArithOp.MUL, num(3), num(5));
        assertEquals(new IntValue(15), e.eval(symTable));
    }

    @Test
    void divisionComputesQuotient() throws MyException {
        IExp e = new ArithExp(ArithOp.DIV, num(14), num(4));
        assertEquals(new IntValue(3), e.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, bool(true), num(5));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(5), bool(false));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }
}