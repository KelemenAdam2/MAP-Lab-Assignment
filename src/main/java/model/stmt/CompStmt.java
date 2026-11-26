package model.stmt;

import model.PrgState;

import java.util.Optional;

public record CompStmt(IStmt first, IStmt second) implements IStmt { // S1

    @Override
    public Optional<PrgState> execute(PrgState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return Optional.empty();
    }
    @Override public String toString() { return "(" + first + "; " + second + ")"; }
}
