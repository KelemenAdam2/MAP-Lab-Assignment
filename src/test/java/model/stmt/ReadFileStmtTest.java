package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.ValueExp;
import model.type.IntType;
import model.type.StringType;
import model.value.IntValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileStmtTest {
    @TempDir
    Path tempDir;

    private PrgState state;
    private ValueExp fileExp;
    private ValueExp nonIntFileExp;

    @BeforeEach
    void setUp() throws IOException, MyException {
        Path numPath = tempDir.resolve("numbers.in");
        Files.writeString(numPath, "10\n20\n");
        fileExp = new ValueExp(new StringValue(numPath.toString()));

        Path nonIntPath = tempDir.resolve("bad.in");
        Files.writeString(nonIntPath, "not_a_number\n");
        nonIntFileExp = new ValueExp(new StringValue(nonIntPath.toString()));

        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void readsNumbersInOrderAndGivesZeroAtEndOfFile() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        new OpenRFileStmt(fileExp).execute(state);

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(20), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        new CloseRFileStmt(fileExp).execute(state);
    }

    @Test
    void variableNotDeclaredThrows() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "undeclared").execute(state));
        new CloseRFileStmt(fileExp).execute(state);
    }

    @Test
    void variableNotIntThrows() throws MyException {
        new VarDeclStmt("s", new StringType()).execute(state);
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "s").execute(state));
        new CloseRFileStmt(fileExp).execute(state);
    }

    @Test
    void fileNotOpenThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "x").execute(state));
    }

    @Test
    void nonIntegerLineThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        new OpenRFileStmt(nonIntFileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(nonIntFileExp, "x").execute(state));
        new CloseRFileStmt(nonIntFileExp).execute(state);
    }
}
