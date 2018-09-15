package org.foo.util;

public interface Resolver<K, V> {
    V resolve(K key);
}
