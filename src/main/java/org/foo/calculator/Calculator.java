package org.foo.calculator;

import java.util.Arrays;
import java.util.Scanner;

import org.foo.util.Resolver;

public class Calculator {
	public static void main(String... args) {
		testInfixToPostfix();

		System.out.printf("Operators: %s\n", OperatorFactoryResolver.getInstance());
		System.out.printf("Constants: %s\n", Arrays.asList(Constant.values()));
		OperandCollector<Double> variables = new OperandCollector<Double>();

		// XMLEncoder encoder = new XMLEncoder(System.out);

		Scanner console = new Scanner(System.in);
		System.out.print("Please enter an expression: ");
		String infix = console.nextLine();
		String postfix = InfixToPostfix.INSTANCE.transform(infix);
		System.out.printf("Infix  : \"%s\"\n", infix);
		System.out.printf("Postfix: \"%s\"\n", postfix);

		Expression<Double> expression = loadExpressionFactory(variables).create(postfix);
		// 1 + encoder.writeObject(expression);
		// encoder.flush();

		if (variables.get().isEmpty()) {
			System.out.printf("The result of the expression is: %f\n", expression.evaluate());
		} else {
			do {
				System.out.println(variables);
				for (Operand<Double> variable : variables.get()) {
					System.out.printf("Please enter the value of %s: ", variable.getName());
					variable.setValue(Double.valueOf(console.nextLine()));
				}
				System.out.println(variables);
				System.out.printf("The result of the expression is: %f\n", expression.evaluate());
				System.out.print("Enter 'y' if you want to repeat: ");
			} while ("y".equals(console.nextLine()));
		}
		System.out.print("Thank you and goodbye.");

		// encoder.close();
		console.close();
	}

	private static void testInfixToPostfix() {
		String[] expressions = { "30.1 + 5 * 6 - 7 * ( 8 + 5 )" //
				, "30.1 + sin 5 * 6 - 7 * ( 8 + 5 )" //
				, "30.1 + sin(5) * 6 - 7 * ( 8 + 5 )" //
				, "30.1 + 5 * 6 - 7 * cos ( 8 + 5 )" //
				, "pow 0.2 2 PI" //
				, "pow 2 2 PI" //
				, "pow(2,2) PI" //
				, "pow(2 + 3, 2)" };
		for (String infix : expressions) {
			String postfix = InfixToPostfix.INSTANCE.transform(infix);
			System.out.printf("Infix  : \"%s\"\n", infix);
			System.out.printf("Postfix: \"%s\"\n\n", postfix);
		}
	}

	private static ExpressionFactory loadExpressionFactory(Resolver<String, Expression<Double>> variables) {
		ExpressionFactory factory = new ExpressionFactory();
		factory.setConstants(Constant.RESOLVER);
		factory.setOperatorFactories(OperatorFactoryResolver.getInstance());
		factory.setVariables(variables);
		return factory;
	}
}
