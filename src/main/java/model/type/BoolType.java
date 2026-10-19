package model.type;

import model.value.BoolValue;
import model.value.IValue;

public class BoolType implements IType{
    @Override public IValue defaultValue() { return new BoolValue(false); }
    @Override public String toString() { return "bool"; }
    @Override public boolean equals(Object o) {return o instanceof BoolType;}
}
