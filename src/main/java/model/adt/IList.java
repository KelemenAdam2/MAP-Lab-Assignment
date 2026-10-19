package model.adt;

import exceptions.ADTException;

public interface IList<T>{
    void add(T v);
    T get(int id) throws ADTException;
    int size();
}
