package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.IValue;
import model.value.RefValue;

import java.util.Optional;

public record WriteHeapStmt(String varName, IExp exp) implements IStmt { // S12
    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        if (!symTable.isDefined(varName))
            throw new StatementException("new: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("new: " + varName + " is not a reference");
        IValue value = exp.eval(symTable, heap);
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("new: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());
        if (!heap.isDefined(ref.address()))
            throw new StatementException("rH: " + exp + " is not a defined address");
        heap.update(ref.address(), value);
        return Optional.empty();
    }
    @Override public String toString() { return "wH(" + varName + ", " + exp + ")"; }
}
