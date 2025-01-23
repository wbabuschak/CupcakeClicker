package wbabuschak;

import java.awt.*;
import javax.swing.*;
import java.text.*;

public class GUI {

    String title = "Cupcake Clicker v0.0.7";

    private static int CUPCAKE_GOAL = 1000000;
    private static int SUPERBAKE_COOLDOWN = 50;
    private static int AUTO_SUPER_COST = 7500;
    private static int AUTOBAKER_SUPER_COST = 15000;
    private static int TICK_RATE = 100;

    private Timer gameClock;
    private int tick = 0;
    private int clicks = 0;

    private int superbakeEffects = 10;

    private int cupcakes = 0;
    private int bakeCount = 1;

    private int superbakecd = 0;
    private int superbakecnt = 0;
    private Boolean superbake = false;

    private Boolean autoSuper = false;
    private Boolean autobakerSuper = false;
    
    private int upgrades = 0;
    private int doublers = 0;
    private int autobakers = 0;

    private int upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
    private int doublerPrice = (int) Math.pow(10, doublers + 1);
    private int upgradeSuperbakePrice = (int) Math.pow(2, superbakeEffects);
    private int autobakerPrice = 100 * (int) Math.pow(12, autobakers + 1);

    private JLabel cupcakeLabel;
    private JLabel actionLabel;

    private JProgressBar progressBar;
    private NumberFormat commaFormat = NumberFormat.getInstance();

    private JButton bakeButton;
    private JButton doublerButton;
    private JButton upgradeButton;
    private JButton superbakeButton;
    private JButton superbakeUpgradeButton;
    private JButton autoSuperButton;
    private JButton winButton;
    private JButton autobakerButton;
    private JButton autobakerSuperbakeButton;
    private JButton statsButton;

    private JTextArea statsArea;
    private Boolean statsVisible = false;
    private String statsString;

    private JFrame backFrame;
    private JPanel mainPanel;
    private JFrame statsFrame;

    private static Color DEFAULT_BUTTON_COLOR = new Color(192,192,192);
    private static Color ACTIVE_COLOR = new JButton().getBackground();
    private static Color PURCHASED_COLOR = new Color(176,141,87);

    public GUI() {
        gameClock = new Timer(TICK_RATE, e -> clockUpdate());

        backFrame = new JFrame();
        mainPanel = new JPanel();
        updateBackground();

        // mainPanel layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        mainPanel.setLayout(new GridBagLayout());
        
        // cupcakeLabel
        cupcakeLabel = new JLabel();
        cupcakeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cupcakeLabel.setVerticalAlignment(SwingConstants.CENTER);
        setSize(cupcakeLabel,0);
        updateCupcakeLabel();
        
        // progressBar
        progressBar = new JProgressBar(0,CUPCAKE_GOAL);
        ToolTipManager.sharedInstance().registerComponent(progressBar);
        setSize(progressBar, 0);
        updateProgress();

        // bakeButton
        bakeButton = new JButton();
        bakeButton.addActionListener( e -> bakeCupcake());
        bakeButton.setBackground(ACTIVE_COLOR);
        setSize(bakeButton, 0);
        updateBakeButton();

        // superbakeButton
        superbakeButton = new JButton();
        superbakeButton.addActionListener( e -> superbake());
        setSize(superbakeButton, 0);
        updateSuperbakeButton();
        
        // superbakeUpgradeButton
        superbakeUpgradeButton = new JButton();
        superbakeUpgradeButton.addActionListener( e -> upgradeSuperbake());
        setSize(superbakeUpgradeButton, 1);
        updateUpgradeSuperbakeButton();
        
        // actionLabel
        actionLabel = new JLabel();
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionLabel.setVerticalAlignment(SwingConstants.CENTER);
        setSize(actionLabel, 1);
        
        // upgradeButton
        upgradeButton = new JButton();
        upgradeButton.addActionListener( e -> upgradeIngredients());
        setSize(upgradeButton, 1);
        updateUpgradeButton();

        // doublerButton
        doublerButton = new JButton();
        doublerButton.addActionListener( e -> buyDoubler());
        setSize(doublerButton, 1);
        updateDoublerButton();

        // autoSuperButton
        autoSuperButton = new JButton();
        autoSuperButton.addActionListener( e -> buyAutoSuper());
        setSize(autoSuperButton, 1);
        updateAutoSuperButton();

        // autobakerButton
        autobakerButton = new JButton();
        autobakerButton.addActionListener( e -> buyautobaker());
        setSize(autobakerButton, 0);
        updateautobakerButton();

        // autobakerSuperbakeButton
        autobakerSuperbakeButton = new JButton();
        autobakerSuperbakeButton.addActionListener( e -> buyautobakerSuperbake());
        setSize(autobakerSuperbakeButton, 1);
        updateAutobakerSuperbakeButton();

        // winButton
        winButton = new JButton();
        winButton.addActionListener( e -> checkWin());
        setSize(winButton, 0);
        updateWinButton();

        // statsButton
        statsButton = new JButton("Statistics");
        statsButton.addActionListener( e -> openStatisticsPanel());
        setSize(statsButton, 0);

        // add elements
        addElement(cupcakeLabel, 0, 0);
        addElement(progressBar, 0, 1);
        addElement(actionLabel, 1, 1);
        addElement(winButton, 0, 3);
        addElement(statsButton, 3, 3);

        addElement(bakeButton, 1, 0);
        addElement(superbakeButton, 2, 0);
        addElement(superbakeUpgradeButton, 2, 1);
        addElement(autoSuperButton, 2, 2);

        addElement(upgradeButton, 1, 3);
        addElement(doublerButton, 2, 3);

        addElement(autobakerButton, 3, 0);
        addElement(autobakerSuperbakeButton, 3, 2);
        

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

        gameClock.start();
    }

    private void update() {
        upgradePrice = 10 * (upgrades + 1) * (bakeCount + upgrades);
        doublerPrice = (int) Math.pow(10, doublers + 1);
        upgradeSuperbakePrice = (int) Math.pow(2, superbakeEffects);
        autobakerPrice = 100 * (int) Math.pow(12, autobakers + 1);
        statsString = "Total clicks: " + commaFormat.format(clicks) 
        + "\n\nIngredient Upgrades: " + upgrades 
        + "\nDoublers: " + doublers 
        + "\nAutobakers: " + autobakers 
        + "\n\nAutomatic Superbaking: " + autoSuper
        + "\nAutobaker Superbaking: " + autobakerSuper;
        
       
        updateBakeButton();
        updateUpgradeButton();
        updateDoublerButton();
        updateSuperbakeButton();
        updateUpgradeSuperbakeButton();
        updateAutoSuperButton();
        updateWinButton();
        updateautobakerButton();
        updateAutobakerSuperbakeButton();
        updateStatsArea();
        activateButton(!statsVisible,statsButton);

        updateCupcakeLabel();
        updateProgress();
        updateBackground();
    }

    private void clockUpdate() {
        tick++;
        // autobakers
        if (tick % 10 == 0){
            for (int i = 0; i < autobakers; i++){
                if (autobakerSuper){
                    if (superbake){
                        cupcakes += 2 * bakeCount * (int) Math.pow(2, doublers);
                        superbakecnt++;
                    } else {
                        cupcakes += bakeCount * (int) Math.pow(2, doublers);
                        superbakecd++;
                    }
                    if (superbakecnt >= superbakeEffects){
                        superbake = false;
                        superbakecnt = 0;
                        superbakecd = 0;
                    }
                    if (autoSuper){
                        superbake();
                    }
                } else {
                    cupcakes += bakeCount * (int) Math.pow(2, doublers);
                }
            }
        }
        update();
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
        doublerButton.setToolTipText("Price: " + doublerPrice + " = 100 ^ (doublers {" + doublers + "} + 1)");
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
    private void updateAutoSuperButton(){
        superbakeUpgradeButton.setToolTipText("Automatically activates Superbake whenever available. No clicking necessary!");
        activateButton(cupcakes >= AUTO_SUPER_COST && autoSuper == false, autoSuperButton);
        if (autoSuper){
            autoSuperButton.setText("Automatically Superbaking!");
            autoSuperButton.setBackground(PURCHASED_COLOR);
        } else {
            autoSuperButton.setText("Puchase Automatic Superbaker: (" + AUTO_SUPER_COST + ")");
        }
    }
    private void updateAutobakerSuperbakeButton(){
        autobakerSuperbakeButton.setToolTipText("Allows your autobakers to utilize Superbake");
        activateButton(cupcakes >= AUTOBAKER_SUPER_COST && autobakerSuper == false, autobakerSuperbakeButton);
        if (autobakerSuper){
            autobakerSuperbakeButton.setText("Clickers Superbaking!");
            autobakerSuperbakeButton.setBackground(PURCHASED_COLOR);
        } else {
            autobakerSuperbakeButton.setText("Puchase Clicker Superbaking: (" + AUTOBAKER_SUPER_COST + ")");
        }
    }
    private void updateWinButton(){
        winButton.setToolTipText("Goal: " + commaFormat.format(CUPCAKE_GOAL));
        winButton.setText("Win! (closes game)");
        activateButton(cupcakes >= CUPCAKE_GOAL, winButton);
    }
    private void updateautobakerButton(){
        autobakerButton.setToolTipText("Price: " + autobakerPrice + " = 100 * 12 ^ (autobakers {" + autobakers + "} + 1)");
        autobakerButton.setText("Buy Autobaker! (" + autobakerPrice + ")");
        activateButton(cupcakes >= autobakerPrice,autobakerButton);
    }
    private void updateProgress(){
        progressBar.setToolTipText("Cupcakes Baked: " + progressBar.getValue() + " / " + commaFormat.format(CUPCAKE_GOAL));
        progressBar.setValue(cupcakes);
    }
    private void updateCupcakeLabel(){
        cupcakeLabel.setText("Cupcakes: " + commaFormat.format(cupcakes));
    }
    private void updateBackground(){
        mainPanel.setBackground(new Color(255, 225 - (int) (95 * Math.min(1,(Math.log10(cupcakes + 1)/Math.log10(CUPCAKE_GOAL)))), 225 - (int) (95 * Math.min(1,(Math.log10(cupcakes + 1)/Math.log10(CUPCAKE_GOAL))))));
    }
    private void updateStatsArea(){
        if (statsVisible){
            statsArea.setText(statsString);
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
        clicks++;
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

        // end superbake
        if (superbakecnt >= superbakeEffects){
            superbake = false;
            superbakecnt = 0;
            superbakecd = 0;
        }
        
        if (autoSuper){
            superbake();
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

    private void buyDoubler() {
        if (cupcakes >= doublerPrice){
            cupcakes -= doublerPrice;
            doublers++;
            actionLabel.setText("Doubler purchased! -" + doublerPrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase Doubler.");
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
            actionLabel.setText("Insufficient funds to purchase Upgrade.");
        }
        update();
    }
    private void upgradeSuperbake(){
        if (cupcakes >= upgradeSuperbakePrice){
            cupcakes -= upgradeSuperbakePrice;
            superbakeEffects++;
            actionLabel.setText("Superbake upgrade purchased! -" + upgradeSuperbakePrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase Superbake Upgrade.");
        }
        update();
    }
    private void buyAutoSuper(){
        if (cupcakes >= AUTO_SUPER_COST){
            cupcakes -= AUTO_SUPER_COST;
            autoSuper = true;
            actionLabel.setText("Upgrade purchased! -" + AUTO_SUPER_COST + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase Automatic Superbaker.");
        }
        update();
    }
    private void buyautobakerSuperbake() {
        if (cupcakes >= AUTOBAKER_SUPER_COST){
            cupcakes -= AUTOBAKER_SUPER_COST;
            autobakerSuper = true;
            actionLabel.setText("Upgrade purchased! -" + AUTOBAKER_SUPER_COST + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase autobaker Superbaking.");
        }
        update();
    }
    private void buyautobaker() {
        if (cupcakes >= autobakerPrice){
            cupcakes -= autobakerPrice;
            autobakers++;
            actionLabel.setText("autobaker purchased! -" + autobakerPrice + " cupcakes!");
        } else {
            actionLabel.setText("Insufficient funds to purchase autobaker.");
        }
        update();
    }

    private void openStatisticsPanel(){
        if (statsVisible){
            return;
        }
        statsVisible = true;
        statsFrame = new JFrame("Statistics");
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        JButton closeStatsButton = new JButton("Close Statistics");
        closeStatsButton.setPreferredSize(new Dimension(300, 50));
        closeStatsButton.setMaximumSize(new Dimension(300, 50));
        closeStatsButton.addActionListener (e -> closeStats());
        activateButton(true, closeStatsButton);
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setOpaque(false);
        statsPanel.add(statsArea);
        statsPanel.add(closeStatsButton);
        statsFrame.add(statsPanel);
        updateStatsArea();
        statsFrame.setSize(300,300);
        statsFrame.setLocation(backFrame.getLocationOnScreen());
        statsFrame.setLocation(statsFrame.getX() + backFrame.getWidth(), statsFrame.getY());
        statsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statsFrame.setVisible(true);
        update();
    }

    private void addElement(JComponent component, int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(component,gbc);
    }
    private void setSize(JComponent component, int size){
        switch (size){
            case 0:
                component.setPreferredSize(new Dimension(350, 75));
                component.setMinimumSize(new Dimension(250, 50));
                break;
            case 1:
                component.setPreferredSize(new Dimension(350, 50));
                component.setMinimumSize(new Dimension(250, 40));
                break;
            default: 
                component.setPreferredSize(new Dimension(350, 60));
                component.setMinimumSize(new Dimension(250, 50));
                break;
        }
    }
    private void closeStats(){
        statsFrame.dispose();
        statsVisible = false;
        update();
    }

    private void activateButton(Boolean truth, JButton button){
        if (truth){
            button.setBackground(ACTIVE_COLOR);
        } else {
            button.setBackground(DEFAULT_BUTTON_COLOR);
        }
    }

    // // alternate activateButton color
    // private void activateButton(Boolean truth, JButton button, Color activeColor){
    //     if (truth){
    //         button.setBackground(activeColor);
    //     } else {
    //         button.setBackground(DEFAULT_BUTTON_COLOR);
    //     }
    // } 
    
}
