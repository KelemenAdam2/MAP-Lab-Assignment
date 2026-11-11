package model.type;

import model.value.IValue;
import model.value.RefValue;

public record RefType(IType inner) implements IType {
    @Override public IValue defaultValue() { return new RefValue(0, inner); } // null
    @Override public String toString() { return "Ref(" + inner + ")"; }
}
