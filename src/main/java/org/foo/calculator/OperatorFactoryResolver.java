package org.foo.calculator;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.foo.util.MathRegistry;
import org.foo.util.Resolver;

public final class OperatorFactoryResolver implements Resolver<String, OperatorFactory<Double>> {
    private static final Resolver<String, OperatorFactory<Double>> INSTANCE = new OperatorFactoryResolver();

    private Map<String, OperatorFactory<Double>> operatorFactories;

    private OperatorFactoryResolver() {
        super();
        this.operatorFactories = createOperatorFactories();
    }

    @Override
    public OperatorFactory<Double> resolve(String key) {
        return operatorFactories.get(key);
    }

    public static Resolver<String, OperatorFactory<Double>> getInstance() {
        return INSTANCE;
    }

    private Map<String, OperatorFactory<Double>> createOperatorFactories() {
        Map<String, OperatorFactory<Double>> factories = new TreeMap<String, OperatorFactory<Double>>();
        addOperators(factories);
        addMathFunctions(factories);
        return Collections.unmodifiableMap(factories);
    }

    public void addOperators(Map<String, OperatorFactory<Double>> factories) {
        for (final Operator operator : Operator.values()) {
            factories.put(operator.getSymbol(), new OperatorFactory<Double>() {
                @Override
                public Expression<Double> create(Stack<Expression<Double>> stack) {
                    OperatorExpression expression = new OperatorExpression();
                    expression.setOperator(operator);
                    expression.setB(stack.pop());
                    expression.setA(stack.pop());
                    return expression;
                }
            });
        }
    }

    public void addMathFunctions(Map<String, OperatorFactory<Double>> factories) {
        for (final Method method : MathRegistry.getMathStaticFunctions().values()) {
            switch (method.getParameterTypes().length) {
            case 1:
                factories.put(method.getName(), new OperatorFactory<Double>() {
                    @Override
                    public Expression<Double> create(Stack<Expression<Double>> stack) {
                        UnaryExpression expression = new UnaryExpression();
                        expression.setMethod(method);
                        expression.setOperand(stack.pop());
                        return expression;
                    }
                });
                break;
            case 2:
                factories.put(method.getName(), new OperatorFactory<Double>() {
                    @Override
                    public Expression<Double> create(Stack<Expression<Double>> stack) {
                        BinaryExpression expression = new BinaryExpression();
                        expression.setMethod(method);
                        expression.setB(stack.pop());
                        expression.setA(stack.pop());
                        return expression;
                    }
                });
                break;
            }
        }
    }

    @Override
    public String toString() {
        return operatorFactories.keySet().toString();
    }
}
