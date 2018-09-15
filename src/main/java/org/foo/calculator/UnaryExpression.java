package org.foo.calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class UnaryExpression implements Expression<Double> {
    private Expression<Double> operand;
    private Method method;

    public Expression<Double> getOperand() {
        return operand;
    }

    public void setOperand(Expression<Double> operand) {
        this.operand = operand;
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
            return (double) method.invoke(null, operand.evaluate());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ParseException(e);
        }
    }
}