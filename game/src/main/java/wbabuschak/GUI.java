package wbabuschak;

import java.awt.*;
import javax.swing.*;
import java.text.*;

public class GUI {

    String title = "Cupcake Clicker v0.0.4";

    private static int CUPCAKE_GOAL = 1000000;
    private static int SUPERBAKE_COOLDOWN = 50;

    private int superbakeEffects = 10;

    private int cupcakes = 0;
    private int bakeCount = 1;

    private int superbakecd = 0;
    private int superbakecnt = 0;
    private Boolean superbake = false;
    
    private int upgrades = 0;
    private int doublers = 0;

    private int upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
    private int doublerPrice = (int) Math.pow(10, doublers + 1);
    private int upgradeSuperbakePrice = (int) Math.pow(2, superbakeEffects);

    private JLabel cupcakeLabel;
    private JLabel actionLabel;

    private JProgressBar progressBar;
    private NumberFormat commaFormat = NumberFormat.getInstance();

    private JButton bakeButton;
    private JButton doublerButton;
    private JButton upgradeButton;
    private JButton superbakeButton;
    private JButton superbakeUpgradeButton;

    private JFrame backFrame;
    private JPanel mainPanel;

    private Color DEFAULT_BUTTON_COLOR = new Color(192,192,192);
    private static Color ACTIVE_COLOR = new JButton().getBackground();

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
        
        // progressBar
        progressBar = new JProgressBar(0,CUPCAKE_GOAL);
        ToolTipManager.sharedInstance().registerComponent(progressBar);
        updateProgress();

        // bakeButton
        bakeButton = new JButton();
        bakeButton.addActionListener( e -> bakeCupcake());
        bakeButton.setBackground(ACTIVE_COLOR);
        updateBakeButton();

        // superbakeButton
        superbakeButton = new JButton();
        superbakeButton.addActionListener( e -> superbake());
        updateSuperbakeButton();

        // superbakeUpgradeButton
        superbakeUpgradeButton = new JButton();
        superbakeUpgradeButton.addActionListener( e -> upgradeSuperbake());
        updateUpgradeSuperbakeButton();
        
        // actionLabel
        actionLabel = new JLabel();
        
        // upgradeButton
        upgradeButton = new JButton();
        upgradeButton.addActionListener( e -> upgradeIngredients());
        updateUpgradeButton();

        // doublerButton
        doublerButton = new JButton();
        doublerButton.setPreferredSize(new Dimension(250, 40));
        doublerButton.addActionListener( e -> buyDoubler());
        updateDoublerButton();

        // add elements
        mainPanel.add(cupcakeLabel);
        mainPanel.add(progressBar);
        mainPanel.add(bakeButton);
        mainPanel.add(actionLabel);
        mainPanel.add(superbakeButton);
        mainPanel.add(upgradeButton);
        mainPanel.add(superbakeUpgradeButton);
        mainPanel.add(doublerButton);
        

        // backFrame
        backFrame.add(mainPanel, BorderLayout.CENTER);
        backFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backFrame.setTitle(title);
        backFrame.pack();
        backFrame.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("thumbnail.png"));
        Image image = icon.getImage();
        backFrame.setIconImage(image);
        backFrame.setVisible(true);
    }

    private void update() {
        upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
        doublerPrice = (int) Math.pow(10, doublers + 1);
        upgradeSuperbakePrice = (int) Math.pow(2, superbakeEffects);
       
        updateCupcakeLabel();
        updateBakeButton();
        updateUpgradeButton();
        updateDoublerButton();
        updateSuperbakeButton();
        updateUpgradeSuperbakeButton();
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
        activateButton(cupcakes >= upgradePrice,upgradeButton);
    }
    private void updateDoublerButton(){
        doublerButton.setToolTipText("Price: " + doublerPrice + " = 10 ^ (doublers {" + doublers + "} + 1)");
        doublerButton.setText("Buy Doubler! (" + doublerPrice + ")");
        activateButton(cupcakes >= doublerPrice,doublerButton);
    }
    private void updateSuperbakeButton(){
        superbakeButton.setToolTipText("Cooldown: " + SUPERBAKE_COOLDOWN + " clicks, Effect: " + superbakeEffects + " clicks");
        if (superbake){
            superbakeButton.setText("SUPERBAKE! (" + (superbakeEffects - superbakecnt) + ")");
        } else {
            superbakeButton.setText("Activate Superbake! (" + Math.max(0, SUPERBAKE_COOLDOWN - superbakecd) + ")");
            superbakeButton.setBackground(DEFAULT_BUTTON_COLOR);
        }
        activateButton(superbakecd >= SUPERBAKE_COOLDOWN, superbakeButton);
        if (superbake){
            superbakeButton.setBackground(new Color(212,175,55));
        }
    }
    private void updateUpgradeSuperbakeButton(){
        superbakeUpgradeButton.setToolTipText("Price: " + upgradeSuperbakePrice + " = 2 ^ (superbake effects {" + superbakeEffects + "})");
        superbakeUpgradeButton.setText("Buy Superbake Upgrade! (" + upgradeSuperbakePrice + ")");
        activateButton(cupcakes >= upgradeSuperbakePrice,superbakeUpgradeButton);
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
                mainPanel.setBackground(new Color(255,139,148));
                break;
            case 1:
                // orange
                mainPanel.setBackground(new Color(255,223,186));
                break;
            case 2:
                // yellow
                mainPanel.setBackground(new Color(255,255,186));  
                break;
            case 3:
                // green
                mainPanel.setBackground(new Color(186,255,201));  
                break;
            case 4:
                // blue
                mainPanel.setBackground(new Color(186,225,255));  
                break;
            case 5:
                // pink
                mainPanel.setBackground(new Color(255,170,165));  
                break;    
            default:
                // red
                mainPanel.setBackground(new Color(255,139,148));
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
        if (superbake){
            cupcakes += 2 * bakeCount * (int) Math.pow(2, doublers);
            superbakecnt++;
            actionLabel.setText("Baked " + 2 * bakeCount * (int) Math.pow(2, doublers) + " cupcakes!");
        } else {
            cupcakes += bakeCount * (int) Math.pow(2, doublers);
            superbakecd++;
            if ((int) Math.pow(bakeCount, doublers) > 1){
                actionLabel.setText("Baked " + bakeCount * (int) Math.pow(2, doublers) + " cupcakes!");
            } else {
                actionLabel.setText("Baked " + bakeCount * (int) Math.pow(2, doublers) + " cupcake!");
            }
        }
        if (superbakecnt >= superbakeEffects){
            superbake = false;
            superbakecnt = 0;
            superbakecd = 0;
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

    private void upgradeSuperbake(){
        if (cupcakes >= upgradeSuperbakePrice){
            cupcakes -= upgradeSuperbakePrice;
            superbakeEffects++;
            actionLabel.setText("Superbake upgrade purchased! -" + upgradeSuperbakePrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase Superbake upgrade.");
        }
        update();
    }

    private void superbake() {
        if (superbakecd >= SUPERBAKE_COOLDOWN){
            superbake = true;
            superbakecd = 0;
        }
        update();
    }

    private void activateButton(Boolean truth, JButton button){
        if (truth){
            button.setBackground(ACTIVE_COLOR);
        } else {
            button.setBackground(DEFAULT_BUTTON_COLOR);
        }
    }

    private void activateButton(Boolean truth, JButton button, Color activeColor){
        if (truth){
            button.setBackground(activeColor);
        } else {
            button.setBackground(DEFAULT_BUTTON_COLOR);
        }
    }

    
}
