/*
 * Demos: DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package ui;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Icon;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public final class Login extends JDialog
        implements ActionListener,
        PropertyChangeListener {

    private String typedText = null;
    private JTextField input;

    private JOptionPane optionPane;

    private final String btnEnter = "Enter";
    private final String btnCancel = "Cancel";

    /**
     * Returns null if the typed string was invalid; otherwise, returns the
     * string as the user entered it.
     */
    public String getValidatedText() {
        return typedText;
    }

    /**
     * Creates the reusable dialog.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Login(Frame aFrame, String title, String msg, Icon icon) {
        super(aFrame, true);
        setIconImage(getIconImage());
        setTitle(title);
        input = new JTextField(10);
        //Create an array of the text and components to be displayed.        
        Object[] array = {msg, input};
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnEnter, btnCancel};
        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                icon,
                options,
                options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);
        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                input.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        input.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }

    /**
     * This method handles events for the text field.
     */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnEnter);
    }

    /**
     * This method reacts to state changes in the option pane.
     */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (btnEnter.equals(value)) {
                if (!input.getText().isEmpty()) {
                    typedText = input.getText();
                    clearAndHide();
                }
            } else {
                typedText = null;
                clearAndHide();
            }
        }
    }

    /**
     * This method clears the dialog and hides it.
     */
    public void clearAndHide() {
        input.setText(null);
        setVisible(false);
    }

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("resources/logo.png"));
        return retValue;
    }
}
