package controller;

import exceptions.CalculatorException;
import model.CalculatorModel;

import java.util.NoSuchElementException;

/**
 * Created by shund on 07.06.2017.
 */
public class CalculatorManager {
    private CalculatorModel calculatorModel;

    public CalculatorManager(CalculatorModel calculatorModel) {
        this.calculatorModel = calculatorModel;
    }

    public void calculateResult(String expression) throws CalculatorException, NoSuchElementException {
        calculatorModel.calculateResult(expression);
    }

    public void removeData() {
        calculatorModel.removeData();
    }

    public void minimize() throws CalculatorException, NullPointerException {
        calculatorModel.minimize();
    }

    public void expand() throws CalculatorException, NullPointerException {
        calculatorModel.expand();
    }

    public CalculatorModel getCalculatorModel() {
        return calculatorModel;
    }
}
