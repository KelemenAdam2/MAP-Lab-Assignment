package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.type.BoolType;
import model.type.IntType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
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
    void declaresStringVariableWithDefaultValue() throws MyException {
        IStmt stmt = new VarDeclStmt("s", new StringType());
        stmt.execute(state);
        assertTrue(state.getSymTable().isDefined("s"));
        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
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
