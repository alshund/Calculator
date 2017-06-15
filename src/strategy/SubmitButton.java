package strategy;

import controller.CalculatorManager;
import exceptions.CalculatorException;

import javax.swing.*;
import java.util.NoSuchElementException;

/**
 * Created by shund on 07.06.2017.
 */
public class SubmitButton implements ButtonStrategy {
    private CalculatorManager calculatorManager;
    private JTextField textField;

    public SubmitButton(CalculatorManager calculatorManager, JTextField textField) {
        this.calculatorManager = calculatorManager;
        this.textField = textField;
    }

    @Override
    public void execute() throws CalculatorException, NoSuchElementException {
        calculatorManager.calculateResult(textField.getText());
    }
}
