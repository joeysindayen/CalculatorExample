package org.foo.calculator;

public final class Operand<T> implements Expression<T> {
    private String name;
    private T value;

    public Operand(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
