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

class IfStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void trueConditionPushesThenBranch() throws MyException {
        IStmt thenBranch = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt elseBranch = new PrintStmt(new ValueExp(new IntValue(2)));
        IStmt ifStmt = new IfStmt(new ValueExp(new BoolValue(true)), thenBranch, elseBranch);

        ifStmt.execute(state);
        assertEquals(thenBranch, state.getExeStack().pop());
    }

    @Test
    void falseConditionPushesElseBranch() throws MyException {
        IStmt thenBranch = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt elseBranch = new PrintStmt(new ValueExp(new IntValue(2)));
        IStmt ifStmt = new IfStmt(new ValueExp(new BoolValue(false)), thenBranch, elseBranch);

        ifStmt.execute(state);
        assertEquals(elseBranch, state.getExeStack().pop());
    }

    @Test
    void nonBooleanIfConditionThrows() {
        IStmt thenBranch = new NopStmt();
        IStmt elseBranch = new NopStmt();
        IStmt ifStmt = new IfStmt(new ValueExp(new IntValue(10)), thenBranch, elseBranch);

        var ex = assertThrows(StatementException.class, () -> ifStmt.execute(state));
        assertEquals("condition is not a boolean", ex.getMessage());
    }
}
