package model.graph;

import exceptions.CalculatorException;

import java.util.List;

/**
 * Created by shund on 14.06.2017.
 */
public interface Node {
    double getResult() throws CalculatorException;
    String getName();
    int getArity();
    void setDepth(int depth);
    int getDepth();
    List<Node> getDomainList();
    String getText(int depth) throws CalculatorException;
}
