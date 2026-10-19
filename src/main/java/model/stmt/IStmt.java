package model.stmt;

import exceptions.MyException;
import model.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
}
