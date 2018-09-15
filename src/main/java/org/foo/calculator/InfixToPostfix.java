package org.foo.calculator;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

import org.foo.util.MathRegistry;
import org.foo.util.Transformer;

public enum InfixToPostfix implements Transformer<String, String> {
    INSTANCE;

    @Override
    public String transform(String infix) {
        CharacterIterator chars = new StringCharacterIterator(infix);
        StringBuilder postfix = new StringBuilder();
        Stack<String> stack = new Stack<String>();
        while (chars.current() != CharacterIterator.DONE) {
            if (Character.isWhitespace(chars.current())) {
                skip(chars);
                continue;
            }
            if (Character.isDigit(chars.current())) {
                appendToken(postfix, stack, parseNumber(chars));
                continue;
            }
            if (Character.isLetter(chars.current())) {
                appendNamedToken(postfix, stack, parseName(chars));
                continue;
            }
            String symbol = String.valueOf(chars.current());
            switch (symbol) {
            case "(":
                push(stack, symbol);
                break;
            case ",":
            case ")":
                pop(postfix, stack, symbol);
                break;
            default:
                appendOperator(postfix, stack, symbol);
            }
            skip(chars);
        }
        appendRemaining(postfix, stack);
        return postfix.toString().trim();
    }

    private void appendRemaining(StringBuilder postfix, Stack<String> stack) {
        while (!stack.isEmpty()) {
            String token = stack.pop();
            if ("()".contains(token)) {
                throw new ParseException("Parentheses are not balanced");
            }
            appendToken(postfix, token);
        }
    }

    private void appendOperator(StringBuilder postfix, Stack<String> stack, String symbol) {
        if (Operator.contains(symbol)) {
            while (!stack.isEmpty()) {
                String top = stack.peek();
                if (isMathFunction(top) || Operator.get(symbol).hasPrecedence(Operator.get(top))) {
                    appendToken(postfix, stack.pop());
                } else {
                    break;
                }
            }
            push(stack, symbol);
        } else {
            throw new ParseException(String.format("Unknown symbol '%s'", symbol));
        }
    }

    private void pop(StringBuilder postfix, Stack<String> stack, String symbol) {
        boolean isBalanced = false;
        while (!stack.isEmpty()) {
            String token = stack.pop();
            if ("(".equals(token)) {
                isBalanced = true;
                break;
            } else {
                appendToken(postfix, token);
            }
        }
        if (!isBalanced) {
            throw new ParseException("Parentheses are not balanced");
        }
        if (")".equals(symbol)) {
            if (!stack.isEmpty() && isMathFunction(stack.peek())) {
                appendToken(postfix, stack.pop());
            }
        } else {
            stack.push("(");
        }
    }

    private void push(Stack<String> stack, String symbol) {
        stack.push(symbol);
    }

    private void appendNamedToken(StringBuilder postfix, Stack<String> stack, String name) {
        if (isMathFunction(name)) {
            push(stack, name);
        } else {
            appendToken(postfix, stack, name);
        }
    }

    private void appendToken(StringBuilder postfix, Stack<String> stack, String number) {
        appendToken(postfix, number);
        if (!stack.isEmpty() && isMathFunction(stack.peek())) {
            appendToken(postfix, stack.pop());
        }
    }

    private void skip(CharacterIterator chars) {
        chars.next();
    }

    private StringBuilder appendToken(StringBuilder output, String token) {
        return output.append(" ").append(token);
    }

    private boolean isMathFunction(String name) {
        return MathRegistry.getMathStaticFunctions().containsKey(name);
    }

    private static String parseNumber(CharacterIterator chars) {
        StringBuilder builder = new StringBuilder();
        for (char ch = chars.current(); ch != CharacterIterator.DONE && (Character.isDigit(ch) || ch == '.'); ch = chars.next()) {
            builder.append(ch);
        }
        return builder.toString();
    }

    private static String parseName(CharacterIterator chars) {
        StringBuilder builder = new StringBuilder();
        for (char ch = chars.current(); ch != CharacterIterator.DONE && Character.isLetterOrDigit(ch); ch = chars.next()) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
