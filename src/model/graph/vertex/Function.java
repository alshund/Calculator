package model.graph.vertex;

import exceptions.CalculatorException;
import model.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shund on 14.06.2017.
 */
public class Function implements Node {
    private String operation;
    private Node domain;
    private int depth;

    public Function(String operation) {
        this.operation = operation;
    }

    @Override
    public double getResult() throws CalculatorException {
        switch (operation){
            case "sqrt":
                return Math.sqrt(domain.getResult());
            case "sin":
                return Math.sin(domain.getResult());
            case "cos":
                return Math.cos(domain.getResult());
            case "ctg":
                return 1 / Math.tan(domain.getResult());
            case "tg":
                return Math.tan(domain.getResult());
            default:
                return 0;
        }
    }

    @Override
    public String getName() {
        return operation;
    }

    @Override
    public int getArity() {
        return 1;
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
        List <Node> domainList = new ArrayList<Node>();
        domainList.add(domain);
        return domainList;
    }

    @Override
    public String getText(int depth) throws CalculatorException {
        if (depth >= domain.getDepth()) {
            return "(" + operation + "(" + domain.getText(depth) + ")";
        } else {
            return String.valueOf(getResult());
        }
    }

    public void setDomain(Node domain) {
        this.domain = domain;
    }
}
