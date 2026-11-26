package model;

import exceptions.MyException;
import exceptions.StatementException;
import model.adt.*;
import model.stmt.IStmt;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.util.Optional;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IHeap heap;
    private final IStmt originalProgram;

    private final int id;
    private static int nextId = 1;

    private static synchronized int newId() { return nextId++; }

    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable, IList<IValue> out, IDictionary<StringValue, BufferedReader> fileTable, IHeap heap, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program;
        exeStack.push(program);
        this.id = newId();
    }

    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), new MyHeap(), program);
    }

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(IDictionary<StringValue, BufferedReader> symTable) {this.fileTable = fileTable; }

    public IHeap getHeap() {
        return heap;
    }

    public void setHeap(IHeap heap) {this.heap = heap; }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    @Override
    public String toString() {
        return """
                === Program State ===
                ExeStack: %s
                SymTable: %s
                Out:      %s
                FileTable: %s
                Heap: %s
                Id: %s
                ---------------------""".formatted(exeStack, symTable, out, fileTable, heap, id);
    }

    public String toLogString() {
        var sb = new StringBuilder();
        sb.append("Id: ").append(id).append("\n");
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(s -> sb.append(s).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((k, v) ->
                sb.append(k).append(" --> ").append(v).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(v -> sb.append(v).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(f -> sb.append(f.val()).append('\n'));
        heap.getContent().forEach((i, v) ->
                sb.append(i).append(" --> ").append(v).append('\n'));
        return sb.toString();
    }


    public int getId() { return id; }
    public boolean isNotCompleted() { return !exeStack.isEmpty(); }
    public Optional<PrgState> oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new StatementException("program state stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }
}