package model.value;

import model.type.IType;
import model.type.StringType;

public record StringValue(String val) implements IValue {
    @Override public IType getType() { return new StringType(); }
    @Override public String toString() { return "\"" + val + "\""; }
}
