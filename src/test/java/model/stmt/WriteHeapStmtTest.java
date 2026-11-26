package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.ValueExp;
import model.type.IntType;
import model.type.RefType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriteHeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void changesTheHeapLocation() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);

        assertEquals(new IntValue(20), state.getHeap().get(1));

        assertTrue(new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state).isEmpty());
        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void variableNotDeclaredThrows() {
        assertThrows(StatementException.class, () -> new WriteHeapStmt("undeclared", new ValueExp(new IntValue(10))).execute(state));
    }

    @Test
    void variableNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class, () -> new WriteHeapStmt("x", new ValueExp(new IntValue(10))).execute(state));
    }

    @Test
    void addressNotInTheHeapThrows() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        // v has address 0 (null) which is not in the heap
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", new ValueExp(new IntValue(10))).execute(state));

        // or with dummy RefValue address not allocated
        state.getSymTable().update("v", new RefValue(99, new IntType()));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", new ValueExp(new IntValue(10))).execute(state));
    }

    @Test
    void valueOfTheWrongTypeThrows() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);

        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", new ValueExp(new BoolValue(true))).execute(state));
    }
}
