package model.stmt;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.exp.IExp;
import model.value.StringValue;
import java.io.IOException;
import java.util.Optional;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public Optional<PrgState> execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable(),  state.getHeap()) instanceof StringValue fileName))
            throw new StatementException("closeRFile: " + exp + " is not a string");

        var fileTable = state.getFileTable();

        if (!fileTable.isDefined(fileName))
            throw new StatementException("closeRFile: " + fileName + " is not open");

        try {
            fileTable.lookup(fileName).close();
            fileTable.remove(fileName);
        } catch (IOException e) {
            throw new StatementException("closeRFile: cannot close " + fileName
                    + ": " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override public String toString() { return "closeRFile(" + exp + ")"; }
}
