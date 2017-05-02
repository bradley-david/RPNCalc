/**This is the window that pops up when you click the "Formatting" button in the RPNCalc window. Much of the architecture is the the same as that 
 * of the ConfigWindow class. It reads formatting patterns of the form name:pattern from the text file formatPatterns.txt. 
 *  
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class FormatWindow extends JFrame {
    RPNCalc obj;					
    ArrayList<String[]> arrayList = new ArrayList<>();//this is the list that contains each line read from the text file, slip on the colons in the middle of each line so each element shoukd be an array of length 2
    String[] formatList;//list of names of format patterns to be displayed in combo box.
    Scanner formatScanner;//scans the file 
    JComboBox<String> comboBox;//lists all format patterns
    JButton save, cancel;       //obvious buttons are obvious

    public FormatWindow(RPNCalc obj) {
        super("Formatting");
        this.setLayout(new GridLayout(0, 1));
        this.obj = obj;
        try {
            UIManager.setLookAndFeel(obj.lookAndFeel);  //sets look and feel to the same look and feel as the RPNCalc window
        } catch (Exception ex) {
            
        }
        try {
            formatScanner = new Scanner(new File("formatPatterns.txt"));        //creates scanner, adds pairs of strings to the arraylist
            while (formatScanner.hasNextLine()) {
                arrayList.add(formatScanner.nextLine().split(":"));
            }
        } catch (FileNotFoundException e) {
            JFrame errorFrame = new JFrame("File error");
            errorFrame.setSize(600, 100);                   //if file isn't found, pops up a window
            errorFrame.add(new JLabel("The file \"formatPatterns.txt\" was not found. Defaulting to standard number formatting."));
            errorFrame.setVisible(true);
            errorFrame.setLocation(500, 300);
            this.dispose();

        }
        formatList = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {    //formatlist contains names of format patterns to be displayed
            formatList[i] = arrayList.get(i)[0];
        }

        comboBox = new JComboBox<>(formatList);
        this.add(comboBox);
        save = new JButton("Save");
        save.addActionListener(new FormatListener("save"));
        save.setActionCommand("save");
        this.add(save);

        cancel = new JButton("Cancel");
        cancel.addActionListener(new FormatListener("cancel"));
        cancel.setActionCommand("cancel");
        this.add(cancel);


    }

    class FormatListener extends AbstractAction {
        public FormatListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY,actionCommandKey);
        }

        public void actionPerformed(ActionEvent e) {            //when the save button is pressed
            String operation = e.getActionCommand();            //it searches for the name of the format
            switch (operation) {                                //pattern in the arraylist and applies the associated
                case "save":                                    //pattern to RPNCalc
                    String s = (String) comboBox.getSelectedItem();
                    for (String[] array : arrayList) {
                        if (array[0].equals(s)) {
                            obj.updateFormatPattern(array[1]);
                            obj.display();
                            break;
                        }
                    }
                    dispose();
                    break;
                case "cancel":
                    dispose();
                    break;
            }
        }
    }


}


