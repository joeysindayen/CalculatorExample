package org.foo.calculator;

public final class ConstantExpression<T> implements Expression<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return value;
    }
}