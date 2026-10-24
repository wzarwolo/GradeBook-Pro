# Expression Evaluator

A Java-based expression evaluator implemented for ITSC 2214 Data Structures. This project converts infix arithmetic expressions into postfix notation, validates parentheses, and evaluates expressions using a stack-based approach.

## Overview

The program supports basic arithmetic expressions using:

- addition (`+`)
- subtraction (`-`)
- multiplication (`*`)
- division (`/`)
- parentheses for grouping

It demonstrates core data structure concepts including:

- tokenization
- stack operations
- queue-based postfix processing
- expression validation
- precedence handling

## Features

- Checks whether an expression has balanced parentheses
- Converts infix expressions to postfix notation
- Evaluates postfix expressions safely
- Computes infix expressions directly through conversion and evaluation
- Rejects invalid tokens and malformed expressions with clear errors

## Project Structure

- `src/Main.java` – sample driver program used for testing and demonstration
- `submit/Project3.java` – main implementation submitted for the assignment
- `submit/Project3Test.java` – JUnit tests covering valid and invalid expressions

## Example Expressions

- `10 + 20`
- `10 + 20 * 5`
- `(10 + 20) * 5`
- `2 * (3 + 4)`

## How It Works

1. The expression is tokenized into numbers, operators, and parentheses.
2. Parentheses are checked for balance.
3. Infix notation is converted to postfix using operator precedence.
4. The postfix queue is evaluated using a stack.
5. The final result is returned as an integer.

## Usage

Compile and run the sample driver:

```bash
javac -d bin src/Main.java
java -cp bin Main
```

You can also run the JUnit tests in the `submit` folder using your Java project setup.

## Example Output

```text
Infix: 10 + (20 * 3) / 2
Valid parenthesis
Postfix: 10 20 3 * 2 / +
result = 40
```

## Author

Wannie Zarwolo

## Course

ITSC 2214 Fall 2026
