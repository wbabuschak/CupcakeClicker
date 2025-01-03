package wbabuschak;

import java.awt.*;
import javax.swing.*;

public class GUI {

    String title = "Cupcake Clicker v0.0.2";

    private int CUPCAKE_GOAL = 1000000;
    private int cupcakes = 0;
    private int bakeCount = 1;
    
    private int upgrades = 0;
    private int doublers = 0;

    private int upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
    private int doublerPrice = (int) Math.pow(10, doublers + 1);

    private JLabel cupcakeLabel;
    private JLabel actionLabel;

    private JProgressBar progressBar;

    private JButton bakeButton;
    private JButton doublerButton;
    private JButton upgradeButton;

    private JFrame backFrame;

    public GUI() {
        backFrame = new JFrame();
        JPanel mainPanel = new JPanel();
        
        // progressBar
        progressBar = new JProgressBar(0,CUPCAKE_GOAL);
        ToolTipManager.sharedInstance().registerComponent(progressBar);
        progressBar.setToolTipText("Cookies Baked: " + progressBar.getValue() + " / " + CUPCAKE_GOAL);
        mainPanel.add(progressBar);

        // mainPanel layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5)); 
        mainPanel.setLayout(new GridLayout(0, 1, 10, 10));

        // cupcakeLabel
        cupcakeLabel = new JLabel("Cupcakes: " + cupcakes);
        mainPanel.add(cupcakeLabel);

        // actionLabel
        actionLabel = new JLabel();
        mainPanel.add(actionLabel);

        // buttons
        bakeButton = new JButton("Bake Cupcake");
        bakeButton.addActionListener( e -> bakeCupcake());
        bakeButton.setToolTipText("Bake value doubled " + doublers + " time(s) = " + (int) Math.pow(bakeCount, doublers));
        mainPanel.add(bakeButton);

        upgradeButton = new JButton("Upgrade Ingredients!");
        upgradeButton.addActionListener( e -> upgradeIngredients());
        upgradeButton.setToolTipText("Price: " + upgradePrice);
        mainPanel.add(upgradeButton);

        doublerButton = new JButton("Buy Doubler!");
        doublerButton.addActionListener( e -> buyDoubler());
        doublerButton.setToolTipText("Price: " + doublerPrice);
        mainPanel.add(doublerButton);
        

        // backFrame
        backFrame.add(mainPanel, BorderLayout.CENTER);
        backFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backFrame.setTitle(title);
        backFrame.pack();
        backFrame.setLocationRelativeTo(null);
        backFrame.setVisible(true);
    }

    private void update() {
        cupcakeLabel.setText("Cupcakes: " + cupcakes);
        progressBar.setValue(cupcakes);
        progressBar.setToolTipText("Cookies Baked: " + progressBar.getValue() + " / " + CUPCAKE_GOAL);

        upgradePrice = 10 * (upgrades + 1) * upgrades;
        doublerPrice = (int) Math.pow(10, doublers + 1);

        doublerButton.setToolTipText("Price: " + doublerPrice);
        bakeButton.setToolTipText("Bake value (" + bakeCount + ") doubled " + doublers + " times = " + bakeCount * (int) Math.pow(2, doublers));
        upgradeButton.setToolTipText("Price: " + upgradePrice);
        checkWin();
    }

    private void checkWin(){
        if (cupcakes >= CUPCAKE_GOAL){
            JFrame winFrame = new JFrame("You win!");
            JPanel winPanel = new JPanel(new BorderLayout());
            winPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            ImageIcon winImage = new ImageIcon(getClass().getClassLoader().getResource("winner.jpg"));
            JLabel imageLabel  = new JLabel(winImage);

            winPanel.add(imageLabel);
            winFrame.add(winPanel);

            winFrame.setSize(512, 512);
            winFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            winFrame.setResizable(false);
            winFrame.setLocationRelativeTo(null);
            winFrame.setVisible(true);
            backFrame.dispose();
        }
    }

    private void bakeCupcake() {
        cupcakes += bakeCount * (int) Math.pow(2, doublers);
        if ((int) Math.pow(bakeCount, doublers) > 1){
            actionLabel.setText("Baked " + bakeCount * (int) Math.pow(2, doublers) + " cupcakes!");
        } else {
            actionLabel.setText("Baked " + bakeCount * (int) Math.pow(2, doublers) + " cupcake!");
        }
        update();
    }

    private void buyDoubler() {
        if (cupcakes >= doublerPrice){
            cupcakes -= doublerPrice;
            doublers++;
            actionLabel.setText("Doubler purchased! -" + doublerPrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase doubler.");
        }
        update();
    }

    private void upgradeIngredients() {
        if (cupcakes >= upgradePrice){
            cupcakes -= upgradePrice;
            upgrades++;
            bakeCount++;
            actionLabel.setText("Upgrade purchased! -" + upgradePrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase upgrade.");
        }
        update();
    }

    
}
