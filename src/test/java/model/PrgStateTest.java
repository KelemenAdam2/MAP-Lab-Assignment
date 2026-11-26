package model;

import exceptions.MyException;
import exceptions.StatementException;
import model.exp.ValueExp;
import model.stmt.AssignStmt;
import model.stmt.NopStmt;
import model.stmt.VarDeclStmt;
import model.type.IntType;
import model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {

    @Test
    void idsAreDistinctAcrossSequentialCreations() {
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            PrgState state = PrgState.of(new NopStmt());
            assertTrue(ids.add(state.getId()), "Duplicate PrgState id: " + state.getId());
        }
        assertEquals(20, ids.size());
    }

    @Test
    void idsAreDistinctAcrossConcurrentCreations() {
        Set<Integer> ids = ConcurrentHashMap.newKeySet();
        try (var executor = Executors.newFixedThreadPool(8)) {
            IntStream.range(0, 1_000).forEach(i ->
                    executor.submit(() -> ids.add(PrgState.of(new NopStmt()).getId())));
        }
        assertEquals(1_000, ids.size());
    }

    @Test
    void isNotCompletedReflectsExeStackState() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        assertTrue(state.isNotCompleted());

        state.oneStep();
        assertFalse(state.isNotCompleted());
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();

        var ex = assertThrows(StatementException.class, state::oneStep);
        assertEquals("program state stack is empty", ex.getMessage());
    }

    @Test
    void oneStepExecutesTopStatement() throws MyException {
        PrgState state = PrgState.of(new VarDeclStmt("v", new IntType()));
        Optional<PrgState> result = state.oneStep();

        assertTrue(result.isEmpty());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
        assertFalse(state.isNotCompleted());
    }

    @Test
    void idAppearsFirstInToLogStringAndInToString() {
        PrgState state = PrgState.of(new AssignStmt("x", new ValueExp(new IntValue(1))));
        String idPrefix = "Id: " + state.getId();

        assertTrue(state.toLogString().startsWith(idPrefix),
                "toLogString should start with '" + idPrefix + "', got:\n" + state.toLogString());
        assertTrue(state.toString().contains(String.valueOf(state.getId())),
                "toString should contain the program state id");
    }
}
