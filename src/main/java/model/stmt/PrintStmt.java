package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.BoolValue;
import model.value.IValue;

import java.util.Optional;

public record PrintStmt(IExp exp) implements IStmt { // S4

    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        state.getOut().add(exp.eval(state.getSymTable(),  state.getHeap()));
        return Optional.empty();
    }
    @Override public String toString() { return "print(" + exp +")"; }
}
