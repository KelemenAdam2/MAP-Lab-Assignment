package controller;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyHeap;
import model.adt.MyList;
import model.adt.MyStack;
import model.stmt.IStmt;
import model.stmt.NopStmt;
import model.value.IValue;
import model.value.IntValue;
import model.value.RefValue;
import model.type.IntType;
import model.type.RefType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.Repository;
import view.Examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private PrgState emptyState;
    private Controller emptyController;
    @TempDir
    Path dir;

    @BeforeEach
    void setUp() throws MyException {
        emptyState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new NopStmt());
        emptyState.getExeStack().pop();
        emptyController = new Controller(new Repository(emptyState, dir.toString()), false);
    }


    private PrgState run(IStmt program) throws MyException, InterruptedException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep(); // display off
        return state;
    }

    @Test
    void completeExecutionOfExample1PrintsTwo() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample2PrintsEighteen() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample3PrintsTwo() throws MyException, InterruptedException {
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

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws MyException, InterruptedException, IOException {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(15), s.getOut().get(0));
        assertEquals(new IntValue(50), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithEmptyFilePrintsZeroZero() throws MyException, InterruptedException, IOException {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(0), s.getOut().get(0));
        assertEquals(new IntValue(0), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void completeExecutionOfExample5PrintsFive() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX5);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample6PrintsRefValues() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX6);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(2, state.getOut().size());
        assertEquals(new RefValue(1, new IntType()), state.getOut().get(0));
        assertEquals(new RefValue(2, new RefType(new IntType())), state.getOut().get(1));
    }

    @Test
    void completeExecutionOfExample7PrintsTwentyAndTwentyFive() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX7);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(20), state.getOut().get(0));
        assertEquals(new IntValue(25), state.getOut().get(1));
    }

    @Test
    void completeExecutionOfExample8PrintsTwentyAndThirtyFive() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX8);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(20), state.getOut().get(0));
        assertEquals(new IntValue(35), state.getOut().get(1));
    }

    @Test
    void completeExecutionOfExample9PrintsTwenty() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX9);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(20), state.getOut().get(0));
    }

    @Test
    void completeExecutionOfExample10PrintsFourThreeTwoOneZero() throws MyException, InterruptedException {
        PrgState state = run(Examples.EX10);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(5, state.getOut().size());
        assertEquals(new IntValue(4), state.getOut().get(0));
        assertEquals(new IntValue(3), state.getOut().get(1));
        assertEquals(new IntValue(2), state.getOut().get(2));
        assertEquals(new IntValue(1), state.getOut().get(3));
        assertEquals(new IntValue(0), state.getOut().get(4));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws MyException, InterruptedException, IOException {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void removeCompletedPrgKeepsOnlyRunningStates() throws MyException {
        PrgState running = PrgState.of(new NopStmt());
        PrgState completed = PrgState.of(new NopStmt());
        completed.getExeStack().pop();

        var repo = new Repository(running, dir.resolve("log_remove.txt").toString());
        var controller = new Controller(repo, false);

        List<PrgState> remaining = controller.removeCompletedPrg(List.of(completed, running));

        assertEquals(1, remaining.size());
        assertSame(running, remaining.getFirst());
    }

    @Test
    void ex11ProducesExpectedSymTablesHeapAndOutput() throws MyException, InterruptedException, IOException {
        PrgState mainState = PrgState.of(Examples.EX11);
        Path logPath = dir.resolve("log11.txt");
        var repo = new Repository(mainState, logPath.toString());
        var controller = new Controller(repo, false);

        controller.allStep();

        // Main thread SymTable: {v -> 10, a -> (1, int)}
        assertEquals(new IntValue(10), mainState.getSymTable().lookup("v"));
        assertEquals(new RefValue(1, new IntType()), mainState.getSymTable().lookup("a"));

        // Child thread SymTable (last round state kept in repository): {v -> 32, a -> (1, int)}
        PrgState childState = repo.getPrgList().stream()
                .filter(p -> p.getId() != mainState.getId())
                .findFirst()
                .orElseThrow();
        assertEquals(new IntValue(32), childState.getSymTable().lookup("v"));
        assertEquals(new RefValue(1, new IntType()), childState.getSymTable().lookup("a"));

        // Heap: {1 -> 30}
        assertEquals(Map.of(1, new IntValue(30)), mainState.getHeap().getContent());

        // Out: {10, 30, 32, 30} (order of prints in the same round may vary)
        List<IValue> out = mainState.getOut().toList();
        assertEquals(4, out.size());
        Map<IValue, Long> counts = out.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertEquals(1L, counts.get(new IntValue(10)));
        assertEquals(2L, counts.get(new IntValue(30)));
        assertEquals(1L, counts.get(new IntValue(32)));

        // Log file records both thread ids
        String log = Files.readString(logPath);
        assertTrue(log.contains("Id: " + mainState.getId()));
        assertTrue(log.contains("Id: " + childState.getId()));
    }
}