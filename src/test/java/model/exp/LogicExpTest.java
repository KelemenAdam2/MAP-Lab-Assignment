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

class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void andOperatorEvaluatesTrueWhenBothAreTrue() throws MyException {
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(true));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void andOperatorEvaluatesFalseWhenOneIsFalse() throws MyException {
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void orOperatorEvaluatesTrueWhenAtLeastOneIsTrue() throws MyException {
        IExp e = new LogicExp(LogicOp.OR, bool(false), bool(true));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void orOperatorEvaluatesFalseWhenBothAreFalse() throws MyException {
        IExp e = new LogicExp(LogicOp.OR, bool(false), bool(false));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void firstOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.AND, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void secondOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), num(2));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }
}
