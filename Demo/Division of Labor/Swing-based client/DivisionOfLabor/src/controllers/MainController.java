/*
 * Demos: DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package controllers;

import drakkar.prow.DrakkarProw;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import ui.Login;
import ui.MainFrame;

public class MainController {

    private static DrakkarProw client;

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        while (true) {
            try {
                String host = showLogin();
                if (host != null) {
                    String[] param = {"-sh", host};
                    client = new DrakkarProw(param);
                    MainFrame mainFrame = new MainFrame(client);
                    mainFrame.setLocationRelativeTo(null);
                    mainFrame.setVisible(true);
                    break;
                } else {
                    System.exit(0);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Server not found.", "Collaborative Search System", 0);
            }
        }
    }

    public static String showLogin() {
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        Login login = new Login(frame, "Collaborative Search System", "Please, set the server IP or DNS name:", new javax.swing.ImageIcon(Login.class.getResource("/resources/server.png")));
        login.pack();
        login.setLocationRelativeTo(frame);
        login.setVisible(true);
        return login.getValidatedText();
    }

}
