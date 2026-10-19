package model.type;

import model.value.IValue;
import model.value.IntValue;

public class IntType implements IType{
    @Override public IValue defaultValue() { return new IntValue(0); }
    @Override public String toString() { return "int"; }
    @Override public boolean equals(Object o) {return o instanceof IntType;}
}
