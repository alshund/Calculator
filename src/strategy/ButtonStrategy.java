package strategy;

import exceptions.CalculatorException;

/**
 * Created by shund on 07.06.2017.
 */
public interface ButtonStrategy {
    void execute() throws CalculatorException;
}
