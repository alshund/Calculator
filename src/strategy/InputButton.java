package strategy;

import javax.swing.*;

/**
 * Created by shund on 07.06.2017.
 */
public class InputButton implements ButtonStrategy {
    private JTextField textField;
    private String data;

    public InputButton(JTextField textField, String data) {
        this.textField = textField;
        this.data = data;
    }

    @Override
    public void execute() {
        StringBuffer stringBuffer = new StringBuffer(textField.getText());
        stringBuffer.insert(textField.getCaretPosition(), data);
        textField.setText(String.valueOf(stringBuffer));
    }
}
