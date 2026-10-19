package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.value.BoolValue;
import model.value.IValue;

public record NopStmt() implements IStmt{ // S6

    @Override
    public PrgState execute(PrgState state) throws MyException {
        return state;
    }
    @Override public String toString() { return "nop"; }
}
