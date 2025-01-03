package wbabuschak;

import java.awt.*;
import javax.swing.*;
import java.text.*;

public class GUI {

    String title = "Cupcake Clicker v0.0.3";

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
    private NumberFormat commaFormat = NumberFormat.getInstance();

    private JButton bakeButton;
    private JButton doublerButton;
    private JButton upgradeButton;

    private JFrame backFrame;
    private JPanel mainPanel;

    public GUI() {
        backFrame = new JFrame();
        mainPanel = new JPanel();
        updateBackground();

        // mainPanel layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10)); 
        mainPanel.setLayout(new GridLayout(0,2,10,10));

        // cupcakeLabel
        cupcakeLabel = new JLabel();
        updateCupcakeLabel();
        mainPanel.add(cupcakeLabel);
        
        // progressBar
        progressBar = new JProgressBar(0,CUPCAKE_GOAL);
        ToolTipManager.sharedInstance().registerComponent(progressBar);
        updateProgress();
        mainPanel.add(progressBar);

        // bake button
        bakeButton = new JButton();
        bakeButton.addActionListener( e -> bakeCupcake());
        updateBakeButton();
        mainPanel.add(bakeButton);

        // actionLabel
        actionLabel = new JLabel();
        mainPanel.add(actionLabel);

        // upgradeButton
        upgradeButton = new JButton();
        upgradeButton.addActionListener( e -> upgradeIngredients());
        updateUpgradeButton();
        mainPanel.add(upgradeButton);

        // doublerButton
        doublerButton = new JButton();
        doublerButton.setPreferredSize(new Dimension(250, 40));
        doublerButton.addActionListener( e -> buyDoubler());
        updateDoublerButton();
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
        upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
        doublerPrice = (int) Math.pow(10, doublers + 1);
       
        updateCupcakeLabel();
        updateBakeButton();
        updateUpgradeButton();
        updateDoublerButton();
        updateProgress();
        updateBackground();

        checkWin();
    }

    private void updateBakeButton(){
        bakeButton.setToolTipText("Bake value (" + bakeCount + ") doubled " + doublers + " times = " + bakeCount * (int) Math.pow(2, doublers));
        if (bakeCount * (int) Math.pow(2, doublers) > 1){
            bakeButton.setText("Bake " + bakeCount * (int) Math.pow(2, doublers) + " cupcakes");
        } else {
            bakeButton.setText("Bake " + bakeCount * (int) Math.pow(2, doublers) + " cupcake");
        }
    }
    private void updateUpgradeButton(){
        upgradeButton.setToolTipText("Price: " + upgradePrice + " = 10 * (upgrades {" + upgrades + "} + 1) * (bake value + {" + bakeCount + "} + upgrades{" + upgrades + "})");
        upgradeButton.setText("Upgrade Ingredients! (" + upgradePrice + ")");
    }
    private void updateDoublerButton(){
        doublerButton.setToolTipText("Price: " + doublerPrice + " = 10 ^ (doublers {" + doublers + "} + 1)");
        doublerButton.setText("Buy Doubler! (" + doublerPrice + ")");
    }
    private void updateProgress(){
        progressBar.setToolTipText("Cupcakes Baked: " + progressBar.getValue() + " / " + commaFormat.format(CUPCAKE_GOAL));
        progressBar.setValue(cupcakes);
    }
    private void updateCupcakeLabel(){
        cupcakeLabel.setText("Cupcakes: " + commaFormat.format(cupcakes));
    }
    private void updateBackground(){
        int colorCode = (int) Math.floor(Math.log10(cupcakes));
        switch (colorCode) {
            case 0:
                // red
                mainPanel.setBackground(new Color(255,40,0));
                break;
            case 1:
                // yellow-orange
                mainPanel.setBackground(new Color(255,220,0));
                break;
            case 2:
                // pale green
                mainPanel.setBackground(new Color(175,255,175));  
                break;
            case 3:
                // cyan-aqua
                mainPanel.setBackground(new Color(0,245,245));  
                break;
            case 4:
                // periwinkle
                mainPanel.setBackground(new Color(185,155,255));  
                break;
            case 5:
                // pink
                mainPanel.setBackground(new Color(255,175,175));  
                break;    
            default:
                mainPanel.setBackground(Color.LIGHT_GRAY);
                break;
        }
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
