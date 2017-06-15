package strategy;

import controller.CalculatorManager;

/**
 * Created by shund on 14.06.2017.
 */
public class RemoveButton implements ButtonStrategy {
    private CalculatorManager calculatorManager;

    public RemoveButton(CalculatorManager calculatorManager) {
        this.calculatorManager = calculatorManager;
    }

    @Override
    public void execute() {
        calculatorManager.removeData();
    }
}
