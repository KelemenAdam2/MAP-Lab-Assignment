package model.stmt;

import exceptions.MyException;
import model.PrgState;
import model.exp.ValueExp;
import model.type.IntType;
import model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForkStmtTest {

    @Test
    void forkCreatesAChildWithACopyOfTheSymTableAndTheSharedStructures() throws MyException {
        PrgState parent = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new IntType()).execute(parent);
        var body = new AssignStmt("v", new ValueExp(new IntValue(32)));

        PrgState child = new ForkStmt(body).execute(parent).orElseThrow();

        assertNotEquals(parent.getId(), child.getId());
        assertSame(parent.getHeap(), child.getHeap());
        assertSame(parent.getOut(), child.getOut());
        assertSame(parent.getFileTable(), child.getFileTable());
        assertNotSame(parent.getSymTable(), child.getSymTable());

        child.oneStep();
        assertEquals(new IntValue(32), child.getSymTable().lookup("v"));
        assertEquals(new IntValue(0), parent.getSymTable().lookup("v"));
    }

    @Test
    void childExeStackStartsWithForkBodyOnly() throws MyException {
        PrgState parent = PrgState.of(new NopStmt());
        IStmt body = new PrintStmt(new ValueExp(new IntValue(99)));
        ForkStmt fork = new ForkStmt(body);

        PrgState child = fork.execute(parent).orElseThrow();

        assertEquals(1, child.getExeStack().toList().size());
        assertEquals(body, child.getExeStack().pop());
        assertEquals("fork(" + body + ")", fork.toString());
    }
}
