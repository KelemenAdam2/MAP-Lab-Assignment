package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.value.BoolValue;
import model.value.IValue;

import java.util.Optional;

public record NopStmt() implements IStmt{ // S6

    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        return Optional.empty();
    }
    @Override public String toString() { return "nop"; }
}
