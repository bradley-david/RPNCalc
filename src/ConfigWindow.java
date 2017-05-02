/**This is the window that pops up when you click the "settings" JMenuItem in RPNCalc the window
 * 
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ConfigWindow extends JFrame {    
    JRadioButton bRadians, bDegrees, bDefaultSwingLook, bSystemLook;//radio buttons for various settings
    JButton save, cancel;//self explanatory buttons that go at the bottom
    ButtonGroup angleGroup, lookGroup;//groups radio buttons so that only one in a set can be clicked.
    JLabel angleLabel, lookLabel;//labels the buttons groups
    boolean radians;//instance variable that parallels RPNCalc's radians instance variable
    String lookAndFeel;//again parallels RPNCalc's instance variable
    RPNCalc obj;//instance variable that points to the RPNCalc instance that is passed from the RPNCalc class itself to the constructor

    public ConfigWindow(RPNCalc obj) {

        super("Settings");
        try {
            UIManager.setLookAndFeel(obj.lookAndFeel);	//sets look and feel to be consistent with the RPNCalc class's look and feel. 
        } catch (Exception ex) {
           
        }
        lookAndFeel = obj.lookAndFeel;
        this.obj = obj;
        this.setLayout(new GridLayout(0, 1));	//uses single-column gridLayout with unspecified rows

        angleLabel = new JLabel("Angle Format");

        bRadians = new JRadioButton("Radians");
        bRadians.addActionListener(new ConfigListener("radians"));
        bRadians.setActionCommand("radians");

        bDegrees = new JRadioButton("Degrees");
        bDegrees.addActionListener(new ConfigListener("degrees"));
        bDegrees.setActionCommand("degrees");

        lookLabel = new JLabel("Appearance");

        bDefaultSwingLook = new JRadioButton("Default Swing");
        bDefaultSwingLook.addActionListener(new ConfigListener("default_swing"));
        bDefaultSwingLook.setActionCommand("default_swing");

        bSystemLook = new JRadioButton("System Look");
        bSystemLook.addActionListener(new ConfigListener("system_look"));
        bSystemLook.setActionCommand("system_look");


        save = new JButton("Save");
        save.addActionListener(new ConfigListener("save"));
        save.setActionCommand("save");

        cancel = new JButton("Cancel");
        cancel.addActionListener(new ConfigListener("cancel"));
        cancel.setActionCommand("cancel");

        if (obj.radians) {				//these if statements set which radio button is selected on window start
            bRadians.setSelected(true);
        } else {
            bDegrees.setSelected(true);
        }

        if (obj.lookAndFeel.equals(UIManager.getCrossPlatformLookAndFeelClassName())) {
            bDefaultSwingLook.setSelected(true);
        } else if (obj.lookAndFeel.equals(UIManager.getSystemLookAndFeelClassName())) {
            bSystemLook.setSelected(true);
        }

        angleGroup = new ButtonGroup();
        angleGroup.add(bRadians);
        angleGroup.add(bDegrees);

        lookGroup = new ButtonGroup();
        lookGroup.add(bDefaultSwingLook);
        lookGroup.add(bSystemLook);

        this.add(angleLabel);
        this.add(bRadians);
        this.add(bDegrees);

        this.add(lookLabel);
        this.add(bDefaultSwingLook);
        this.add(bSystemLook);

        this.add(save);
        this.add(cancel);

    }

    class ConfigListener extends AbstractAction {	//action listener for all of the buttons in this window


        public ConfigListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY, actionCommandKey);

        }

        public void actionPerformed(ActionEvent e) {	
            String operation = e.getActionCommand();
            switch (operation) {
                case "radians":
                    radians = true;
                    break;
                case "degrees":
                    radians = false;
                    break;
                case "default_swing":
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                    break;
                case "system_look":
                    lookAndFeel = UIManager.getSystemLookAndFeelClassName();
                    break;
                case "save":
                    obj.radians = radians;
                    obj.setLookAndFeel(lookAndFeel);
                    obj.repaint();
                    setVisible(false);
                    dispose();
                    break;
                case "cancel":
                    setVisible(false);
                    dispose();
                    break;

            }
        }
    }
}


