package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.ValueExp;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.IntValue;
import model.value.RefValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void allocatesAndUpdatesTheVariable() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        assertTrue(new NewStmt("v", new ValueExp(new IntValue(20))).execute(state).isEmpty());

        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));

        // allocate again updates the variable with new address
        new NewStmt("v", new ValueExp(new IntValue(30))).execute(state);
        assertEquals(new RefValue(2, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void variableNotDeclaredThrows() {
        assertThrows(StatementException.class, () -> new NewStmt("undeclared", new ValueExp(new IntValue(20))).execute(state));
    }

    @Test
    void variableNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class, () -> new NewStmt("x", new ValueExp(new IntValue(20))).execute(state));
    }

    @Test
    void valueOfTheWrongTypeThrows() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        assertThrows(StatementException.class, () -> new NewStmt("v", new ValueExp(new StringValue("bad"))).execute(state));

        new VarDeclStmt("s", new RefType(new StringType())).execute(state);
        assertThrows(StatementException.class, () -> new NewStmt("s", new ValueExp(new IntValue(10))).execute(state));
    }
}
