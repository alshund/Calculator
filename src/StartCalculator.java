import controller.CalculatorManager;
import model.CalculatorModel;
import view.MainWindow;

/**
 * Created by shund on 05.06.2017.
 */
public class StartCalculator {
    public static void main(String[] arg) {
        CalculatorModel calculatorModel = new CalculatorModel();
        CalculatorManager calculatorManager = new CalculatorManager(calculatorModel);
        new MainWindow(calculatorManager);
    }
}
