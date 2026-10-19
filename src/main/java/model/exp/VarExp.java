package model.exp;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.adt.IDictionary;
import model.value.IValue;

public record VarExp(String str) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException{
        if (symTable.isDefined(str)) return symTable.lookup(str);
        else throw new ExpressionException("variable " + str + " is not defined");
    }

    @Override
    public String toString() {
        return str;
    }
}
