package model.adt;

import exceptions.ADTException;
import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T>{
    private final List<T> list = new ArrayList<>(); // java.util

    @Override public synchronized void add(T v) { list.add(v); }

    @Override public synchronized T get(int id) throws ADTException {
        if (id < 0 || id >= list.size()) throw new ADTException("index out of bounds");
        return list.get(id);
    }

    @Override public synchronized int size() {return list.size();}
    @Override public synchronized String toString() { return list.toString(); }
    @Override public synchronized List<T> toList() { return List.copyOf(list); }
}
