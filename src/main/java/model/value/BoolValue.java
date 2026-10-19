package model.value;

import model.type.BoolType;
import model.type.IType;

public record BoolValue(boolean b) implements IValue{
    @Override public IType getType() { return new BoolType(); }
    @Override public String toString() { return Boolean.toString(b); }
}
