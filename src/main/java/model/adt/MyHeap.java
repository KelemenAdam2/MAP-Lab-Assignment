package model.adt;

import exceptions.ADTException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IHeap {
    private Map<Integer, IValue> heap = new HashMap<>();
    private int nextFree = 1; // address 0 means null

    @Override
    public synchronized int allocate(IValue value) {
        int address = nextFree++;
        heap.put(address, value);
        return address;
    }

    @Override public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }

    @Override
    public synchronized IValue get(int address) throws ADTException {
        if (!heap.containsKey(address)) throw new ADTException("No such address " + address);
        return heap.get(address);
    }

    @Override public synchronized void update(int address, IValue value) throws ADTException{
        if (!heap.containsKey(address)) throw new ADTException("No such address " + address);
        heap.replace(address, value);
    }

    @Override public synchronized boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override public synchronized String toString() { return heap.toString(); }

    @Override public synchronized Map<Integer, IValue> getContent(){
        return Map.copyOf(heap);
    }
}
