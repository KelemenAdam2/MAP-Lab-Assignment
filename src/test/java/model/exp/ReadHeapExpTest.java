package model.exp;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.stmt.NopStmt;
import model.type.IntType;
import model.type.RefType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadHeapExpTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void readsTheValueFromHeap() throws MyException {
        int addr = state.getHeap().allocate(new IntValue(42));
        IExp exp = new ReadHeapExp(new ValueExp(new RefValue(addr, new IntType())));
        assertEquals(new IntValue(42), exp.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void nonReferenceExpressionThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new IntValue(10)));
        assertThrows(StatementException.class, () -> exp.eval(state.getSymTable(), state.getHeap()));

        IExp expBool = new ReadHeapExp(new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> expBool.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void addressNotInTheHeapThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new RefValue(99, new IntType())));
        assertThrows(StatementException.class, () -> exp.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void addressZeroNotInHeapThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new RefValue(0, new IntType())));
        assertThrows(StatementException.class, () -> exp.eval(state.getSymTable(), state.getHeap()));
    }
}
