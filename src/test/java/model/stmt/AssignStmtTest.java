package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.exp.ValueExp;
import model.value.BoolValue;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void successfulAssignmentUpdatesVariable() throws MyException {
        state.getSymTable().put("x", new IntValue(0));
        IStmt stmt = new AssignStmt("x", new ValueExp(new IntValue(42)));
        stmt.execute(state);
        assertEquals(new IntValue(42), state.getSymTable().lookup("x"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        IStmt stmt = new AssignStmt("x", new ValueExp(new IntValue(42)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("variable x was not declared", ex.getMessage());
    }

    @Test
    void typeMismatchThrows() {
        state.getSymTable().put("x", new IntValue(0));
        IStmt stmt = new AssignStmt("x", new ValueExp(new BoolValue(true)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("declared type of variable x and type of the assigned expression do not match", ex.getMessage());
    }
}
