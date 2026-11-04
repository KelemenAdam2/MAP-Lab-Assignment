package model.type;

import model.value.IValue;
import model.value.StringValue;

public record StringType() implements IType {
    @Override public IValue defaultValue() { return new StringValue(""); }
    @Override public String toString() { return "string"; }
}
