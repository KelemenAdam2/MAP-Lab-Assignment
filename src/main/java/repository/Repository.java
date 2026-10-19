package repository;

import model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private final List<PrgState> list = new ArrayList<>();

    public Repository(PrgState state) {
        this.list.add(state);
    }

    @Override public PrgState getCrtPrg() {
        return list.getFirst();
    }
}
