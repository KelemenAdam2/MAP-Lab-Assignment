package model.adt;

import exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T>{
    private final List<T> list = new ArrayList<>(); // java.util

    @Override public void add(T v) { list.add(v); }

    @Override public T get(int id) throws ADTException {
        if (id < 0 || id >= list.size()) throw new ADTException("index out of bounds");
        return list.get(id);
    }

    @Override public int size() {return list.size();}
    @Override public String toString() { return list.toString(); }
}
