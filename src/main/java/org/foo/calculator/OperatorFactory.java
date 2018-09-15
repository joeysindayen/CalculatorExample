package org.foo.calculator;

import java.util.Stack;

import org.foo.util.Factory;

public interface OperatorFactory<T> extends Factory<Stack<Expression<T>>, Expression<T>> {
    @Override
    Expression<T> create(Stack<Expression<T>> stack);
}
