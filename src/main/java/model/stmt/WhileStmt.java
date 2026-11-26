package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.BoolValue;

import java.util.Optional;

public record WhileStmt(IExp cond, IStmt body) implements IStmt { // S13
    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        if (!(cond.eval(state.getSymTable(), state.getHeap()) instanceof BoolValue(boolean b)))
            throw new StatementException("cond: " + cond + " is not a boolean");
        if (b)
        {
            state.getExeStack().push(new WhileStmt(cond, body));
            state.getExeStack().push(body);
        }
        return Optional.empty();
    }
    @Override public String toString() { return "(while (" + cond + ") " + body + ")"; }
}
