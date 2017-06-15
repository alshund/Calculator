package strategy;

import controller.CalculatorManager;
import exceptions.CalculatorException;

/**
 * Created by shund on 14.06.2017.
 */
public class ExpandButton implements ButtonStrategy {
    private CalculatorManager calculatorManager;

    public ExpandButton(CalculatorManager calculatorManager) {
        this.calculatorManager = calculatorManager;
    }

    @Override
    public void execute() throws CalculatorException, NullPointerException {
        calculatorManager.expand();
    }
}
