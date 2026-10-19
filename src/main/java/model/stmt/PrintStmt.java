package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.BoolValue;
import model.value.IValue;

public record PrintStmt(IExp exp) implements IStmt { // S4

    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getOut().add(exp.eval(state.getSymTable()));
        return state;
    }
    @Override public String toString() { return "print(" + exp +")"; }
}
