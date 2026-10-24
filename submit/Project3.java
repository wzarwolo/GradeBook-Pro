import java.util.Arrays;
import itsc2214.*;

/**
 * Project 3 Setup.
 * ITSC 2214 Fall 2026
 * 
 * @author Wannie Zarwolo
 */
public class Project3 implements ExpressionEvaluator {

    /**
     * Splits an infix arithmetic expression into its number and operator tokens.
     * Whitespace is ignored, and each operator or parenthesis becomes its own
     * token.
     *
     * @param expr the expression to tokenize
     * @return the expression tokens in their original order
     */
    public String[] tokenize(String expr) {
        String expression = expr.replaceAll("\\s+", "");
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
        return Arrays.stream(tokens)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * Determines whether an expression has matching opening and closing
     * parentheses.
     *
     * @param expr the expression to check
     * @return true if the parentheses are balanced; false otherwise
     */
    @Override
    public boolean balancedParenthesis(String expr) {
        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth < 0) {
                    return false;
                }
            }
        }
        return depth == 0;
    }

    /**
     * Converts an infix arithmetic expression to postfix notation.
     * Multiplication and division have higher precedence than addition and
     * subtraction, and operators of equal precedence are evaluated left to right.
     *
     * @param expr the infix expression to convert
     * @return a queue containing the postfix tokens
     * @throws IllegalArgumentException if the expression contains an invalid token
     *                                  or unmatched parentheses
     */
    @Override
    public QueueADT<String> infix2Postfix(String expr) {
        QueueADT<String> output = new QueueArrayList<>();
        StackADT<String> operators = new StackArrayList<>();
        for (String token : tokenize(expr)) {
            if (token.matches("\\d+")) {
                output.enqueue(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.enqueue(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Unbalanced parentheses");
                }
                operators.pop();
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && isOperator(operators.peek())
                        && precedence(operators.peek()) >= precedence(token)) {
                    output.enqueue(operators.pop());
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(")) {
                throw new IllegalArgumentException("Unbalanced parentheses");
            }
            output.enqueue(op);
        }
        return output;
    }

    /**
     * Evaluates an expression already written in postfix notation.
     * The supplied queue is consumed during evaluation.
     *
     * @param postfix the queue of postfix tokens
     * @return the integer result of the expression
     * @throws IllegalArgumentException if the tokens do not form a valid postfix
     *                                  expression
     * @throws ArithmeticException      if division by zero occurs
     */
    @Override
    public int evaluatePostfix(QueueADT<String> postfix) {
        StackADT<Integer> values = new StackArrayList<>();
        while (!postfix.isEmpty()) {
            String token = postfix.dequeue();
            if (token.matches("\\d+")) {
                values.push(Integer.parseInt(token));
            } else if (isOperator(token)) {
                if (values.size() < 2) {
                    throw new IllegalArgumentException("Malformed postfix expression");
                }
                int right = values.pop();
                int left = values.pop();
                switch (token) {
                    case "+":
                        values.push(left + right);
                        break;
                    case "-":
                        values.push(left - right);
                        break;
                    case "*":
                        values.push(left * right);
                        break;
                    case "/":
                        values.push(left / right);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        if (values.size() != 1) {
            throw new IllegalArgumentException("Malformed postfix expression");
        }
        return values.pop();
    }

    /**
     * Converts and evaluates an infix arithmetic expression.
     *
     * @param expr the infix expression to evaluate
     * @return the integer result of the expression
     * @throws IllegalArgumentException if the expression is malformed
     * @throws ArithmeticException      if division by zero occurs
     */
    @Override
    public int evaluateInfix(String expr) {
        return evaluatePostfix(infix2Postfix(expr));
    }

    /**
     * Checks whether a token is one of the supported arithmetic operators.
     *
     * @param token the token to check
     * @return true if the token is an arithmetic operator
     */
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    /**
     * Returns the precedence of an arithmetic operator.
     *
     * @param operator the arithmetic operator
     * @return 1 for addition or subtraction, and 2 for multiplication or division
     */
    private int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        }
        return 2;
    }
}
