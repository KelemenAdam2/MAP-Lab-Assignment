package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyStack;
import model.exp.ReadHeapExp;
import model.exp.ValueExp;
import model.exp.VarExp;
import model.stmt.*;
import model.type.IntType;
import model.type.RefType;
import model.value.IValue;
import model.value.IntValue;
import model.value.RefValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GarbageCollectorTest {
    @Test
    void unreachableLocationsAreRemoved() {
        // SymTable only points to address 1
        List<IValue> symTable = List.of(new RefValue(1, new IntType()));
        // Heap has address 1 and unreachable address 2 and 3
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(100),
                2, new IntValue(200),
                3, new IntValue(300)
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);

        assertEquals(Set.of(1), kept.keySet());
        assertTrue(kept.containsKey(1));
        assertFalse(kept.containsKey(2));
        assertFalse(kept.containsKey(3));
    }

    @Test
    void locationReachableOnlyThroughTheHeapIsKept() {
        // v -> (3, int), a -> (2, Ref(int))
        // heap: 2 -> (1, int), 1 -> 20, 3 -> 30, 4 -> 7
        // Address 1 is not in symTable roots directly (roots: {2, 3})
        // Address 1 is reachable only through address 2 in heap!
        List<IValue> symTable = List.of(
                new RefValue(3, new IntType()),
                new RefValue(2, new RefType(new IntType()))
        );
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(20),
                2, new RefValue(1, new IntType()),
                3, new IntValue(30),
                4, new IntValue(7) // unreachable
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);

        assertEquals(Set.of(1, 2, 3), kept.keySet());
        assertTrue(kept.containsKey(1)); // location reachable only through heap is kept
        assertTrue(kept.containsKey(2));
        assertTrue(kept.containsKey(3));
        assertFalse(kept.containsKey(4)); // unreachable is removed
    }

    @Test
    void multiLevelIndirectionInHeapIsKept() {
        // a -> (3, Ref(Ref(int)))
        // 3 -> (2, Ref(int)) -> 2 -> (1, int) -> 1 -> 42
        List<IValue> symTable = List.of(new RefValue(3, new RefType(new RefType(new IntType()))));
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(42),
                2, new RefValue(1, new IntType()),
                3, new RefValue(2, new RefType(new IntType())),
                5, new IntValue(99)
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);

        assertEquals(Set.of(1, 2, 3), kept.keySet());
    }

    @Test
    void emptySymTableClearsAllHeap() {
        List<IValue> symTable = List.of();
        Map<Integer, IValue> heap = Map.of(1, new IntValue(10), 2, new IntValue(20));

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);

        assertTrue(kept.isEmpty());
    }

    @Test
    void locationReachableOnlyFromChildSymTableIsKept() throws MyException {
        PrgState parent = PrgState.of(new NopStmt());
        PrgState child = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                parent.getOut(),
                parent.getFileTable(),
                parent.getHeap(),
                new NopStmt()
        );

        int childAddr = child.getHeap().allocate(new IntValue(99));
        int unreachableAddr = parent.getHeap().allocate(new IntValue(123));
        child.getSymTable().put("c", new RefValue(childAddr, new IntType()));

        List<IValue> allSymTableValues = Stream.of(parent, child)
                .flatMap(p -> p.getSymTable().toMap().values().stream())
                .toList();
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(allSymTableValues);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, parent.getHeap().getContent());

        assertTrue(kept.containsKey(childAddr));
        assertFalse(kept.containsKey(unreachableAddr));
    }

    @Test
    void schedulerKeepsLocationReachableOnlyFromChildThread(@TempDir Path dir) throws MyException, InterruptedException {
        IStmt childProgram = new CompStmt(
                new VarDeclStmt("c", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("c", new ValueExp(new IntValue(99))),
                        new PrintStmt(new ReadHeapExp(new VarExp("c")))
                )
        );
        IStmt program = new CompStmt(
                new ForkStmt(childProgram),
                new CompStmt(new NopStmt(), new CompStmt(new NopStmt(), new NopStmt()))
        );

        PrgState mainState = PrgState.of(program);
        var repo = new Repository(mainState, dir.resolve("gc_log.txt").toString());
        new Controller(repo, false).allStep();

        assertEquals(List.of(new IntValue(99)), mainState.getOut().toList());
    }
}
