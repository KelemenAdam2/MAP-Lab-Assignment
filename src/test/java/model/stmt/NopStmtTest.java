package model.stmt;

import exceptions.MyException;
import model.PrgState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void executeLeavesStateUnchanged() throws MyException {
        IStmt nop = new NopStmt();
        var result = nop.execute(state);

        assertTrue(result.isEmpty());
        assertTrue(state.getExeStack().isEmpty());
    }
}
