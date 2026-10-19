package model.stmt;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void executeLeavesStateUnchanged() throws MyException {
        IStmt nop = new NopStmt();
        PrgState result = nop.execute(state);

        assertSame(state, result);
        assertTrue(result.getExeStack().isEmpty());
    }
}
