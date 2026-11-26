package model.stmt;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyStack;

import java.util.Optional;

public record ForkStmt(IStmt stmt) implements IStmt { // S14
    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        PrgState child = new PrgState(new MyStack<>(),
                state.getSymTable().copy(), // a copy: not shared
                state.getOut(), // shared
                state.getFileTable(), // shared
                state.getHeap(), // shared
                stmt); // the program of the child
        return Optional.of(child);
    }
    @Override public String toString() { return "fork(" + stmt + ")"; }
}
