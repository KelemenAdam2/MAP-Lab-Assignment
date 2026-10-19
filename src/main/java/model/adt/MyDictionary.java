package model.adt;

import exceptions.ADTException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> dict = new HashMap<>(); // java.util

    @Override public boolean isDefined(K k) { return dict.containsKey(k); }

    @Override
    public V lookup(K k) throws ADTException {
        if (!dict.containsKey(k)) throw new ADTException("key not found");
        return dict.get(k);
    }

    @Override public void put(K k, V v) {dict.putIfAbsent(k, v);}

    @Override public void update(K k, V v) throws ADTException {
        if (!dict.containsKey(k)) throw new ADTException("key not found");
        dict.replace(k,v);
    }

    @Override public String toString() { return dict.toString(); }
}
