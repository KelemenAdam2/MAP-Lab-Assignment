package model.stmt;

import exceptions.MyException;
import model.PrgState;

import java.util.Optional;

public interface IStmt {
    Optional<PrgState> execute(PrgState state) throws MyException;
}
