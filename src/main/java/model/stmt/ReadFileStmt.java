package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.IntValue;
import model.value.StringValue;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {// S9
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable(),  state.getHeap()) instanceof StringValue fileName))
            throw new StatementException("ReadFile: " + exp + " is not a string");

        var fileTable = state.getFileTable();

        if (!fileTable.isDefined(fileName))
            throw new StatementException("ReadFile: " + fileName + " is not open");

        if (!state.getSymTable().isDefined(varName))
            throw new StatementException("ReadFile: " + varName + " is not defined");

        if (!(state.getSymTable().lookup(varName) instanceof IntValue))
            throw new StatementException("ReadFile: " + varName + " is not a int");

        try {
            String line = fileTable.lookup(fileName).readLine();
            if (line == null) state.getSymTable().update(varName, new IntValue(0));
            else state.getSymTable().update(varName, new IntValue(Integer.parseInt(line.strip())));
        } catch (IOException e) {
            throw new StatementException("ReadFile: cannot open " + fileName + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StatementException("ReadFile: " + fileName + " is not a number");
        }
        return state;
    }
    @Override public String toString() { return "ReadFile(" + exp + ")"; }
}
