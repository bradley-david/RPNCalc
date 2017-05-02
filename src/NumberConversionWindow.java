/**Simple Jframe class designed to be invoked form RPNCalc's menu bar.
* Provides integer conversion from decimal to binary, hexadecimal and octal.
* *
* *
* *
* *
* *
* *
*
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class NumberConversionWindow extends JFrame  {
    JTextField inputField, outputField;//input number, output number
    JLabel panel;//just a placeholder to take up space
    JButton convert;//button that does calculation

    JComboBox<String> inputBox, outputBox;      //combo boxes to determine which base the input and output is in

    public NumberConversionWindow(RPNCalc obj) {
        super("Number Conversion");
        try{
        	UIManager.setLookAndFeel(obj.lookAndFeel);//sets look and feel to the same look and feel as the RPNCalc window
        }
        catch(Exception ex){

        }
        String[] types = {"Binary", "Decimal", "Hex", "Octal"}; //different number bases
        GridLayout layout = new GridLayout(2, 3);//uses 2x3 gridLayout
        this.setLayout(layout);

        inputField = new JTextField(10);
        outputField = new JTextField(10);
        outputField.setEditable(false);//cannot edit output field
        panel = new JLabel();
        inputBox = new JComboBox<>(types);
        outputBox = new JComboBox<>(types);

        convert = new JButton("Convert");
        buttonListener listener = new buttonListener("convert");
        convert.addActionListener(listener);
        convert.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"calculate");
        convert.getActionMap().put("calculate",listener);
        convert.setActionCommand("convert");


        this.add(inputField);
        this.add(panel);
        this.add(outputField);
        this.add(inputBox);
        this.add(convert);
        this.add(outputBox);

    }
    //this actually does the calculation using the methods of the Integer class
    class buttonListener extends AbstractAction{
        public buttonListener(String actionCommandKey){
            putValue(ACTION_COMMAND_KEY,actionCommandKey);
        }
        public void actionPerformed(ActionEvent e) {
            int inputNum = 0;
            try {

                switch ((String) inputBox.getSelectedItem()) {
                    case "Binary":
                        inputNum = Integer.parseInt(inputField.getText(), 2);
                        break;
                    case "Decimal":
                        inputNum = Integer.parseInt(inputField.getText());
                        break;
                    case "Hex":
                        inputNum = Integer.decode("0x" + inputField.getText());
                        break;
                    case "Octal":
                        inputNum = Integer.parseInt(inputField.getText(), 8);
                        break;


                }
                switch ((String) outputBox.getSelectedItem()) {
                    case "Binary":
                        outputField.setText(Integer.toString(inputNum, 2));
                        break;
                    case "Decimal":
                        outputField.setText(Integer.toString(inputNum));
                        break;
                    case "Hex":
                        outputField.setText(Integer.toHexString(inputNum));
                        break;
                    case "Octal":
                        outputField.setText(Integer.toString(inputNum, 8));
                        break;
                }
            }
            catch(NumberFormatException ex){
                outputField.setText("Format error.");
            }

        }


    }

}
