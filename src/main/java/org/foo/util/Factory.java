package org.foo.util;

public interface Factory<C, T> {
    T create(C context);
}
