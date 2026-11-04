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

class CloseRFileStmtTest {
    @TempDir
    Path tempDir;

    private PrgState state;
    private ValueExp fileExp;
    private StringValue fileStringVal;

    @BeforeEach
    void setUp() throws IOException, MyException {
        Path filePath = tempDir.resolve("close_test.in");
        Files.writeString(filePath, "42\n");
        fileStringVal = new StringValue(filePath.toString());
        fileExp = new ValueExp(fileStringVal);
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void removesFileFromFileTable() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertTrue(state.getFileTable().isDefined(fileStringVal));

        new CloseRFileStmt(fileExp).execute(state);
        assertFalse(state.getFileTable().isDefined(fileStringVal));
    }

    @Test
    void closingFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new CloseRFileStmt(fileExp).execute(state));
    }

    @Test
    void nonStringExpressionThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(new ValueExp(new IntValue(10))).execute(state));
    }
}
