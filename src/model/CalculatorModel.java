package model;

import constants.CalculatorConstants;
import exceptions.CalculatorException;
import managers.Observable;
import managers.Observer;
import model.graph.Node;
import model.graph.vertex.BinaryOperation;
import model.graph.vertex.Function;
import model.graph.vertex.Number;
import model.graph.vertex.UnaryOperation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shund on 07.06.2017.
 */
public class CalculatorModel implements Observable {
    private Observer observer;
    private Node rootNode;
    private int depth;
    private int maxDepth;

    public CalculatorModel() {
        maxDepth = 0;
        depth = 0;
    }

    @Override
    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void calculateResult(String expression) throws CalculatorException, NoSuchElementException {
        expression = expression.replace(" ", "").replace("(-", "(0-");
        if (expression.charAt(0) == '-') {
            expression = 0 + expression;
        }
        if (isBracketValid(expression)) {
            Stack<String> PRN = parseExpression(new StringTokenizer(expression, CalculatorConstants.DELIMITER, true));
            Stack<Node> graphElements = parsePRN(PRN);
            buildTree(graphElements);
            observer.update(rootNode, depth);
        } else {
            throw new CalculatorException("Некоректно проставлены скобки!");
        }
    }

    private Stack<String> parseExpression(StringTokenizer expression) throws CalculatorException {
        Stack<String> PRN = new Stack<String>();
        Stack<String> operations = new Stack<String>();
        while (expression.hasMoreTokens()) {
            String token = expression.nextToken();
            processToken(token, PRN, operations);
        }
        addRemainingOperations(PRN, operations);
        return PRN;
    }

    private Stack<Node> parsePRN(Stack<String> PRN) throws CalculatorException {
        Stack<Node> graphElements = new Stack<Node>();
        while (!PRN.empty()) {
            String token = PRN.pop();
            graphElements.push(createGraphElement(token));
        }
        return graphElements;
    }

    private void buildTree(Stack<Node> graphElements) throws CalculatorException, NoSuchElementException {
        rootNode = graphElements.firstElement();
        Stack<Node> subsidiaryStack = new Stack<Node>();
        while (!graphElements.empty()) {
            Node currentNode = graphElements.pop();
            buildVertex(subsidiaryStack, currentNode);
        }
        setDepth(rootNode, 0);
        depth = maxDepth;
    }

    private void processToken(String token, Stack<String> PRN, Stack<String> operations) throws CalculatorException {
        if (isNumber(token)) {
            processNumber(token, PRN);
        } else if (isOpenBracket(token)) {
            processOpenBracket(token, operations);
        } else if (isCloseBracket(token)) {
            processCloseBracket(PRN, operations);
        } else if (isBinaryOperation(token) || isUnaryOperation(token)) {
            processOperation(token, PRN, operations);
        } else if (isFunction(token)) {
            processFunction(token, operations);
        } else {
            throw new CalculatorException(CalculatorConstants.OPERATION_ERROR);
        }
    }

    private void processNumber(String number, Stack<String> PRN) {
        PRN.push(number);
    }

    private void processOpenBracket(String openBracket, Stack<String> operations) {
        operations.push(openBracket);
    }

    private void processCloseBracket(Stack<String> PRN, Stack<String> operations) {
        while (!isOpenBracket(operations.lastElement())) {
            PRN.push(operations.pop());
        }
        operations.pop();
        if (!operations.empty() && isFunction(operations.lastElement())) {
            PRN.push(operations.pop());
        }
    }

    private void processOperation(String operation, Stack<String> PRN, Stack<String> operations) {
        while (!operations.empty() && getPriority(operation) >= getPriority(operations.lastElement())) {
            PRN.push(operations.pop());
        }
        operations.push(operation);
        if (isUnaryOperation(operations.lastElement())) {
            PRN.push(operations.pop());
        }
    }

    private void processFunction(String function, Stack<String> operations) {
        operations.push(function);
    }

    private void addRemainingOperations (Stack<String> PRN, Stack<String> operations) {
        while(!operations.isEmpty()) {
            PRN.push(operations.pop());
        }
    }

    private Node createGraphElement(String token) throws CalculatorException {
        if (isNumber(token)) {
            return new Number(Double.parseDouble(token));
        } else if (isBinaryOperation(token)) {
            return new BinaryOperation(token);
        }  else if (isUnaryOperation(token)) {
            return new UnaryOperation(token);
        } else if (isFunction(token)) {
            return new Function(token);
        } else {
            throw new CalculatorException(CalculatorConstants.OPERATION_ERROR);
        }
    }

    private void buildVertex(Stack<Node> subsidiaryStack, Node node) throws CalculatorException {
        switch (node.getArity()) {
            case 0:
                subsidiaryStack.push(node);
                break;
            case 1:
                if (isFunction(node.getName()) && !subsidiaryStack.empty()) {
                    Node domain = subsidiaryStack.pop();
                    ( (Function) node).setDomain(domain);
                    subsidiaryStack.push(node);
                } else if (isUnaryOperation(node.getName()) && !subsidiaryStack.empty()) {
                    Node domain = subsidiaryStack.pop();
                    ( (UnaryOperation) node).setDomain(domain);
                    subsidiaryStack.push(node);
                } else {
                    throw new CalculatorException(CalculatorConstants.OPERATION_ERROR);
                }
                break;
            case 2:
                if (!subsidiaryStack.empty() && subsidiaryStack.peek() != subsidiaryStack.firstElement()) {
                    Node secondDomain = subsidiaryStack.pop();
                    Node firstDomain = subsidiaryStack.pop();
                    ( (BinaryOperation) node).setFirstDomain(firstDomain);
                    ( (BinaryOperation) node).setSecondDomain(secondDomain);
                    subsidiaryStack.push(node);
                } else {
                    throw new CalculatorException(CalculatorConstants.OPERATION_ERROR);
                }
                break;
        }
    }

    public void setDepth(Node node, int depth) {
        if (maxDepth < depth) maxDepth = depth;
        node.setDepth(depth);
        List<Node> domainList = node.getDomainList();
        for (Node domain : domainList) {
            setDepth(domain, depth + 1);
        }
    }

    public void removeData() {
        depth = 0;
        maxDepth = 0;
        observer.removeData();
    }

    public void minimize() throws NullPointerException, CalculatorException {
        if (depth > 0) depth--;
        observer.setInputText(rootNode.getText(depth));
        observer.update(rootNode, depth);
    }

    public void expand() throws NullPointerException, CalculatorException {
        if (depth < maxDepth) depth++;
        observer.setInputText(rootNode.getText(depth));
        observer.update(rootNode, depth);
    }

    private boolean isNumber (String token) {
        Pattern numberPattern = Pattern.compile(CalculatorConstants.REG_NUMBER);
        Matcher numberMatcher = numberPattern.matcher(token);
        return numberMatcher.matches();
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isBinaryOperation(String token) {
        Pattern numberPattern = Pattern.compile(CalculatorConstants.REG_BINARY_OPERATIONS);
        Matcher numberMatcher = numberPattern.matcher(token);
        return numberMatcher.matches();
    }

    private boolean isUnaryOperation(String token) {
        Pattern numberPattern = Pattern.compile(CalculatorConstants.REG_UNARY_OPERATIONS);
        Matcher numberMatcher = numberPattern.matcher(token);
        return numberMatcher.matches();
    }

    private boolean isFunction(String token) {
        for (String function : CalculatorConstants.FUNCTIONS) {
            if (function.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBracketValid (String expression) {
        int openBracketAmount = expression.length() - expression.replace("(", "").length();
        int closeBracketAmount = expression.length() - expression.replace(")", "").length();
        return openBracketAmount == closeBracketAmount;
    }

    private int getPriority(String operator) {
        switch (operator) {
            case "%":
                return 1;
            case "*":
                return 2;
            case "/":
                return 2;
            case "+":
                return 3;
            case "-":
                return 3;
            case "(":
                return 4;
            case ")":
                return 4;
            default:
                return 0;
        }
    }
}
