package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.ArithExp;
import model.exp.ArithOp;
import model.exp.RelOp;
import model.exp.RelationalExp;
import model.exp.ValueExp;
import model.exp.VarExp;
import model.type.IntType;
import model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WhileStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    private void runProgram(IStmt stmt) throws MyException {
        state.getExeStack().push(stmt);
        while (!state.getExeStack().isEmpty()) {
            IStmt current = state.getExeStack().pop();
            current.execute(state);
        }
    }

    @Test
    void loopThatRunsZeroTimes() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ValueExp(new IntValue(0))).execute(state);

        IStmt loop = new WhileStmt(
                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                new AssignStmt("v", new ValueExp(new IntValue(99)))
        );

        runProgram(loop);
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopThatRunsOneTime() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ValueExp(new IntValue(1))).execute(state);
        new VarDeclStmt("count", new IntType()).execute(state);

        IStmt loop = new WhileStmt(
                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                new CompStmt(
                        new AssignStmt("count", new ValueExp(new IntValue(1))),
                        new AssignStmt("v", new ValueExp(new IntValue(0)))
                )
        );

        runProgram(loop);
        assertEquals(new IntValue(1), state.getSymTable().lookup("count"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopThatRunsSeveralTimes() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ValueExp(new IntValue(5))).execute(state);
        new VarDeclStmt("sum", new IntType()).execute(state);

        // while (v > 0) { sum = sum + v; v = v - 1; }
        IStmt loop = new WhileStmt(
                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                new CompStmt(
                        new AssignStmt("sum", new ArithExp(ArithOp.ADD, new VarExp("sum"), new VarExp("v"))),
                        new AssignStmt("v", new ArithExp(ArithOp.SUB, new VarExp("v"), new ValueExp(new IntValue(1))))
                )
        );

        runProgram(loop);
        // 5 + 4 + 3 + 2 + 1 = 15
        assertEquals(new IntValue(15), state.getSymTable().lookup("sum"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void nonBooleanConditionThrows() throws MyException {
        IStmt loop = new WhileStmt(new ValueExp(new IntValue(42)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }
}
