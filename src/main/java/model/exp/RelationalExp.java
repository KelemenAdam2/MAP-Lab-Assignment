package model.exp;

import exceptions.ExpressionException;
import exceptions.MyException;
import model.adt.IDictionary;
import model.adt.IHeap;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public record RelationalExp(RelOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(left.eval(symTable, heap) instanceof IntValue(int n1)))
            throw new ExpressionException("first operand is not an integer");
        if (!(right.eval(symTable, heap) instanceof IntValue(int n2)))
            throw new ExpressionException("second operand is not an integer");
        return new BoolValue(switch (op) {
            case LT -> n1 < n2;
            case LE -> n1 <= n2;
            case EQ -> n1 == n2;
            case NE -> n1 != n2;
            case GT -> n1 > n2;
            case GE -> n1 >= n2;
        });
    }
    @Override
    public String toString() { return "(" + left + " " + op + " " + right + ")"; }
}
