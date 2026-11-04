package model.adt;

import exceptions.ADTException;
import java.util.List;

public interface IList<T>{
    void add(T v);
    T get(int id) throws ADTException;
    int size();
    List<T> toList();
}
