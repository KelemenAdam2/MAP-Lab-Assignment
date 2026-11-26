package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.IValue;
import model.value.RefValue;

import javax.swing.text.html.Option;
import java.util.Optional;

public record NewStmt(String varName, IExp exp) implements IStmt { // S11
    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("new: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("new: " + varName + " is not a reference");
        IValue value = exp.eval(symTable, state.getHeap());
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("new: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());
        int address = state.getHeap().allocate(value);
        symTable.update(varName, new RefValue(address, ref.locationType()));
        return Optional.empty();
    }
    @Override public String toString() { return "new(" + varName + ", " + exp + ")"; }
}
