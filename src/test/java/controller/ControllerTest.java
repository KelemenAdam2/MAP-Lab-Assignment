package controller;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.stmt.IStmt;
import model.stmt.NopStmt;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;
import view.Examples;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private PrgState emptyState;
    private Controller emptyController;

    @BeforeEach
    void setUp() throws MyException {
        emptyState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        emptyState.getExeStack().pop();
        emptyController = new Controller(new Repository(emptyState), false);
    }

    private PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        new Controller(new Repository(state), false).allStep();
        return state;
    }

    @Test
    void completeExecutionOfExample1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void oneStepOnEmptyStackThrows() {
        var ex = assertThrows(StatementException.class, () -> emptyController.oneStep(emptyState));
        assertEquals("program state stack is empty", ex.getMessage());
    }
}