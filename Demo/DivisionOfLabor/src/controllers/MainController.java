/*
 * @(#)MainController.java   2.0.0   26/11/2013
 *
 * CIRLab - Collaborative Information Retrieval Laboratory
 * Webpage: unascribed
 * Contact: cirlab-dev@googlegroups.com
 * University of Granada - Department of Computing Science and Artificial
 * Intelligence
 * http://www.ugr.es/
 * University of the Informatics Sciences - Holguin Software Development Center
 * http://www.uci.cu/
 *
 * The contents of this file are subject under the terms described in the
 * CIRLAB_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License.
 *
 * Copyright (C) 2008-2013 CIRLab. All Rights Reserved.
 *
 * Contributor(s):
 *   RODRIGUEZ-AVILA, HUMBERTO <hrodrigueza{a.}facinf.uho.edu.cu>
 *   LEYVA-AMADOR, YOVIER <yovi{a.}hol.inv.cu>
 *   RODRIGUEZ-CANO, JULIO CESAR <jcrcano{a.}uci.cu>
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
        Login login = new Login(frame, "Collaborative Search System", "Please, set the server IP or DNS name:", new javax.swing.ImageIcon(Login.class.getResource("/resources/cirlab_server.png")));
        login.pack();
        login.setLocationRelativeTo(frame);
        login.setVisible(true);
        return login.getValidatedText();
    }

}
