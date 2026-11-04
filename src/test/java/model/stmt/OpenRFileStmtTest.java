package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.ValueExp;
import model.value.IntValue;
import model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OpenRFileStmtTest {
    @TempDir
    Path tempDir;

    private PrgState state;
    private ValueExp fileExp;
    private StringValue fileStringVal;

    @BeforeEach
    void setUp() throws IOException, MyException {
        Path filePath = tempDir.resolve("sample.in");
        Files.writeString(filePath, "123\n");
        fileStringVal = new StringValue(filePath.toString());
        fileExp = new ValueExp(fileStringVal);
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void addsFileToFileTable() throws MyException, IOException {
        IStmt stmt = new OpenRFileStmt(fileExp);
        stmt.execute(state);

        assertTrue(state.getFileTable().isDefined(fileStringVal));
        assertNotNull(state.getFileTable().lookup(fileStringVal));
        // clean up reader
        state.getFileTable().lookup(fileStringVal).close();
    }

    @Test
    void nonStringExpressionThrows() {
        IStmt stmt = new OpenRFileStmt(new ValueExp(new IntValue(42)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void fileAlreadyOpenThrows() throws MyException, IOException {
        IStmt stmt = new OpenRFileStmt(fileExp);
        stmt.execute(state);

        assertThrows(StatementException.class, () -> stmt.execute(state));
        state.getFileTable().lookup(fileStringVal).close();
    }

    @Test
    void missingFileThrows() {
        ValueExp missingFileExp = new ValueExp(new StringValue(tempDir.resolve("missing.in").toString()));
        IStmt stmt = new OpenRFileStmt(missingFileExp);

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }
}
