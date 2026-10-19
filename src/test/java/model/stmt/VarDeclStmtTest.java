package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void declaresIntVariableWithDefaultValue() throws MyException {
        IStmt stmt = new VarDeclStmt("x", new IntType());
        stmt.execute(state);
        assertTrue(state.getSymTable().isDefined("x"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void declaresBoolVariableWithDefaultValue() throws MyException {
        IStmt stmt = new VarDeclStmt("b", new BoolType());
        stmt.execute(state);
        assertTrue(state.getSymTable().isDefined("b"));
        assertEquals(new BoolValue(false), state.getSymTable().lookup("b"));
    }

    @Test
    void redeclaredVariableThrows() throws MyException {
        IStmt stmt1 = new VarDeclStmt("x", new IntType());
        stmt1.execute(state);
        IStmt stmt2 = new VarDeclStmt("x", new IntType());
        var ex = assertThrows(StatementException.class, () -> stmt2.execute(state));
        assertEquals("x already declared", ex.getMessage());
    }
}
