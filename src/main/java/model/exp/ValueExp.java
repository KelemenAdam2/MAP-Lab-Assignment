package model.exp;

import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.value.IValue;

public record ValueExp(IValue value) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
