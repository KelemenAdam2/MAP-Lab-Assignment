package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

import java.util.Optional;

public record VarDeclStmt(String name, IType type) implements IStmt { // S3

    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        if (state.getSymTable().isDefined(name))
            throw new StatementException(name + " already declared");

        state.getSymTable().put(name,type.defaultValue());
        return Optional.empty();
    }
    @Override public String toString() { return type + " " + name; }
}
