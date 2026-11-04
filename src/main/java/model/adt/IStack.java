package model.adt;

import exceptions.ADTException;
import java.util.List;

public interface IStack<T> {
    void push(T v);
    T pop() throws ADTException;
    boolean isEmpty();
    List<T> toList();
}
