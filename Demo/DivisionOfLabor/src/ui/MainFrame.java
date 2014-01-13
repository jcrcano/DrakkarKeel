/*
 * @(#)MainFrame.java   2.0.0   26/11/2013
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
package ui;

import drakkar.prow.DrakkarProw;
import drakkar.oar.DocumentMetaData;
import drakkar.cover.swing.ResultURI;
import controllers.FrameController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

public final class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuHelp;
    private JTextField searchBox;
    public JTable resultsTable;
    private JPanel ownerPanel;
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel notificPanel;
    private JPanel bottomPanel;
    private JButton serachButton;
    private FrameController controller;
    private DrakkarProw client;
    private JLabel documentCount;
    private JScrollPane scrollPane;
    private JLabel notificSearch;

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public MainFrame(final DrakkarProw client) throws InterruptedException {
        try {
            this.client = client;
            documentCount = new JLabel("", SwingConstants.CENTER);
            setIconImage(getIconImage());
            controller = new FrameController(this);
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    controller.logout();
                    System.exit(1);
                }
            });

            //---BEGIN MENU---------------------------------------------------------
            menuBar = new JMenuBar();
            menuFile = new JMenu("File");
            menuFile.setMnemonic(KeyEvent.VK_F);
            menuHelp = new JMenu("Help");
            menuHelp.setMnemonic(KeyEvent.VK_H);
            JMenuItem menuItenExit = new JMenuItem("Exit");
            menuItenExit.setMnemonic(KeyEvent.VK_E);
            menuItenExit.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    controller.logout();
                    System.exit(1);
                }
            });
            menuFile.add(new JMenuItem("Open"));
            menuFile.add(menuItenExit);

            menuHelp.add(new JMenuItem("About"));
            menuBar.add(menuFile);
            menuBar.add(menuHelp);
            //---END MENU-----------------------------------------------------------

            //---BEGIN NORTH PANEL--------------------------------------------------
            northPanel = new JPanel();
            northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            searchBox = new JTextField();

            searchBox.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == 10) {
                        search();
                    }
                }
            });
            serachButton = new JButton("Search", createImageIcon("/resources/cirlab_logo.png"));
            serachButton.setPreferredSize(new Dimension(110, 30));
            serachButton.setMnemonic(KeyEvent.VK_S);
            serachButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    search();
                }
            });

            searchBox.setPreferredSize(new Dimension(250, 30));
            northPanel.add(searchBox);
            northPanel.add(serachButton);
            //---END NORTH PANEL----------------------------------------------------

            //---BEGIN CENTER PANEL-------------------------------------------------
            centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());

            resultsTable = new JTable(controller.getTableModel()) {

                @Override
                public TableColumnModel getColumnModel() {
                    try {
                        columnModel.getColumn(0).setWidth(20);
                        columnModel.getColumn(1).setWidth(getSize().width - 20);
                    } catch (Exception e) {
                    }
                    return super.getColumnModel();
                }

                //Implement table cell tool tips.
                @Override
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    try {
                        java.awt.Point p = e.getPoint();
                        int rowIndex = rowAtPoint(p);
                        DocumentMetaData docMetaData = (DocumentMetaData) getValueAt(rowIndex, 1);
                        Float size = docMetaData.getSize();
                        String sizeType = "Byte";
                        if (size > 1048576) {
                            size /= 1048576;
                            sizeType = "MB";
                        } else if (size > 1024) {
                            size /= 1024;
                            sizeType = "KB";
                        }
                        String sizeInMB = String.valueOf(size);
                        try {
                            sizeInMB = sizeInMB.substring(0, (sizeInMB.indexOf(".") + 3));
                        } catch (Exception ex) {
                        }
                        String fixPath = "" + client.getCnxServer().getServerHost() + "://" + controller.processPath(docMetaData.getPath());
                        try {
                            fixPath = fixPath.substring(0, 77).concat("...");
                        } catch (Exception ex) {
                        }
                        String author = docMetaData.getAuthor();
                        if (author == null || author.trim().isEmpty()) {
                            author = "-";
                        }
                        String lastModified = docMetaData.getLastModified();
                        if (lastModified == null || lastModified.trim().isEmpty()) {
                            lastModified = "-";
                        }
                        tip = "<html><strong>" + docMetaData.getName() + "</strong><br>&nbsp;&nbsp;&nbsp;Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + docMetaData.getType().toUpperCase() + "<br>&nbsp;&nbsp;&nbsp;Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + sizeInMB + " " + sizeType + "<br>&nbsp;&nbsp;&nbsp;Author:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + author + "<br>&nbsp;&nbsp;&nbsp;Last Modified:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + lastModified + "<br>&nbsp;&nbsp;&nbsp;URI:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fixPath + "</html>";
                    } catch (Exception ex) {
                    }
                    return tip;
                }
            };
            resultsTable.setGridColor(new java.awt.Color(255, 255, 255));
            resultsTable.setSize(800, 600);
            resultsTable.setFocusable(false);
            resultsTable.setRowHeight(26);
            resultsTable.getTableHeader().setResizingAllowed(false);
            resultsTable.getTableHeader().setReorderingAllowed(false);
            resultsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = resultsTable.getSelectedRow();
                        DocumentMetaData docMetaData = (DocumentMetaData) resultsTable.getValueAt(row, 1);
                        ResultURI URI = new ResultURI(docMetaData.getPath(), MainFrame.this.client.getCnxServer().getServerHost());
                        controller.getFile(URI.getOriginalPath());

                    }
                }
            });
            scrollPane = new JScrollPane(resultsTable);
            notificPanel = new JPanel();
            notificPanel.setLayout(new BorderLayout());
            notificSearch = new JLabel();
            notificPanel.add(notificSearch, BorderLayout.LINE_START);
            notificPanel.add(new JLabel("<html><strong>Welcome,</strong> " + client.getClientSetting().getSeeker().getUser() + "&nbsp;&nbsp;</html>", SwingConstants.RIGHT), BorderLayout.LINE_END);
            centerPanel.add(notificPanel, BorderLayout.NORTH);
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            //---END CENTER PANEL---------------------------------------------------

            //---BEGIN CENTER PANEL-------------------------------------------------
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.add(documentCount, BorderLayout.CENTER);
            //---END CENTER PANEL---------------------------------------------------
            this.setTitle("Collaborative Search System");
            this.setSize(800, 600);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().setLayout(new BorderLayout());
            this.setJMenuBar(menuBar);
            ownerPanel = new JPanel();
            BorderLayout ownerLeyout = new BorderLayout();
            ownerPanel.setLayout(ownerLeyout);
            ownerPanel.add(northPanel, BorderLayout.NORTH);
            ownerPanel.add(centerPanel, BorderLayout.CENTER);
            ownerPanel.add(bottomPanel, BorderLayout.SOUTH);
            this.getContentPane().add(ownerPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            throw new InterruptedException();
        }
    }

    public DrakkarProw getClient() {
        return client;
    }

    public JLabel getLabel() {
        return documentCount;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) throws IOException {
        java.net.URL imgURL = MainFrame.class.getResource(path);
        ImageIcon icon = null;
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        } else {
            throw new IOException("Couldn't find file: " + path);
        }
        return icon;
    }

    public void search() {
        final String query = searchBox.getText();
        if (!query.isEmpty()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    notificSearch.setText("<html><strong>Searching...</strong></html>");
                    controller.search(query);
                    notificSearch.setText("");
                }
            });
            thread.start();

        } else {
            JOptionPane.showMessageDialog(null, "Empty query", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("resources/cirlab_logo.png"));
        return retValue;
    }    
    
}
