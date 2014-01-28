/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.cover.swing;

import drakkar.oar.ScorePQT;
import drakkar.oar.util.ImageUtil;
import static drakkar.oar.util.KeyAwareness.*;
import drakkar.oar.util.OutputMonitor;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.cover.swing.facade.PQTFacade;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class JCollabQueryField extends JPanel implements ActionListener {

    private Timer timer;
    private Map<String, ScorePQT> statistics;
    private String user;
    /**
     * contains all application methods
     */
    private PQTFacade facade;
    private static String term;

    /** Creates new form BeanForm */
    public JCollabQueryField() {
        initComponents();
        statistics = new HashMap<>();
    }

    /** Creates new form JQueryField
     *
     * @param user nombre del usuario
     * @param icon avatar del usuario
     * @param flag
     */
    public JCollabQueryField(String user, ImageIcon icon, boolean flag) {

        initComponents();
        this.lbUser.setToolTipText(user);
        if (icon.getIconWidth() > 16) {
            BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            this.lbUser.setIcon(new ImageIcon(resized));
        } else {
            this.lbUser.setIcon(icon);
        }


        this.statistics = new HashMap<>();
        this.tFieldQuery.setEditable(flag);
        this.user = user;
        this.initTimer(flag);
    }

    /** Creates new form JQueryField
     *
     * @param user  nombre del usuario
     * @param icon  avatar del usuario
     * @param flag
     * @param facade
     *
     */
    public JCollabQueryField(String user, ImageIcon icon, boolean flag, PQTFacade facade) {

        initComponents();
        this.lbUser.setToolTipText(user);
        if (icon.getIconWidth() > 16) {
            BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            this.lbUser.setIcon(new ImageIcon(resized));
        } else {
            this.lbUser.setIcon(icon);
        }
        this.statistics = new HashMap<>();
        this.facade = facade;
        this.tFieldQuery.setEditable(flag);
        this.user = user;
        this.initTimer(flag);
    }

    /** Creates new form JQueryField
     *
     * @param user  nombre del usuario
     * @param icon  avatar del usuario
     * @param color color del texto
     * @param flag
     *
     */
    public JCollabQueryField(String user, ImageIcon icon, Color color, boolean flag) {

        initComponents();
        this.lbUser.setToolTipText(user);
        if (icon.getIconWidth() > 16) {
            BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            this.lbUser.setIcon(new ImageIcon(resized));
        } else {
            this.lbUser.setIcon(icon);
        }
        this.tFieldQuery.setForeground(color);
        this.statistics = new HashMap<>();
        this.tFieldQuery.setEditable(flag);
        this.user = user;
        this.initTimer(flag);
    }

    /** Creates new form JQueryField
     *
     * @param user  nombre del usuario
     * @param icon  avatar del usuario
     * @param color color del texto
     * @param flag
     * @param facade
     *
     */
    public JCollabQueryField(String user, ImageIcon icon, Color color, boolean flag, PQTFacade facade) {

        initComponents();
        this.lbUser.setToolTipText(user);
        this.lbUser.setIcon(icon);
        this.tFieldQuery.setForeground(color);
        this.statistics = new HashMap<>();
        this.facade = facade;
        this.tFieldQuery.setEditable(flag);
        this.user = user;
        this.initTimer(flag);
    }

    private void initTimer(boolean flag) {
        if (flag) {
            this.mItemAgree.setEnabled(false);
            this.mItemDisagree.setEnabled(false);
            this.tFieldQuery.requestFocusInWindow();
            timer = new Timer(3000, this);
            timer.setInitialDelay(5000);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pMenuOptions = new javax.swing.JPopupMenu();
        mItemAgree = new javax.swing.JMenuItem();
        mItemDisagree = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mItemStatistics = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lbUser = new javax.swing.JLabel();
        antKeyBoard = new drakkar.cover.swing.JAnimator();
        antKeyBoard.setVisible(false);
        tFieldQuery = new javax.swing.JTextField();

        pMenuOptions.setInvoker(tFieldQuery);
        pMenuOptions.setLabel("Options");
        pMenuOptions.setName("pMenuOptions"); // NOI18N

        mItemAgree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drakkar/cover/resources/btnAgree.png"))); // NOI18N
        mItemAgree.setText("Agree");
        mItemAgree.setName("mItemAgree"); // NOI18N
        mItemAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemAgreeActionPerformed(evt);
            }
        });
        pMenuOptions.add(mItemAgree);

        mItemDisagree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drakkar/cover/resources/btnDisagree.png"))); // NOI18N
        mItemDisagree.setText("Disagree");
        mItemDisagree.setName("mItemDisagree"); // NOI18N
        mItemDisagree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemDisagreeActionPerformed(evt);
            }
        });
        pMenuOptions.add(mItemDisagree);

        jSeparator1.setName("jSeparator1"); // NOI18N
        pMenuOptions.add(jSeparator1);

        mItemStatistics.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drakkar/cover/resources/btnStatistics.png"))); // NOI18N
        mItemStatistics.setText("Statistics");
        mItemStatistics.setName("mItemStatistics"); // NOI18N
        mItemStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemStatisticsActionPerformed(evt);
            }
        });
        pMenuOptions.add(mItemStatistics);

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(80, 26));
        setPreferredSize(new java.awt.Dimension(80, 26));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(38, 24));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(38, 24));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        lbUser.setFont(new java.awt.Font("Tahoma", 1, 11));
        lbUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drakkar/cover/resources/user16.png"))); // NOI18N
        lbUser.setMaximumSize(new java.awt.Dimension(24, 24));
        lbUser.setMinimumSize(new java.awt.Dimension(24, 24));
        lbUser.setName("lbUser"); // NOI18N
        lbUser.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(lbUser);

        antKeyBoard.setBackground(new java.awt.Color(255, 255, 255));
        antKeyBoard.setName("antKeyBoard"); // NOI18N
        antKeyBoard.setPreferredSize(new java.awt.Dimension(12, 12));
        jPanel1.add(antKeyBoard);

        add(jPanel1, java.awt.BorderLayout.WEST);

        tFieldQuery.setEditable(false);
        tFieldQuery.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tFieldQuery.setMargin(new java.awt.Insets(1, 10, 1, 5));
        tFieldQuery.setName("tFieldQuery"); // NOI18N
        tFieldQuery.setOpaque(false);
        tFieldQuery.setPreferredSize(new java.awt.Dimension(31, 22));
        tFieldQuery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tFieldQueryMouseClicked(evt);
            }
        });
        tFieldQuery.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tFieldQueryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tFieldQueryFocusLost(evt);
            }
        });
        tFieldQuery.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tFieldQueryKeyTyped(evt);
            }
        });
        add(tFieldQuery, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void mItemAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemAgreeActionPerformed
        invokeMethod(TERM_AGREE);
}//GEN-LAST:event_mItemAgreeActionPerformed

    private void mItemDisagreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemDisagreeActionPerformed
        invokeMethod(TERM_DISAGREE);
}//GEN-LAST:event_mItemDisagreeActionPerformed

    private void mItemStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemStatisticsActionPerformed
        invokeMethod(SHOW_STATISTICS);
}//GEN-LAST:event_mItemStatisticsActionPerformed

    private void tFieldQueryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tFieldQueryMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            term = this.tFieldQuery.getSelectedText();

            if (term != null) {
                term.trim();
                int length = this.tFieldQuery.getText().length();
                int start = this.tFieldQuery.getSelectionStart();
                int end = this.tFieldQuery.getSelectionEnd();

                if (start > 0) {
                    char beforeChar = this.tFieldQuery.getText().charAt(start - 1);
                    if (beforeChar != ' ') {
                        showError();
                        return;
                    }
                }

                if (end < length) {
                    char afterChar = this.tFieldQuery.getText().charAt(end);
                    if (afterChar != ' ') {
                        showError();
                        return;
                    }
                }

                Point p = evt.getLocationOnScreen();
                pMenuOptions.setLocation(p.x, p.y + 15);
                pMenuOptions.setVisible(true);
                this.pMenuOptions.requestFocusInWindow();

            } else {
                JOptionPane.showMessageDialog(this.getParent(), "Select a term of query!", "Error!", JOptionPane.ERROR_MESSAGE);
            }


        }
    }//GEN-LAST:event_tFieldQueryMouseClicked

    private void tFieldQueryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFieldQueryFocusGained
    }//GEN-LAST:event_tFieldQueryFocusGained

    private void tFieldQueryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFieldQueryFocusLost
        if (tFieldQuery.isEditable()) {
            invokeMethod(SEND_ACTION_QUERY_TYPED);
            this.antKeyBoard.setVisible(false);
            this.antKeyBoard.stop();
        }
    }//GEN-LAST:event_tFieldQueryFocusLost

    private void tFieldQueryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFieldQueryKeyTyped
        if (!antKeyBoard.isVisible()) {
            this.antKeyBoard.setVisible(true);
            this.antKeyBoard.start();
             invokeMethod(SEND_ACTION_QUERY_TYPED);
        } else {
            this.antKeyBoard.start();
        }

        invokeMethod(SEND_ACTION_QUERY_CHANGE);

    }//GEN-LAST:event_tFieldQueryKeyTyped

    public void actionPerformed(ActionEvent e) {
        invokeMethod(SEND_ACTION_QUERY_CHANGE);
    }

    private void showError() {
        JOptionPane.showMessageDialog(this.getParent(), "Select all of the term!", "Error!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Execute the  corresponding method in the MenuPQTFacade.
     */
    private void invokeMethod(final int idMethod) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    String method = null;
                    switch (idMethod) {
                        case SEND_ACTION_QUERY_CHANGE:
                            method = "notifyQueryChange";
                            facade.getClass().getMethod(method, new Class[]{String.class, Map.class}).invoke(facade, new Object[]{tFieldQuery.getText(), statistics});
                            break;
                        case SEND_ACTION_QUERY_TYPED:
                            method = "notifyQueryTyped";
                            facade.getClass().getMethod(method, new Class[]{boolean.class}).invoke(facade, new Object[]{tFieldQuery.isFocusOwner()});
                            break;
                        case TERM_AGREE:
                            method = "agree";
                            facade.getClass().getMethod(method, new Class[]{String.class, String.class}).invoke(facade, new Object[]{term, user});
                            updateAgreeCounter(term);
                            break;
                        case TERM_DISAGREE:
                            method = "disagree";
                            facade.getClass().getMethod(method, new Class[]{String.class, String.class}).invoke(facade, new Object[]{term, user});
                            updateDisagreeCounter(term);
                            break;
                        default:
                            method = "showStatistics";
                            ScorePQT score = statistics.get(term);
                            if (score == null) {
                                score = new ScorePQT(0, 0);
                                statistics.put(term, score);
                            }

                            facade.getClass().getMethod(method, new Class[]{String.class, ScorePQT.class}).invoke(facade, new Object[]{term, score});
                    }
                } catch (Exception ex) {
                    OutputMonitor.printStream("", ex);
                }

            }
        });
    }

    /**
     * 
     * @param term
     */
    public void updateAgreeCounter(String term) {
        ScorePQT score = this.statistics.get(term);
        if (score != null) {
            score.setAgree(score.getAgree() + 1);
        } else if (tFieldQuery.getText().contains(term)) {
            score = new ScorePQT(1, 0);
            this.statistics.put(term, score);
        }
    }

    /**
     *
     * @param term
     */
    public void updateDisagreeCounter(String term) {
        ScorePQT score = this.statistics.get(term);
        if (score != null) {
            score.setDisagree(score.getDisagree() + 1);
        } else if (tFieldQuery.getText().contains(term)) {
            score = new ScorePQT(0, 1);
            this.statistics.put(term, score);
        }
    }

    /**
     * Modificar el avatar del usuario
     *
     * @param icon avatar del usuario
     */
    public void setIcon(ImageIcon icon) {
        this.lbUser.setIcon(icon);
    }

    /**
     * Retorna el avatar del usuario
     *
     * @return avatar
     */
    public Icon getIcon() {
        return this.lbUser.getIcon();
    }

    /**
     * Modicar el nombre del usuario
     *
     * @param user nombre del usuario
     */
    public void setUser(String user) {
        this.user = user;
        this.lbUser.setToolTipText(user);
    }

    /**
     * Retorna el nombre del usuario
     *
     * @return nombre
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Modicar el color del texto del campo de la consulta de búsqueda
     *
     * @param color color del texto
     */
    public void setTextColor(Color color) {
        this.tFieldQuery.setForeground(color);
    }

    /**
     * Retorna el color del texto del campo de la consulta de búsqueda
     *
     * @return color del texto
     */
    public Color getTextColor() {
        return this.tFieldQuery.getForeground();
    }

    /**
     * Retorna el color del texto del campo de la consulta de búsqueda
     *
     * @return color del texto
     */
    public Map<String, ScorePQT> getStatistics() {
        return statistics;
    }

    /**
     * Modicar el color del texto del campo de la consulta de búsqueda
     *
     * @param statistics
     */
    public void setStatistics(Map<String, ScorePQT> statistics) {
        this.statistics = statistics;
    }

    /**
     * Get the value of componentFacade
     *
     * @return the value of componentFacade
     */
    public PQTFacade getMenuPQTFacade() {
        return facade;
    }

    /**
     * Set the value of componentFacade
     *
     * @param componentFacade new value of componentFacade
     */
    public void setComponentFacade(PQTFacade componentFacade) {
        this.facade = componentFacade;
    }

    /**
     *
     * @return
     */
    public JLabel getLabelUser() {
        return lbUser;
    }

    /**
     *
     * @return
     */
    public JTextField getTextFieldQuery() {
        return tFieldQuery;
    }

    /**
     *
     * @return
     */
    public String getQuery() {
        return this.tFieldQuery.getText();
    }

    /**
     * Modifica el valor de la consulta de búsqueda
     *
     * @param query consulta
     */
    public void setQuery(String query) {
        this.tFieldQuery.setText(query);
    }

    /**
     *
     * @param flag
     */
    public void showKeyBoard(boolean flag) {
        this.antKeyBoard.setVisible(flag);
        if (flag) {
            this.antKeyBoard.start();
        } else {
            this.antKeyBoard.stop();
        }
    }

    /**
     *
     * @param font
     */
    public void setFontQueryField(Font font) {
        this.tFieldQuery.setFont(font);


    }

    /**
     * 
     * @param c
     */
    public void setForegroundQueryField(Color c) {
        this.tFieldQuery.setForeground(c);


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private drakkar.cover.swing.JAnimator antKeyBoard;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lbUser;
    private javax.swing.JMenuItem mItemAgree;
    private javax.swing.JMenuItem mItemDisagree;
    private javax.swing.JMenuItem mItemStatistics;
    private javax.swing.JPopupMenu pMenuOptions;
    private javax.swing.JTextField tFieldQuery;
    // End of variables declaration//GEN-END:variables
}
