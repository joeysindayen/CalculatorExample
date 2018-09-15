package org.foo.util;

public interface Transformer<T1, T2> {
    T2 transform(T1 entity);
}
