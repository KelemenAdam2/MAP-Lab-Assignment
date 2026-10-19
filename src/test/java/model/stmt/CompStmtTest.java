package model.stmt;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.exp.ValueExp;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void executePushesSecondThenFirstToExecutionStack() throws MyException {
        IStmt s1 = new NopStmt();
        IStmt s2 = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt comp = new CompStmt(s1, s2);
        comp.execute(state);

        assertEquals(s1, state.getExeStack().pop());
        assertEquals(s2, state.getExeStack().pop());
    }
}
