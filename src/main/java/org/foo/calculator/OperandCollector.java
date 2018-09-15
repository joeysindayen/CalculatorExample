package org.foo.calculator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.foo.util.Collector;
import org.foo.util.Resolver;

public class OperandCollector<T> implements Collector<String, Operand<T>>, Resolver<String, Expression<T>> {
    private Map<String, Operand<T>> operands = new TreeMap<String, Operand<T>>();

    @Override
    public Collection<Operand<T>> get() {
        return Collections.unmodifiableCollection(operands.values());
    }

    @Override
    public Operand<T> add(String key) {
        Operand<T> operand = operands.get(key);
        if (operand == null) {
            operand = new Operand<T>(key);
            operands.put(key, operand);
        }
        return operand;
    }

    @Override
    public Expression<T> resolve(String key) {
        return operands.get(key);
    }

    @Override
    public String toString() {
        return operands.values().toString();
    }
}
