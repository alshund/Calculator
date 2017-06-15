package model.graph.vertex;

import exceptions.CalculatorException;
import model.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shund on 14.06.2017.
 */
public class UnaryOperation implements Node {
    private String operation;
    private Node domain;
    private int depth;

    public UnaryOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public double getResult() throws CalculatorException {
        switch (operation){
            case "%":
                return domain.getResult()/100;
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
        List<Node> domainList = new ArrayList<Node>();
        domainList.add(domain);
        return domainList;
    }

    @Override
    public String getText(int depth) throws CalculatorException {
        if (depth >= domain.getDepth()) {
            return domain.getText(depth) + operation;
        } else {
            return String.valueOf(getResult());
        }
    }

    public void setDomain(Node domain) {
        this.domain = domain;
    }
}
