/**
 * Main class for the RPN Calculator project
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;


import static java.awt.GridBagConstraints.*;

/*PROGRAMMER'S NOTE: for most comments, "register x" or position "x" or just "x" is used instead of register 1
 * register "y" or position "y" or just "y" is used instead of register 2
 * 
 */
public class RPNCalc extends JFrame {

    ArrayList<Double> stack = new ArrayList<>(); // stack holds all numbers, isn't actually a stack
    String formatPattern = "0.00####";//default number format pattern
    DecimalFormat formatter = new DecimalFormat(formatPattern);    // this controls how the numbers in the registers are formatted. The format patterns are changed in the FormatWindow class.
    JMenuBar menubar;//this menu bar at the top of the window holds tools, formatting,settings
    JMenu tools; //this JMenu holds number conversion and vector math JMenuItems
    JMenuItem numberConversion, vectorMath, formatting, settings, graphing;//JMenuItems that actually trigger new windows to open
    JTextField register1, register2; // how user accesses stack
    JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bPlus, bMinus, bMult, bDiv,
            bENTER, bSIGN, bDot, bSQRT, bCLR, bPOW, bSIN, bCOS, bTAN, bSwitch, bLN, bLOG, bBACK, bRECIPROCAL, bPI, bSHIFT;// buttons for numbers, operations, enter, exit
    boolean tempVal = false; // this thing checks to see if the number in register 1 should be kicked up on the next number entry
    boolean radians = false; //Whether or not we are working in radians, false by default
    boolean shift = false;    //controls whether or not the shift function is active
    String lookAndFeel;

    public RPNCalc() {
        super("RPN Calculator");//set window title

        stack.add(0.0);
        stack.add(0.0); // fills stack with zeros
        stack.add(0.0);
        stack.add(0.0);

        addComponents();
        lookAndFeel = UIManager.getSystemLookAndFeelClassName();    //sets the default look and feel to the system look
        setLookAndFeel(lookAndFeel);
        repaint();        //repaints so that we can see the system look instead of the default Swing look

    }

    protected void display() {        //this method updates and reformats the contents of each register
        //called from most methods that influence the stack

        register2.setText(String.valueOf(stack.get(0)));
        if (!register1.getText().equals("")) {            //if stack position x is not empty, reformat
            register1.setText(formatter.format(Double.parseDouble(register1.getText())));
        }
        if (!register2.getText().equals("")) {            //if stack position y is not empty, reformat
            register2.setText(formatter.format(Double.parseDouble(register2.getText())));
        }

        if (stack.size() < 4) {
            stack.add(0.0);
            stack.add(0.0); // ghetto error trap, prevents IndexOutOfBounds exception if stack gets too small
            stack.add(0.0);
            stack.add(0.0);
        }
    }

    protected void switchRegister() {            //switches contents of x and y registers and calls display()
        double temp1, temp2;
        temp1 = Double.parseDouble(register1.getText());
        temp2 = stack.get(0);
        register1.setText(String.valueOf(temp2));
        stack.set(0, temp1);
        display();
    }

    protected void ENTER() {    //inserts empty string into position x and kicks whatever was in position x up 1 register

        if (register1.getText().equals("")) {
            stack.add(0, 0.0);
        } else {
            stack.add(0, Double.parseDouble(register1.getText()));
        }
        register1.setText("");
        display();
        tempVal = false;

    }

    protected void add() {            //adds whatever is in positions x and y and puts it in x, calls display()
        double temp;
        temp = stack.remove(0)
                + Double.parseDouble(register1.getText());

        register1.setText(String.valueOf(temp));

        display();
        tempVal = true;
    }

    protected void subtract() {    //same deal as add()
        double temp;
        temp = stack.remove(0)
                - Double.parseDouble(register1.getText());

        register1.setText(String.valueOf(temp));

        display();
        tempVal = true;
    }

    protected void multiply() {    //same deal as add()
        double temp;
        temp = stack.remove(0)
                * Double.parseDouble(register1.getText());

        register1.setText(String.valueOf(temp));

        display();
        tempVal = true;
    }

    protected void divide() {//same deal as add()
        double temp;
        temp = stack.remove(0)
                / Double.parseDouble(register1.getText());

        register1.setText(String.valueOf(temp));

        display();
        tempVal = true;
    }

    protected void sqrt() { //sets the value in register x to its square root
        register1.setText(String.valueOf(Math.sqrt(Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void sine() {    //sets whatever is in register x to its sine, based on the value of var radians. Calls display()
        if (radians) {

            register1.setText(String.valueOf(Math.sin(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf(Math.sin(Math.toRadians(Double.parseDouble(register1.getText())))));

        }
        display();
        tempVal = true;
    }

    protected void cosine() {//same deal as sine()
        if (radians) {
            register1.setText(String.valueOf(Math.cos(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf(Math.cos(Math.toRadians(Double.parseDouble(register1.getText())))));

        }
        display();
        tempVal = true;
    }

    protected void tangent() {//same deal as sine()
        if (radians) {
            register1.setText(String.valueOf(Math.tan(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf(Math.tan(Math.toRadians(Double.parseDouble(register1.getText())))));

        }
        display();
        tempVal = true;
    }

    protected void arcsine() {//same deal as sine()
        if (radians) {
            register1.setText(String.valueOf(Math.asin(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf((180 / Math.PI) * Math.asin(Double.parseDouble(register1.getText()))));

        }
        display();
        tempVal = true;
    }

    protected void arccosine() {//same deal as sine()
        if (radians) {
            register1.setText(String.valueOf(Math.acos(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf((180 / Math.PI) * Math.acos(Double.parseDouble(register1.getText()))));

        }
        display();
        tempVal = true;
    }

    protected void arctangent() {//same deal as sine()
        if (radians) {
            register1.setText(String.valueOf(Math.atan(Double.parseDouble(register1.getText()))));

        } else {
            register1.setText(String.valueOf((180 / Math.PI) * Math.atan(Double.parseDouble(register1.getText()))));

        }
        display();
        tempVal = true;
    }

    protected void exp() {//sets whatever is in position x to e^x, calls display()
        register1.setText(String.valueOf(Math.pow(Math.E, Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void exp10() {//sets whatever is in position x to 10^x, calls display()
        register1.setText(String.valueOf(Math.pow(10, Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void nthroot() {//sets position x equal to the xth root of y, removes y from stack, calls display()
        register1.setText(String.valueOf(Math.pow(stack.remove(0), 1 / Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void power() {//sets position x equal to y^x, calls display()
        register1.setText(String.valueOf(Math.pow(stack.remove(0), Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void ln() {//sets position x equal to ln(x), calls display()
        register1.setText(String.valueOf(Math.log(Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void log10() {//sets position x equal to log10(x), calls display()
        register1.setText(String.valueOf(Math.log10(Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }

    protected void reciprocal() {//sets position x equal to 1/x
        register1.setText(String.valueOf(1 / (Double.parseDouble(register1.getText()))));
        display();
        tempVal = true;
    }


    protected void clear() {//sets position x to empty string
        register1.setText("");
    }

    protected void backspace() {//deletes one character
        if (!register1.getText().equals("")) {
            register1.setText(register1.getText().substring(0, register1.getText().length() - 1));
        }


    }

    protected void flipSign() {//flips sign of whatever is in register x
        if (!register1.getText().equals("")) {


            if (register1.getText().charAt(0) == '-') {
                register1.setText(register1.getText().substring(1));

            } else {
                register1.setText("-" + register1.getText());
            }


        }
    }

    protected void shift() {    //changes labels for buttons with shift functionality, flips value of shift boolean variable

        shift = !shift;
        if (shift) {
            bSIN.setText("ASIN");
            bSIN.setActionCommand("SIN");
            bCOS.setText("ACOS");
            bCOS.setActionCommand("COS");
            bTAN.setText("ATAN");
            bTAN.setActionCommand("TAN");
            bLN.setText("e^X");
            bLN.setActionCommand("LN");
            bLOG.setText("10^X");
            bLOG.setActionCommand("LOG10");
            bSQRT.setText("Nth Rt");
            bSQRT.setActionCommand("SQRT");

        } else {
            bSIN.setText("SIN");
            bSIN.setActionCommand("SIN");
            bCOS.setText("COS");
            bCOS.setActionCommand("COS");
            bTAN.setText("TAN");
            bTAN.setActionCommand("TAN");
            bLN.setText("LN");
            bLN.setActionCommand("LN");
            bLOG.setText("LOG");
            bLOG.setActionCommand("LOG10");
            bSQRT.setText("\u221A");
            bSQRT.setActionCommand("SQRT");


        }
    }

    public void updateFormatPattern(String pattern) {    //changes format pattern, called from format window class
        formatPattern = pattern;
        formatter.applyPattern(formatPattern);
    }

    public void setLookAndFeel(String lookAndFeel) {    //sets look and feel, called from settings window class
        this.lookAndFeel = lookAndFeel;
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception ex) {

        }
    }


    //adds all components to JFrame using GridBagLayout

    public void addComponents() {

        //instantiates buttons with key functionality
        //adds key mappings

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b1 = new JButton("1");
        numberListener numberListener1 = new numberListener("1");
        b1.addActionListener(numberListener1);
        b1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "1");
        b1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
        b1.getActionMap().put("1", numberListener1);

        b2 = new JButton("2");
        numberListener numberListener2 = new numberListener("2");
        b2.addActionListener(numberListener2);
        b2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "2");
        b2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "2");
        b2.getActionMap().put("2", numberListener2);

        b3 = new JButton("3");
        numberListener numberListener3 = new numberListener("3");
        b3.addActionListener(numberListener3);
        b3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "3");
        b3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "3");
        b3.getActionMap().put("3", numberListener3);

        b4 = new JButton("4");
        numberListener numberListener4 = new numberListener("4");
        b4.addActionListener(numberListener4);
        b4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "4");
        b4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "4");
        b4.getActionMap().put("4", numberListener4);

        b5 = new JButton("5");
        numberListener numberListener5 = new numberListener("5");
        b5.addActionListener(numberListener5);
        b5.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "5");
        b5.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), "5");
        b5.getActionMap().put("5", numberListener5);

        b6 = new JButton("6");
        numberListener numberListener6 = new numberListener("6");
        b6.addActionListener(numberListener6);
        b6.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), "6");
        b6.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "6");
        b6.getActionMap().put("6", numberListener6);

        b7 = new JButton("7");
        numberListener numberListener7 = new numberListener("7");
        b7.addActionListener(numberListener7);
        b7.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "7");
        b7.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "7");
        b7.getActionMap().put("7", numberListener7);

        b8 = new JButton("8");
        numberListener numberListener8 = new numberListener("8");
        b8.addActionListener(numberListener8);
        b8.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "8");
        b8.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "8");
        b8.getActionMap().put("8", numberListener8);

        b9 = new JButton("9");
        numberListener numberListener9 = new numberListener("9");
        b9.addActionListener(numberListener9);
        b9.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), "9");
        b9.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "9");
        b9.getActionMap().put("9", numberListener9);

        b0 = new JButton("0");
        numberListener numberListener0 = new numberListener("0");
        b0.addActionListener(numberListener0);
        b0.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "0");
        b0.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "0");
        b0.getActionMap().put("0", numberListener0);

        bDot = new JButton(".");
        numberListener numberListenerDot = new numberListener(".");
        bDot.addActionListener(numberListenerDot);
        bDot.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), ".");
        bDot.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), ".");
        bDot.getActionMap().put(".", numberListenerDot);

        bPI = new JButton("\u03C0");
        bPI.addActionListener(new numberListener("pi"));
        bPI.setActionCommand("pi");       //for some reason it sets the ActionCommand for this button to the symbol for pi without this line.


        bPlus = new JButton("+");
        opListener plusListener = new opListener("+");
        bPlus.addActionListener(plusListener);
        bPlus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "+");
        bPlus.getActionMap().put("+", plusListener);

        bMinus = new JButton("-");
        opListener minusListener = new opListener("-");
        bMinus.addActionListener(minusListener);
        bMinus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "-");
        bMinus.getActionMap().put("-", minusListener);

        bMult = new JButton("\u2715");
        opListener multListener = new opListener("*");
        bMult.addActionListener(multListener);
        bMult.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "*");
        bMult.getActionMap().put("*", multListener);
        bMult.setActionCommand("*");

        bDiv = new JButton("\u00F7");
        opListener divListener = new opListener("/");
        bDiv.addActionListener(divListener);
        bDiv.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0), "/");
        bDiv.getActionMap().put("/", divListener);
        bDiv.setActionCommand("/");

        bBACK = new JButton("\u2190");
        opListener backListener = new opListener("BACKSPACE");
        bBACK.addActionListener(backListener);
        bBACK.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACKSPACE");
        bBACK.getActionMap().put("BACKSPACE", backListener);
        bBACK.setActionCommand("BACKSPACE");

        bENTER = new JButton("ENTER");
        opListener enterListener = new opListener("ENTER");
        bENTER.addActionListener(enterListener);
        bENTER.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
        bENTER.getActionMap().put("ENTER", enterListener);


        //action buttons without keyboard interaction, do not require named ActionListener
        bSQRT = new JButton("\u221a");
        bSQRT.addActionListener(new opListener("SQRT"));
        bSQRT.setActionCommand("SQRT");
        bCLR = new JButton("CLX");
        bCLR.addActionListener(new opListener("CLR"));
        bCLR.setActionCommand("CLR");
        bPOW = new JButton("Y^X");
        bPOW.addActionListener(new opListener("POW"));
        bPOW.setActionCommand("POW");
        bSIN = new JButton("SIN");
        bSIN.addActionListener(new opListener("SIN"));
        bCOS = new JButton("COS");
        bCOS.addActionListener(new opListener("COS"));
        bTAN = new JButton("TAN");
        bTAN.addActionListener(new opListener("TAN"));
        bSwitch = new JButton("X<->Y");
        bSwitch.addActionListener(new opListener("SWITCH"));
        bSwitch.setActionCommand("SWITCH");
        bLN = new JButton("LN");
        bLN.addActionListener(new opListener("LN"));
        bLOG = new JButton("LOG");
        bLOG.addActionListener(new opListener("LOG10"));
        bLOG.setActionCommand("LOG10");
        bRECIPROCAL = new JButton("1/X");
        bRECIPROCAL.addActionListener(new opListener("RECIPROCAL"));
        bRECIPROCAL.setActionCommand("RECIPROCAL");
        bSIGN = new JButton("+/-");
        bSIGN.addActionListener(new opListener("SIGN"));
        bSIGN.setActionCommand("SIGN");

        bSHIFT = new JButton("SHIFT");
        bSHIFT.addActionListener(new opListener("SHIFT"));
        bSHIFT.setActionCommand("SHIFT");

        //instantiates registers, makes sure that user can't put in strange input
        register1 = new JTextField(25);
        register2 = new JTextField(25);
        register1.setEditable(false);
        register2.setEditable(false);

        //menu bar at the top of the window
        menubar = new JMenuBar();

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);


        //Handles menu bar

        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 0;
        menuConstraints.weightx = 0.1;
        menuConstraints.weighty = 0.1;
        menuConstraints.gridwidth = 5;
        menuConstraints.fill = BOTH;
        this.add(menubar, menuConstraints);
        //JMenu for number conversion window and vector math window
        tools = new JMenu("Tools");
        menubar.add(tools);
        menubar.setLayout(new GridLayout(1, 5));
        numberConversion = new JMenuItem("Number Conversion Window");
        numberConversion.addActionListener(new menuListener("numberConversion", this));
        tools.add(numberConversion);

        vectorMath = new JMenuItem("Vector Arithmetic");
        vectorMath.addActionListener(new menuListener("vectorMath", this));
        tools.add(vectorMath);


        //JMenuItem for settings, just sits on the menu bar
        settings = new JMenuItem("Settings");
        settings.addActionListener(new menuListener("config", this));

        menubar.add(settings);

        //JMenuItem for formatting, again just sits on menu bar outside of JMenu
        formatting = new JMenuItem("Formatting");
        formatting.addActionListener(new menuListener("format", this));

        menubar.add(formatting);

        //JMenuItem for formatting, again just sits on menu bar outside of JMenu
        graphing = new JMenuItem("Graphing");
        graphing.addActionListener(new menuListener("graphing", this));

        menubar.add(graphing);

        //add registers to top of window with GridBagLayout

        GridBagConstraints register2Constraints = new GridBagConstraints();
        register2Constraints.gridx = 0;
        register2Constraints.gridy = 1;
        register2Constraints.gridwidth = 5;
        register2Constraints.gridheight = 1;
        register2Constraints.weighty = 0.1;
        register2Constraints.weightx = 0.1;
        register2Constraints.fill = BOTH;
        this.add(register2, register2Constraints);

        GridBagConstraints register1Constraints = new GridBagConstraints();
        register1Constraints.gridx = 0;
        register1Constraints.gridy = 2;
        register1Constraints.gridwidth = 5;
        register1Constraints.gridheight = 1;
        register1Constraints.weighty = 0.1;
        register1Constraints.weightx = 0.1;
        register1Constraints.fill = BOTH;
        this.add(register1, register1Constraints);

        //adds all the buttons to the window with GridBagLayout

        GridBagConstraints reciprocalConstraints = new GridBagConstraints();
        reciprocalConstraints.gridx = 0;
        reciprocalConstraints.gridy = 3;
        reciprocalConstraints.weightx = 0.1;
        reciprocalConstraints.fill = HORIZONTAL;
        this.add(bRECIPROCAL, reciprocalConstraints);

        GridBagConstraints sqrtConstraints = new GridBagConstraints();
        sqrtConstraints.gridx = 1;
        sqrtConstraints.gridy = 3;
        sqrtConstraints.weightx = 0.1;
        sqrtConstraints.fill = HORIZONTAL;
        this.add(bSQRT, sqrtConstraints);

        GridBagConstraints powConstraints = new GridBagConstraints();
        powConstraints.gridx = 2;
        powConstraints.gridy = 3;
        powConstraints.weightx = 0.1;
        powConstraints.fill = HORIZONTAL;
        this.add(bPOW, powConstraints);

        GridBagConstraints lnConstraints = new GridBagConstraints();
        lnConstraints.gridx = 3;
        lnConstraints.gridy = 3;
        lnConstraints.weightx = 0.1;
        lnConstraints.fill = HORIZONTAL;
        this.add(bLN, lnConstraints);

        GridBagConstraints logConstraints = new GridBagConstraints();
        logConstraints.gridx = 4;
        logConstraints.gridy = 3;
        logConstraints.weightx = 0.1;
        logConstraints.fill = HORIZONTAL;
        this.add(bLOG, logConstraints);

        GridBagConstraints piConstraints = new GridBagConstraints();
        piConstraints.gridx = 0;
        piConstraints.gridy = 4;
        piConstraints.weightx = 0.1;
        piConstraints.fill = HORIZONTAL;
        this.add(bPI, piConstraints);

        GridBagConstraints sinConstraints = new GridBagConstraints();
        sinConstraints.gridx = 1;
        sinConstraints.gridy = 4;
        sinConstraints.weightx = 0.1;
        sinConstraints.fill = HORIZONTAL;
        this.add(bSIN, sinConstraints);

        GridBagConstraints cosConstraints = new GridBagConstraints();
        cosConstraints.gridx = 2;
        cosConstraints.gridy = 4;
        cosConstraints.weightx = 0.1;
        cosConstraints.fill = HORIZONTAL;
        this.add(bCOS, cosConstraints);

        GridBagConstraints tanConstraints = new GridBagConstraints();
        tanConstraints.gridx = 3;
        tanConstraints.gridy = 4;
        tanConstraints.weightx = 0.1;
        tanConstraints.fill = HORIZONTAL;
        this.add(bTAN, tanConstraints);

        GridBagConstraints backConstraints = new GridBagConstraints();
        backConstraints.gridx = 4;
        backConstraints.gridy = 4;
        backConstraints.weightx = 0.1;
        backConstraints.fill = HORIZONTAL;
        this.add(bBACK, backConstraints);

        GridBagConstraints enterConstraints = new GridBagConstraints();
        enterConstraints.gridx = 0;
        enterConstraints.gridy = 5;
        enterConstraints.weightx = 0.1;
        enterConstraints.fill = HORIZONTAL;
        this.add(bENTER, enterConstraints);


        GridBagConstraints b7Constraints = new GridBagConstraints();
        b7Constraints.gridx = 1;
        b7Constraints.gridy = 5;
        b7Constraints.weightx = 0.1;
        b7Constraints.fill = HORIZONTAL;
        this.add(b7, b7Constraints);

        GridBagConstraints b8Constraints = new GridBagConstraints();
        b8Constraints.gridx = 2;
        b8Constraints.gridy = 5;
        b8Constraints.weightx = 0.1;
        b8Constraints.fill = HORIZONTAL;
        this.add(b8, b8Constraints);

        GridBagConstraints b9Constraints = new GridBagConstraints();
        b9Constraints.gridx = 3;
        b9Constraints.gridy = 5;
        b9Constraints.weightx = 0.1;
        b9Constraints.fill = HORIZONTAL;
        this.add(b9, b9Constraints);

        GridBagConstraints bDivConstraints = new GridBagConstraints();
        bDivConstraints.gridx = 4;
        bDivConstraints.gridy = 5;
        bDivConstraints.weightx = 0.1;
        bDivConstraints.fill = HORIZONTAL;
        this.add(bDiv, bDivConstraints);

        GridBagConstraints switchConstraints = new GridBagConstraints();
        switchConstraints.gridx = 0;
        switchConstraints.gridy = 6;
        switchConstraints.weightx = 0.1;
        switchConstraints.fill = HORIZONTAL;
        this.add(bSwitch, switchConstraints);

        GridBagConstraints b4Constraints = new GridBagConstraints();
        b4Constraints.gridx = 1;
        b4Constraints.gridy = 6;
        b4Constraints.weightx = 0.1;
        b4Constraints.fill = HORIZONTAL;
        this.add(b4, b4Constraints);

        GridBagConstraints b5Constraints = new GridBagConstraints();
        b5Constraints.gridx = 2;
        b5Constraints.gridy = 6;
        b5Constraints.weightx = 0.1;
        b5Constraints.fill = HORIZONTAL;
        this.add(b5, b5Constraints);

        GridBagConstraints b6Constraints = new GridBagConstraints();
        b6Constraints.gridx = 3;
        b6Constraints.gridy = 6;
        b6Constraints.weightx = 0.1;
        b6Constraints.fill = HORIZONTAL;
        this.add(b6, b6Constraints);

        GridBagConstraints bMultConstraints = new GridBagConstraints();
        bMultConstraints.gridx = 4;
        bMultConstraints.gridy = 6;
        bMultConstraints.weightx = 0.1;
        bMultConstraints.fill = HORIZONTAL;
        this.add(bMult, bMultConstraints);

        GridBagConstraints shiftConstraints = new GridBagConstraints();
        shiftConstraints.gridx = 0;
        shiftConstraints.gridy = 7;
        shiftConstraints.weightx = 0.1;
        shiftConstraints.fill = HORIZONTAL;
        this.add(bSHIFT, shiftConstraints);

        GridBagConstraints b1Constraints = new GridBagConstraints();
        b1Constraints.gridx = 1;
        b1Constraints.gridy = 7;
        b1Constraints.weightx = 0.1;
        b1Constraints.fill = HORIZONTAL;
        this.add(b1, b1Constraints);

        GridBagConstraints b2Constraints = new GridBagConstraints();
        b2Constraints.gridx = 2;
        b2Constraints.gridy = 7;
        b2Constraints.weightx = 0.1;
        b2Constraints.fill = HORIZONTAL;
        this.add(b2, b2Constraints);

        GridBagConstraints b3Constraints = new GridBagConstraints();
        b3Constraints.gridx = 3;
        b3Constraints.gridy = 7;
        b3Constraints.weightx = 0.1;
        b3Constraints.fill = HORIZONTAL;
        this.add(b3, b3Constraints);

        GridBagConstraints bMinusConstraints = new GridBagConstraints();
        bMinusConstraints.gridx = 4;
        bMinusConstraints.gridy = 7;
        bMinusConstraints.weightx = 0.1;
        bMinusConstraints.fill = HORIZONTAL;
        this.add(bMinus, bMinusConstraints);

        GridBagConstraints clearConstraints = new GridBagConstraints();
        clearConstraints.gridx = 0;
        clearConstraints.gridy = 8;
        clearConstraints.weightx = 0.1;
        clearConstraints.fill = HORIZONTAL;
        this.add(bCLR, clearConstraints);

        GridBagConstraints b0Constraints = new GridBagConstraints();
        b0Constraints.gridx = 1;
        b0Constraints.gridy = 8;
        b0Constraints.weightx = 0.1;
        b0Constraints.fill = HORIZONTAL;
        this.add(b0, b0Constraints);

        GridBagConstraints bDotConstraints = new GridBagConstraints();
        bDotConstraints.gridx = 2;
        bDotConstraints.gridy = 8;
        bDotConstraints.weightx = 0.1;
        bDotConstraints.fill = HORIZONTAL;
        this.add(bDot, bDotConstraints);

        GridBagConstraints bSIGNConstraints = new GridBagConstraints();
        bSIGNConstraints.gridx = 3;
        bSIGNConstraints.gridy = 8;
        bSIGNConstraints.weightx = 0.1;
        bSIGNConstraints.fill = HORIZONTAL;
        this.add(bSIGN, bSIGNConstraints);

        GridBagConstraints bPlusConstraints = new GridBagConstraints();
        bPlusConstraints.gridx = 4;
        bPlusConstraints.gridy = 8;
        bPlusConstraints.weightx = 0.1;
        bPlusConstraints.fill = HORIZONTAL;
        this.add(bPlus, bPlusConstraints);


    }

    //Class that acts as as the action listener for all "number" buttons, as well as the dot and pi
    //in most cases, appends the string passed in the constructor to register1's content
    //for the case of pi, if register1 is not equal to zero or blank, it kicks up that value and puts pi in register1, and sets
    //tempVal to true;
    class numberListener extends AbstractAction {

        public numberListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY, actionCommandKey);
        }

        public void actionPerformed(ActionEvent e) {
            String num = e.getActionCommand();
            if (tempVal) {
                ENTER();
            }
            if (num.equals("pi")) {
                if (!(register1.getText().equals("") || Double.parseDouble(register1.getText()) == 0)) {
                    ENTER();
                }
                register1.setText(String.valueOf(Math.PI));
                tempVal = true;
                display();
            } else {
                register1.setText(register1.getText() + num);
            }
            menubar.requestFocus(); //throws away focus to prevent accidental actuation with enter key


        }


    }

    //class that acts as action listener for most "action" buttons like sine, log, etc.
    //simply calls the appropriate method.
    class opListener extends AbstractAction {

        public opListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY, actionCommandKey);
        }

        public void actionPerformed(ActionEvent e) { //button listener for all "action" buttons
            String op = e.getActionCommand();
            if (op.equals("SHIFT")) {
                shift();
                menubar.requestFocus(); //throws away focus to prevent accidental actuation with enter key
                return;
            }
            if (op.equals("BACKSPACE")) {

                backspace();
                menubar.requestFocus(); //throws away focus to prevent accidental actuation with enter key
                return;
            }

            if (register1.getText().equals("")) {
                register1.setText("0.0");
            }
            if (!shift) {
                switch (op) {
                    case "+":
                        add();
                        break;
                    case "-":
                        subtract();
                        break;
                    case "*":
                        multiply();
                        break;
                    case "/":
                        divide();
                        break;
                    case "SQRT":
                        sqrt();
                        break;
                    case "CLR":
                        clear();
                        break;
                    case "SIN":
                        sine();
                        break;
                    case "COS":
                        cosine();
                        break;
                    case "TAN":
                        tangent();
                        break;
                    case "POW":
                        power();
                        break;
                    case "SWITCH":
                        switchRegister();
                        tempVal = true;
                        break;
                    case "ENTER":
                        ENTER();
                        break;
                    case "SIGN":
                        flipSign();
                        break;
                    case "LN":
                        ln();
                        break;
                    case "LOG10":
                        log10();
                        break;
                    case "RECIPROCAL":
                        reciprocal();
                        break;
                }
            } else {
                switch (op) {
                    case "+":
                        add();
                        break;
                    case "-":
                        subtract();
                        break;
                    case "*":
                        multiply();
                        break;
                    case "/":
                        divide();
                        break;
                    case "SQRT":
                        nthroot();
                        break;
                    case "CLR":
                        clear();
                        break;
                    case "SIN":
                        arcsine();
                        break;
                    case "COS":
                        arccosine();
                        break;
                    case "TAN":
                        arctangent();
                        break;
                    case "POW":
                        power();
                        break;
                    case "SWITCH":
                        switchRegister();
                        tempVal = true;
                        break;
                    case "ENTER":
                        ENTER();
                        break;
                    case "SIGN":
                        flipSign();
                        break;
                    case "LN":
                        exp();
                        break;
                    case "LOG10":
                        exp10();
                        break;
                    case "RECIPROCAL":
                        reciprocal();
                        break;

                }

            }

            menubar.requestFocus(); //throws away focus to prevent accidental actuation with enter key
        }
    }

    //action listener for menu bar items, instantiates the appropriate subclass of JFrame
    class menuListener implements ActionListener {
        private String s;
        RPNCalc obj;

        public menuListener(String s, RPNCalc obj) {
            this.s = s;
            this.obj = obj;
        }

        public void actionPerformed(ActionEvent e) {
            switch (s) {
                case "numberConversion":
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            NumberConversionWindow w = new NumberConversionWindow(obj);
                            w.setSize(300, 150);
                            w.setVisible(true);
                            w.setLocation(500, 300);
                        }
                    });

                    break;
                case "vectorMath":
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            VectorMathWindow vectormathwindow = new VectorMathWindow(obj);
                            vectormathwindow.setSize(300, 300);
                            vectormathwindow.setVisible(true);
                            vectormathwindow.setLocation(500, 300);
                        }
                    });

                    break;
                case "config":
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            ConfigWindow configwindow = new ConfigWindow(obj);
                            configwindow.setSize(300, 250);
                            configwindow.setVisible(true);
                            configwindow.setResizable(false);
                            configwindow.setLocation(500, 300);
                        }
                    });

                    break;
                case "format":
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            FormatWindow formatwindow = new FormatWindow(obj);
                            formatwindow.setSize(300, 150);
                            formatwindow.setVisible(true);
                            formatwindow.setLocation(500, 300);
                        }
                    });

                    break;
                case "graphing":
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            GraphingWindow graphingwindow = new GraphingWindow();
                            graphingwindow.setVisible(true);
                            graphingwindow.setLocation(500, 300);

                        }
                    });
                    break;
            }
        }

    }

    //main method, instantiate RPNCalc
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                RPNCalc window = new RPNCalc();
                window.setSize(400, 275);
                window.setVisible(true);
                window.setLocation(500, 300);
                window.setResizable(false);
            }
        });



    }

}


 

