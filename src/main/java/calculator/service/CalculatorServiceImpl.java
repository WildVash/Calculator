package calculator.service;

import calculator.service.interfaces.CalculatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    public String calculate(String input) {
        double result;

        try {
            result = processInput(input);
        } catch (Exception c) {
            return "ERROR";
        }

        if (result % 1 == 0 && result < Integer.MAX_VALUE) {
            return String.valueOf((int) result);
        }
        return String.valueOf(result);
    }

    private double processInput(String input) {
        String[] exampleParts = input.split(" ");

        Deque<Double> result = new ArrayDeque<>();
        Deque<String> operations = new ArrayDeque<>();

        doCalculation(exampleParts, result, operations);
        doFinalCalculations(result, operations);

        return result.pop();
    }

    private void doCalculation(String[] input, Deque<Double> result, Deque<String> operations) {
        for (int i = 0; i < input.length; i++) {
            String part = input[i].trim();

            if (isDigit(part)) {
                result.push(Double.parseDouble(part));
            } else if ("+".equals(part) || "-".equals(part)) {
                operations.push(part);
            } else if ("*".equals(part) || "/".equals(part)) {
                String nextPart = input[i + 1].trim();

                if (!isDigit(nextPart)) {
                    operations.push(part);
                } else {
                    result.push(doMath(result.pop(), Double.parseDouble(nextPart), part));
                    i++;
                }
            } else if ("(".equals(part)) {
                operations.push(part);
            } else if (")".equals(part)) {
                calculateWhenParenthesesAreClosed(result, operations);
            }
        }
    }

    private boolean isDigit(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!Character.isDigit(c) && c != '.') {
                return false;
            }
        }
        return true;
    }

    private void doFinalCalculations(Deque<Double> result, Deque<String> operations) {
        while (operations.size() > 0) {
            double first = result.removeLast();
            double second = result.removeLast();
            result.addLast(doMath(first, second, operations.removeLast()));
        }
    }

    private void calculateWhenParenthesesAreClosed(Deque<Double> result, Deque<String> operations) {
        Stack<Double> pResult = new Stack<>();
        Stack<String> pOperations = new Stack<>();

        while (operations.size() > 0 && !"(".equals(operations.peek())) {
            pOperations.push(operations.pop());
            pResult.push(result.pop());
        }

        pResult.push(result.pop());
        operations.pop();

        while (pOperations.size() > 0) {
            pResult.push(doMath(pResult.pop(), pResult.pop(), pOperations.pop()));
        }

        if (pResult.size() == 1) {
            result.push(pResult.pop());
        }

        if (operations.size() > 0) {
            String last = operations.peek();
            if ("*".equals(last) || "/".equals(last)) {
                double second = result.pop();
                double first = result.pop();
                result.push(doMath(first, second, operations.pop()));
            }
        }
    }

    private double doMath(double first, double second, String operation) {
        switch (operation) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            case "/":
                return first / second;
            default:
                throw new RuntimeException("Illegal math operation");
        }
    }
}
