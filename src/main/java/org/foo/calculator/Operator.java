package org.foo.calculator;

import java.util.HashMap;
import java.util.Map;

public enum Operator {
    EXPONENT("^") {
        @Override
        public double evaluate(double a, double b) {
            return Math.pow(a, b);
        }
    },
    MULTIPLY("*") {
        @Override
        public double evaluate(double a, double b) {
            return a * b;
        }
    },
    DIVIDE("/") {
        @Override
        public double evaluate(double a, double b) {
            return a / b;
        }
    },
    ADD("+") {
        @Override
        public double evaluate(double a, double b) {
            return a + b;
        }
    },
    SUBTRACT("-") {
        @Override
        public double evaluate(double a, double b) {
            return a - b;
        }
    };

    private static final Map<String, Operator> REGISTRY = new HashMap<String, Operator>();
    static {
        for (Operator operator : values()) {
            REGISTRY.put(operator.getSymbol(), operator);
        }
    }

    private String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean hasPrecedence(Operator operator) {
        return operator != null && compareTo(operator) > 0;
    }

    public static boolean contains(String symbol) {
        return REGISTRY.containsKey(symbol);
    }

    public static Operator get(String symbol) {
        return REGISTRY.get(symbol);
    }

    public abstract double evaluate(double a, double b);
}
