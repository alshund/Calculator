package model.graph.vertex;

import exceptions.CalculatorException;
import model.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shund on 14.06.2017.
 */
public class BinaryOperation implements Node {
    private String operation;
    private Node firstDomain;
    private Node secondDomain;
    private int depth;

    public BinaryOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public double getResult() throws CalculatorException {
        switch (operation){
            case "+":
                return firstDomain.getResult() + secondDomain.getResult();
            case "-":
                return firstDomain.getResult() - secondDomain.getResult();
            case "*":
                return firstDomain.getResult() * secondDomain.getResult();
            case "/":
                return firstDomain.getResult() / secondDomain.getResult();
        }
        throw new CalculatorException("");
    }

    @Override
    public String getName() {
        return operation;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public List<Node> getDomainList() {
        List<Node> domainList = new ArrayList<Node>();
        domainList.add(firstDomain);
        domainList.add(secondDomain);
        return domainList;
    }

    @Override
    public String getText(int depth) throws CalculatorException {
        if (depth >= firstDomain.getDepth() && depth >= secondDomain.getDepth()) {
            return "(" + firstDomain.getText(depth) + operation + secondDomain.getText(depth) + ")";
        } else {
            return String.valueOf(getResult());
        }
    }

    public void setFirstDomain(Node firstDomain) {
        this.firstDomain = firstDomain;
    }

    public void setSecondDomain(Node secondDomain) {
        this.secondDomain = secondDomain;
    }
}
