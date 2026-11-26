package model.adt;

import exceptions.ADTException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MyStack<T> implements IStack<T> {
    private final Deque<T> stack = new ArrayDeque<>(); // java.util

    @Override public synchronized void push(T v) { stack.push(v); }

    @Override public synchronized T pop() throws ADTException {
        if (stack.isEmpty()) throw new ADTException("pop from an empty stack");
        return stack.pop();
    }

    @Override public synchronized boolean isEmpty() { return stack.isEmpty(); }
    @Override public synchronized String toString() { return stack.toString(); }
    @Override public synchronized List<T> toList() { return List.copyOf(stack); }
}
