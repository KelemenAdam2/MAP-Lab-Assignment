package repository;

import exceptions.MyException;
import model.PrgState;

public interface IRepository {
    public PrgState getCrtPrg();
    void logPrgStateExec() throws MyException;
}
