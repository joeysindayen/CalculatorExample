package org.foo.calculator;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

import org.foo.util.Factory;
import org.foo.util.Resolver;

public class ExpressionFactory implements Factory<String, Expression<Double>> {
    private Resolver<String, OperatorFactory<Double>> operatorFactories;
    private Resolver<String, Expression<Double>> constants;
    private Resolver<String, Expression<Double>> variables;

    @Override
    public Expression<Double> create(String expression) {
        Stack<Expression<Double>> stack = new Stack<Expression<Double>>();
        CharacterIterator chars = new StringCharacterIterator(expression);
        while (chars.current() != CharacterIterator.DONE) {
            if (Character.isWhitespace(chars.current())) {
                chars.next();
                continue;
            }
            if (Character.isDigit(chars.current())) {
                stack.push(parseNumber(chars));
            } else if (Character.isLetter(chars.current())) {
                stack.push(parseNamedOperand(chars, stack));
            } else {
                stack.push(parseSymbolOperator(chars, stack));
            }
        }
        return stack.pop();
    }

    public void setOperatorFactories(Resolver<String, OperatorFactory<Double>> operatorFactories) {
        this.operatorFactories = operatorFactories;
    }

    public void setConstants(Resolver<String, Expression<Double>> constants) {
        this.constants = constants;
    }

    public void setVariables(Resolver<String, Expression<Double>> variables) {
        this.variables = variables;
    }

    private Expression<Double> parseNamedOperand(CharacterIterator chars, Stack<Expression<Double>> stack) {
        String name = parseName(chars);
        Expression<Double> operand = constants.resolve(name);
        if (operand != null) {
            return operand;
        }
        OperatorFactory<Double> factory = operatorFactories.resolve(name);
        if (factory != null) {
            return factory.create(stack);
        }
        operand = variables.resolve(name);
        if (operand != null) {
            return operand;
        }
        throw new ParseException(String.format("Unknown operand '%s'", name));
    }

    private Expression<Double> parseSymbolOperator(CharacterIterator chars, Stack<Expression<Double>> stack) {
        String symbol = parseSymbol(chars);
        OperatorFactory<Double> factory = operatorFactories.resolve(symbol);
        if (factory != null) {
            return factory.create(stack);
        }
        throw new ParseException(String.format("Unknown symbol '%s'", symbol));
    }

    private String parseName(CharacterIterator chars) {
        StringBuilder builder = new StringBuilder();
        for (char ch = chars.current(); ch != CharacterIterator.DONE && Character.isLetterOrDigit(ch); ch = chars.next()) {
            builder.append(ch);
        }
        return builder.toString();
    }

    private Expression<Double> parseNumber(CharacterIterator chars) {
        StringBuilder builder = new StringBuilder();
        for (char ch = chars.current(); ch != CharacterIterator.DONE && (Character.isDigit(ch) || ch == '.'); ch = chars.next()) {
            builder.append(ch);
        }
        ConstantExpression<Double> expression = new ConstantExpression<Double>();
        expression.setValue(Double.valueOf(builder.toString()));
        return expression;
    }

    private String parseSymbol(CharacterIterator chars) {
        String operator = String.valueOf(chars.current());
        chars.next();
        return operator;
    }
}