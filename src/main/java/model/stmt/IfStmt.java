package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.BoolValue;
import model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt{// S5

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue c = cond.eval(state.getSymTable());
        if (!(c instanceof BoolValue(boolean b)))
            throw new StatementException("condition is not a boolean");
        if (b)
            state.getExeStack().push(thenS);
        else
            state.getExeStack().push(elseS);
        return state;
    }
    @Override public String toString() { return "if" + cond + "then" + thenS + "else" + elseS; }
}
