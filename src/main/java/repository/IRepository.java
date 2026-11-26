package repository;

import exceptions.MyException;
import model.PrgState;

import java.util.List;

public interface IRepository {
    public PrgState getCrtPrg();
    void logPrgStateExec() throws MyException;

    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    void logPrgStateExec(PrgState state) throws MyException;
}
