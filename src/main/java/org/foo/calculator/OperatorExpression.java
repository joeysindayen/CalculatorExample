package org.foo.calculator;

public final class OperatorExpression implements Expression<Double> {
    private Operator operator;
    private Expression<Double> a;
    private Expression<Double> b;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

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

    @Override
    public Double evaluate() {
        return operator.evaluate(b.evaluate(), a.evaluate());
    }
}