package model.graph.vertex;

import exceptions.CalculatorException;
import model.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shund on 14.06.2017.
 */
public class Number implements Node {
    private double number;
    private int depth;

    public Number(double number) {
        this.number = number;
    }

    @Override
    public double getResult() {
        return number;
    }

    @Override
    public String getName() {
        return String.valueOf(number);
    }

    @Override
    public int getArity() {
        return 0;
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
        return new ArrayList<Node>();
    }

    @Override
    public String getText(int depth) throws CalculatorException {
        return String.valueOf(getResult());
    }

}
