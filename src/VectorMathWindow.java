/**
 * This is the window that pops up when you click "Vector Arithmetic" in the RPNCalc window. Most of the architecture is the same as
 * the ConfigWindow class.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class VectorMathWindow extends JFrame {
    JTextField vector1Field, vector2Field, outputField;    //three text fields for input and output
    JComboBox<String> optionBox;    //selects which operation to perform
    JButton calculate;        //actually runs the calculation.
    
    public VectorMathWindow(RPNCalc obj) {
        super("Vector Arithmetic");
        try {
            UIManager.setLookAndFeel(obj.lookAndFeel);
        } catch (Exception ex) {
        
        }
        GridLayout layout = new GridLayout(5, 1);
        this.setLayout(layout);
        vector1Field = new JTextField(15);
        vector2Field = new JTextField(15);
        outputField = new JTextField(15);
        String[] options = {"Dot Product", "Cross Product"};
        optionBox = new JComboBox(options);
        
        
        calculate = new JButton("Calculate");
        CalculateListener calculatelistener = new CalculateListener("calculate");
        calculate.addActionListener(calculatelistener);
        
        calculate.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "calculate");
        calculate.getActionMap().put("calculate", calculatelistener);
        
        this.add(vector1Field);
        this.add(vector2Field);
        this.add(optionBox);
        this.add(calculate);
        this.add(outputField);
    }
    
    
    class CalculateListener extends AbstractAction {
        public CalculateListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY, actionCommandKey);
        }
        
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("calculate")) {
                String[] text1 = vector1Field.getText().split(",");
                String[] text2 = vector2Field.getText().split(",");
                double[] v1 = new double[text1.length];
                double[] v2 = new double[text2.length];
                if (text1.length != text2.length) {
                    outputField.setText("Vectors are different lengths");
                } else {
                    for (int i = 0; i < text1.length; i++) {
                        v1[i] = Double.parseDouble(text1[i]);
                        v2[i] = Double.parseDouble(text2[i]);
                    }
                    switch ((String) (optionBox.getSelectedItem())) {
                        case "Dot Product":
                            outputField.setText(String.valueOf(ExtendedMath.dotProduct(v1, v2)));
                            break;
                        case "Cross Product":
                            if (v1.length != 3 || v2.length != 3) {
                                outputField.setText("Vectors are wrong lengths");
                                
                            } else {
                                outputField.setText(Arrays.toString(ExtendedMath.crossProduct(v1, v2)));
                            }
                            break;
                        
                        
                    }
                }
                
                
            }
        }
    }
    
    
}
