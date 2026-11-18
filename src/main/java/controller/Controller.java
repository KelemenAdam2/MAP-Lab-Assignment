package controller;

import exceptions.MyException;
import exceptions.StatementException;
import model.PrgState;
import model.stmt.IStmt;
import repository.IRepository;

import static controller.GarbageCollector.*;

public record Controller(IRepository repo, boolean dflag) {

    public void oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty()) throw new StatementException("program state stack is empty");
        IStmt crtStmt = stack.pop();
        crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        repo.logPrgStateExec();
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            repo.logPrgStateExec();
            prg.getHeap().setContent(safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().toMap().values()),
                    prg.getHeap().getContent()));
            repo.logPrgStateExec();
            display(prg);
        }
    }

    private void display(PrgState state){
        if (dflag) System.out.println(state);
    }
}
