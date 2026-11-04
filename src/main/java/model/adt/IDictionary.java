package model.adt;

import exceptions.ADTException;

import java.util.Map;

public interface IDictionary<K, V>{
    boolean isDefined(K k);
    V lookup(K k) throws ADTException;
    void put(K k, V v);
    void update(K k, V v) throws ADTException;
    void remove(K key) throws ADTException;
    Map<K, V> toMap();
}
