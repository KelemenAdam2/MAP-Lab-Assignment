package model.exp;

import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.value.IValue;

public interface IExp {
    IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException;
}
