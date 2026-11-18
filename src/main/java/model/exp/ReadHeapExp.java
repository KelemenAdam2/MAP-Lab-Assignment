package model.exp;

import exceptions.MyException;
import exceptions.StatementException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.value.IValue;
import model.value.RefValue;

public record ReadHeapExp(IExp exp) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(exp.eval(symTable, heap) instanceof RefValue ref))
            throw new StatementException("rH: " + exp + " is not a reference");
        if (!heap.isDefined(ref.address()))
            throw new StatementException("rH: " + exp + " is not a defined address");
        return heap.get(ref.address());
    }

    @Override
    public String toString() {return "rH(" + exp + ")";}
}
