package org.foo.calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BinaryExpression implements Expression<Double> {
    private Expression<Double> b;
    private Expression<Double> a;
    private Method method;

    public Expression<Double> getA() {
        return a;
    }

    public void setA(Expression<Double> a) {
        this.a = a;
    }

    public Expression<Double> getB() {
        return b;
    }

    public void setB(Expression<Double> b) {
        this.b = b;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public Double evaluate() {
        try {
            return (double) method.invoke(null, b.evaluate(), a.evaluate());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ParseException(e);
        }
    }
}