package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.IHeap;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static controller.GarbageCollector.getAddrFromSymTable;
import static controller.GarbageCollector.safeGarbageCollector;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;
    private ExecutorService executor;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void oneStep(PrgState state) throws MyException {
        state.oneStep();
    }

    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList()); // a mutable list
    }

    void oneStepForAllPrg(List<PrgState> prgList) throws MyException, InterruptedException {
        for (PrgState prg : prgList) repo.logPrgStateExec(prg); // before the step
        List<Callable<Optional<PrgState>>> callList = prgList.stream()
                .map(p -> (Callable<Optional<PrgState>>) p::oneStep)
                .toList();
        List<PrgState> newPrgList = new ArrayList<>();
        for (Future<Optional<PrgState>> future : executor.invokeAll(callList)) {
            try {
                future.get().ifPresent(newPrgList::add); // a new thread?
            } catch (ExecutionException e) { // a statement failed
                if (e.getCause() instanceof MyException me) throw me;
                throw new MyException("unexpected error: " + e.getCause());
            }
        }
        prgList.addAll(newPrgList); // the new threads
        for (PrgState prg : prgList) {
            repo.logPrgStateExec(prg); // after the step
            display(prg);
        }
        repo.setPrgList(prgList);
    }

    public void allStep() throws MyException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        try {
            List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
            while (!prgList.isEmpty()) {
                collectGarbage(prgList); // section 2.7
                oneStepForAllPrg(prgList);
                prgList = removeCompletedPrg(repo.getPrgList());
            }
        } finally {
            executor.shutdownNow(); // also after an error
        }
    }

    private void collectGarbage(List<PrgState> prgList) {
        Set<Integer> roots = getAddrFromSymTable(prgList.stream()
                .flatMap(p -> p.getSymTable().toMap().values().stream())
                .toList()); // the values of ALL SymTables
        IHeap heap = prgList.getFirst().getHeap(); // shared by all threads
        heap.setContent(safeGarbageCollector(roots, heap.getContent()));
    }

    private void display(PrgState state) {
        if (displayFlag) System.out.println(state);
    }
}
