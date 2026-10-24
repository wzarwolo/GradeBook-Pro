import itsc2214.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Project 3 Student Setup.
 * ITSC 2214 Fall 2026
 * 
 * @author Wannie Zarwolo
 * @version Oct 07, 2026
 */
public class Project3Test {
    private ExpressionEvaluator runner = null;

    /**
     * setup() method, runs before each of your test methods.
     * Use this method to recreate the objects needed for
     * testing your class.
     */
    /** Creates a fresh evaluator before each test. */
    @Before
    public void setup() {
        runner = new Project3();
    }

    /**
     * Test balancedParenthesis().
     */
    @Test
    public void testBalancedParenthesis() {
        assertTrue("() should be valid",
                runner.balancedParenthesis("()"));

        assertTrue("Nested parentheses should be valid",
                runner.balancedParenthesis("(1 + (2 * 3))"));
        assertTrue("An expression without parentheses should be valid",
                runner.balancedParenthesis("1 + 2 * 3"));
        assertFalse("An extra closing parenthesis should be invalid",
                runner.balancedParenthesis("())"));
        assertFalse("An opening parenthesis without a match should be invalid",
                runner.balancedParenthesis("((1 + 2)"));
        assertFalse("A closing parenthesis before an opening one should be invalid",
                runner.balancedParenthesis(")1 + 2("));
    }

    /**
     * Test infixToPostfix().
     */
    @Test
    public void testInfixToPostfix() {
        assertArrayEquals(new String[0], new Project3().tokenize(""));

        QueueADT<String> q = runner.infix2Postfix("10 + 20");
        // postfix should be 10 20 +
        assertEquals(3, q.size()); // number of elements in queue
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("+", q.dequeue());
        assertTrue(q.isEmpty());

        q = runner.infix2Postfix("10 + 20 * 5");
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("5", q.dequeue());
        assertEquals("*", q.dequeue());
        assertEquals("+", q.dequeue());
        assertTrue(q.isEmpty());

        q = runner.infix2Postfix("(10 + 20) * 5");
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("+", q.dequeue());
        assertEquals("5", q.dequeue());
        assertEquals("*", q.dequeue());
        assertTrue(q.isEmpty());
    }

    /**
     * Checks that infix conversion rejects invalid tokens and unmatched
     * parentheses.
     */
    @Test
    public void testInvalidInfixExpressions() {
        assertConversionFails("1 + a");
        assertConversionFails(")1 + 2(");
        assertConversionFails("1 + )");
        assertConversionFails("(1 + 2");
    }

    /**
     * Asserts that an infix expression is rejected during conversion.
     *
     * @param expression the invalid infix expression
     */
    private void assertConversionFails(String expression) {
        try {
            runner.infix2Postfix(expression);
            fail("Expected an invalid expression to be rejected: " + expression);
        } catch (IllegalArgumentException expected) {
            // Expected for malformed infix expressions.
        }
    }

    /**
     * Test evaluatePostfix().
     */
    @Test
    public void testEvaluatePostfix() {
        QueueADT<String> q = runner.infix2Postfix("10 + 20");
        assertEquals(30, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 + 20 * 2");
        assertEquals(50, runner.evaluatePostfix(q));

        q = runner.infix2Postfix("10 + (20 * 2)");
        assertEquals(50, runner.evaluatePostfix(q));
        assertEquals(2, runner.evaluatePostfix(runner.infix2Postfix("20 / 10")));
        assertEquals(10, runner.evaluatePostfix(runner.infix2Postfix("20 - 10")));
        assertEquals(-10, runner.evaluatePostfix(runner.infix2Postfix("10 - 20")));

        assertEquals(5, runner.evaluatePostfix(postfixQueue("8", "3", "+")));
        assertEquals(5, runner.evaluatePostfix(postfixQueue("8", "3", "-")));
        assertEquals(24, runner.evaluatePostfix(postfixQueue("8", "3", "*")));
        assertEquals(2, runner.evaluatePostfix(postfixQueue("8", "4", "/")));
    }

    /**
     * Checks that postfix evaluation rejects malformed queues.
     */
    @Test
    public void testInvalidPostfixExpressions() {
        QueueADT<String> q = new QueueArrayList<>();
        q.enqueue("+");
        assertPostfixFails(q);

        q = new QueueArrayList<>();
        q.enqueue("1");
        q.enqueue("2");
        assertPostfixFails(q);

        q = new QueueArrayList<>();
        q.enqueue("1");
        q.enqueue("x");
        assertPostfixFails(q);
    }

    /**
     * Asserts that a postfix queue is rejected during evaluation.
     *
     * @param postfix the malformed postfix queue
     */
    private void assertPostfixFails(QueueADT<String> postfix) {
        try {
            runner.evaluatePostfix(postfix);
            fail("Expected a malformed postfix expression to be rejected");
        } catch (IllegalArgumentException expected) {
            // Expected for malformed postfix expressions.
        }
    }

    /**
     * Test evaluateInfix().
     */
    @Test
    public void testEvaluateInfix() {
        assertEquals(30, runner.evaluateInfix("10 + 20"));
        assertEquals(50, runner.evaluateInfix("10 + 20 * 2"));
        assertEquals(50, runner.evaluateInfix("10 + (20 * 2)"));
        assertEquals(2, runner.evaluateInfix("20 / 10"));
        assertEquals(10, runner.evaluateInfix("20 - 10"));
        assertEquals(-10, runner.evaluateInfix("10 - 20"));
        assertEquals(10, runner.evaluateInfix("(20 - 10) * 2 / 2"));
        assertEquals(6, runner.evaluateInfix("10 - 6 + 2"));
        assertEquals(10, runner.evaluateInfix("2 * 3 + 4"));
        assertEquals(10, runner.evaluateInfix("2 / 1 + 8"));
    }

    /**
     * Creates a postfix queue from the supplied tokens.
     *
     * @param tokens postfix tokens in evaluation order
     * @return a queue containing the supplied tokens
     */
    private QueueADT<String> postfixQueue(String... tokens) {
        QueueADT<String> queue = new QueueArrayList<>();
        for (String token : tokens) {
            queue.enqueue(token);
        }
        return queue;
    }

}
