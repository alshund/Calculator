package view;

import controller.CalculatorManager;
import exceptions.CalculatorException;
import managers.Observer;
import model.graph.Node;
import strategy.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by shund on 05.06.2017.
 */
public class MainWindow implements Observer {
    public final int MAIN_FRAME_WIDTH = 500;
    public final int MAIN_FRAME_HEIGHT = 500;
    public final int INPUT_FIELD_HEIGHT = 40;
    public final int RESULT_FIELD_HEIGHT = 40;

    private JFrame frame;
    private JTextField inputField;
    private JTextField resultField;
    private JTree tree;
    private JPanel specialPanel;

    private CalculatorManager calculatorManager;

    public MainWindow(CalculatorManager calculatorManager) {
        calculatorManager.getCalculatorModel().setObserver(this);
        this.calculatorManager = calculatorManager;
        createMainWindow();
        fillMainWindow(createResultPanel(), createCalculatorPanel());
    }

    @Override
    public void update(Node currentNode, int depth) {
        try {
            resultField.setText(String.valueOf(currentNode.getResult()));
            buildTree(currentNode, depth);
        } catch (CalculatorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setInputText(String inputText) {
        inputField.setText(inputText);
    }

    @Override
    public void removeData() {
        clearOutText();
        clearOutTree();
    }

    private void clearOutText() {
        resultField.setText("");
        inputField.setText("");
    }

    private void clearOutTree() {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode viewNode = (DefaultMutableTreeNode) treeModel.getRoot();
        viewNode.setUserObject("Дерево разбора");
        viewNode.removeAllChildren();
        treeModel.reload(viewNode);
    }

    private void createMainWindow() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        frame.setLayout(new GridBagLayout());
    }

    private JPanel createCalculatorPanel() {
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout());
        calculatorPanel.add(createInputFiled(), BorderLayout.NORTH);
        calculatorPanel.add(createProcessingPanel(), BorderLayout.CENTER);
        return calculatorPanel;
    }

    private JTextField createInputFiled() {
        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setPreferredSize(new Dimension(inputField.getWidth(), INPUT_FIELD_HEIGHT));
        return inputField;
    }

    private JPanel createProcessingPanel() {
        specialPanel = new JPanel();
        specialPanel.setLayout(new GridBagLayout());
        List<JButton> mainButtonList = createMainButtonList();
        List<JButton> specialButtonList = createSpecialButtonList();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        int buttonListIndex = 0, buttonPositionIndex = 0;
        for (JButton button : mainButtonList) {
            if (buttonListIndex % 5 == 0) {
                buttonPositionIndex++;
            }
            mainPanel.add(button, createGridBagLayout(GridBagConstraints.RELATIVE, buttonPositionIndex, 1, 1));
            buttonListIndex++;
        }

        for (JButton button : specialButtonList) {
            specialPanel.add(button, createGridBagLayout(0, GridBagConstraints.RELATIVE, 1, 1));
        }
        specialPanel.setVisible(false);
        JPanel processingPanel = new JPanel();
        processingPanel.setLayout(new BorderLayout());
        processingPanel.add(mainPanel, BorderLayout.CENTER);
        processingPanel.add(specialPanel, BorderLayout.EAST);
        return processingPanel;
    }

    private List<JButton> createMainButtonList() {
        List<JButton> buttonList = new ArrayList<JButton>();
        buttonList.add(createButton("<", new MinimizeButton(calculatorManager)));
        buttonList.add(createButton(">", new ExpandButton(calculatorManager)));
        buttonList.add(createButton("C", new RemoveButton(calculatorManager)));
        buttonList.add(createButton("(", new InputButton(inputField, "(")));
        buttonList.add(createButton(")", new InputButton(inputField, ")")));
        buttonList.add(createButton("7", new InputButton(inputField, "7")));
        buttonList.add(createButton("8", new InputButton(inputField, "8")));
        buttonList.add(createButton("9", new InputButton(inputField, "9")));
        buttonList.add(createButton("+", new InputButton(inputField, "+")));
        buttonList.add(createButton("-", new InputButton(inputField, "-")));
        buttonList.add(createButton("4", new InputButton(inputField, "4")));
        buttonList.add(createButton("5", new InputButton(inputField, "5")));
        buttonList.add(createButton("6", new InputButton(inputField, "6")));
        buttonList.add(createButton("*", new InputButton(inputField, "*")));
        buttonList.add(createButton("/", new InputButton(inputField, "/")));
        buttonList.add(createButton("1", new InputButton(inputField, "1")));
        buttonList.add(createButton("2", new InputButton(inputField, "2")));
        buttonList.add(createButton("3", new InputButton(inputField, "3")));
        buttonList.add(createButton("%", new InputButton(inputField, "%")));
        buttonList.add(createButton("1/x", new InputButton(inputField, "1/")));
        buttonList.add(createButton(".", new InputButton(inputField, ".")));
        buttonList.add(createButton("0", new InputButton(inputField, "0")));
        buttonList.add(createButton("=", new SubmitButton(calculatorManager, inputField)));
        buttonList.add(createButton("sqrt()", new InputButton(inputField, "sqrt()")));
        buttonList.add(createButton("trig", new TrigonometryButton(specialPanel)));
        return buttonList;
    }

    private List<JButton> createSpecialButtonList() {
        List<JButton> specialButtonList = new ArrayList<JButton>();
        specialButtonList.add(createButton("sin()", new InputButton(inputField, "sin()")));
        specialButtonList.add(createButton("cos()", new InputButton(inputField, "cos()")));
        specialButtonList.add(createButton("ctg()", new InputButton(inputField, "ctg()")));
        specialButtonList.add(createButton("tg()", new InputButton(inputField, "tg()")));
        specialButtonList.add(createButton("pi", new InputButton(inputField, String.valueOf(Math.PI))));
        return specialButtonList;
    }

    private JButton createButton(String buttonName, ButtonStrategy buttonStrategy) {
        JButton button = new JButton(buttonName);
        button.addActionListener(e->{
            try {
                buttonStrategy.execute();
            } catch (CalculatorException e1) {
                JOptionPane.showMessageDialog(getFrame(), e1.getMessage());
            } catch (NullPointerException e2) {
                JOptionPane.showMessageDialog(getFrame(), "Данные не обработаны!");
            } catch (NoSuchElementException e3) {
                JOptionPane.showMessageDialog(getFrame(), "Поле ввода не заполнено!");
            }
        });
        return button;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(createResultField(), BorderLayout.NORTH);
        resultPanel.add(createTreePanel(), BorderLayout.CENTER);
        return resultPanel;
    }

    private JTextField createResultField() {
        resultField = new JTextField();
        resultField.setEnabled(false);
        resultField.setPreferredSize(new Dimension(resultField.getWidth(), RESULT_FIELD_HEIGHT));
        return resultField;
    }

    private JScrollPane createTreePanel() {
        JScrollPane scrollPane = new JScrollPane(createTree());
        return scrollPane;
    }

    private JTree createTree() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Дерево разбора");
        tree = new JTree(node);
        return tree;
    }


    private void fillMainWindow(JPanel resultPanel, JPanel calculatorPanel) {
        frame.add(resultPanel, createGridBagLayout(0, 0, 1, 1));
        frame.add(calculatorPanel, createGridBagLayout(1, 0, 2, 1));
        frame.setVisible(true);
    }

    private void buildTree(Node calculatorNode, int depth) {
        clearOutTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode viewNode = (DefaultMutableTreeNode) treeModel.getRoot();
        viewNode.setUserObject(calculatorNode.getName());
        DFS(viewNode, calculatorNode, depth);
        treeModel.reload(viewNode);
        expandTree();
    }

    private void expandTree() {
        for (int rowIndex = 0; rowIndex < tree.getRowCount(); rowIndex++) {
            tree.expandRow(rowIndex);
        }
    }

    private void DFS(DefaultMutableTreeNode viewNode, Node calculatorNode, int depth) {
        List<Node> domainList = calculatorNode.getDomainList();
        for (Node domain : domainList) {
            if (depth >= domain.getDepth()) {
                DefaultMutableTreeNode newViewNode = new DefaultMutableTreeNode(domain.getName());
                viewNode.add(newViewNode);
                DFS(newViewNode, domain, depth);
            } else {
                try {
                    viewNode.setUserObject(calculatorNode.getResult());
                } catch (CalculatorException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private GridBagConstraints createGridBagLayout(int gridX, int gridY, int gridWidth, int gridHeight) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridX;
        gridBagConstraints.gridy = gridY;
        gridBagConstraints.gridwidth = gridWidth;
        gridBagConstraints.gridheight = gridHeight;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        return gridBagConstraints;
    }

    public JFrame getFrame() {
        return frame;
    }
}
