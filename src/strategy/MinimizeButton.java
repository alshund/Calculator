package strategy;

import controller.CalculatorManager;
import exceptions.CalculatorException;

/**
 * Created by shund on 14.06.2017.
 */
public class MinimizeButton implements ButtonStrategy {
    private CalculatorManager calculatorManager;

    public MinimizeButton(CalculatorManager calculatorManager) {
        this.calculatorManager = calculatorManager;
    }

    @Override
    public void execute() throws CalculatorException, NullPointerException {
        calculatorManager.minimize();
    }
}
