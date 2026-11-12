package model.exp;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.MyDictionary;
import model.adt.MyHeap;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;


    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IExp stringVal(String s) {
        return new ValueExp(new StringValue(s));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void lessThanTrueWhenLeftIsSmaller() throws MyException {
        IExp exp = new RelationalExp(RelOp.LT, num(2), num(3));
        assertEquals(new BoolValue(true), exp.eval(symTable, heap));
    }

    @Test
    void lessThanFalseWhenLeftIsNotSmaller() throws MyException {
        IExp exp = new RelationalExp(RelOp.LT, num(3), num(3));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void lessOrEqualTrueWhenLeftIsSmallerOrEqual() throws MyException {
        IExp exp1 = new RelationalExp(RelOp.LE, num(2), num(3));
        assertEquals(new BoolValue(true), exp1.eval(symTable, heap));
        IExp exp2 = new RelationalExp(RelOp.LE, num(3), num(3));
        assertEquals(new BoolValue(true), exp2.eval(symTable, heap));
    }

    @Test
    void lessOrEqualFalseWhenLeftIsGreater() throws MyException {
        IExp exp = new RelationalExp(RelOp.LE, num(4), num(3));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void equalTrueWhenOperandsAreEqual() throws MyException {
        IExp exp = new RelationalExp(RelOp.EQ, num(5), num(5));
        assertEquals(new BoolValue(true), exp.eval(symTable, heap));
    }

    @Test
    void equalFalseWhenOperandsAreNotEqual() throws MyException {
        IExp exp = new RelationalExp(RelOp.EQ, num(5), num(6));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void notEqualTrueWhenOperandsAreDifferent() throws MyException {
        IExp exp = new RelationalExp(RelOp.NE, num(5), num(6));
        assertEquals(new BoolValue(true), exp.eval(symTable, heap));
    }

    @Test
    void notEqualFalseWhenOperandsAreEqual() throws MyException {
        IExp exp = new RelationalExp(RelOp.NE, num(5), num(5));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void greaterThanTrueWhenLeftIsGreater() throws MyException {
        IExp exp = new RelationalExp(RelOp.GT, num(6), num(5));
        assertEquals(new BoolValue(true), exp.eval(symTable, heap));
    }

    @Test
    void greaterThanFalseWhenLeftIsNotGreater() throws MyException {
        IExp exp = new RelationalExp(RelOp.GT, num(5), num(5));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void greaterOrEqualTrueWhenLeftIsGreaterOrEqual() throws MyException {
        IExp exp1 = new RelationalExp(RelOp.GE, num(6), num(5));
        assertEquals(new BoolValue(true), exp1.eval(symTable, heap));
        IExp exp2 = new RelationalExp(RelOp.GE, num(5), num(5));
        assertEquals(new BoolValue(true), exp2.eval(symTable, heap));
    }

    @Test
    void greaterOrEqualFalseWhenLeftIsSmaller() throws MyException {
        IExp exp = new RelationalExp(RelOp.GE, num(4), num(5));
        assertEquals(new BoolValue(false), exp.eval(symTable, heap));
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp exp = new RelationalExp(RelOp.LT, stringVal("abc"), num(5));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp exp = new RelationalExp(RelOp.LT, num(5), stringVal("abc"));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertEquals("second operand is not an integer", ex.getMessage());
    }
}
