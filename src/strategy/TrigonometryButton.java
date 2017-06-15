package strategy;

import javax.swing.*;

/**
 * Created by shund on 15.06.2017.
 */
public class TrigonometryButton implements ButtonStrategy {
    private JPanel trigonometryPanel;

    public TrigonometryButton(JPanel trigonometryPanel) {
        this.trigonometryPanel = trigonometryPanel;
    }

    @Override
    public void execute() {
        if (trigonometryPanel.isVisible()) {
            trigonometryPanel.setVisible(false);
        } else {
            trigonometryPanel.setVisible(true);
        }
    }
}
