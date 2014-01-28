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

import drakkar.prow.facade.desktop.event.ProwAdapter;
import drakkar.prow.facade.desktop.event.CollaborativeEnvironmentAdapter;
import drakkar.prow.facade.desktop.event.CollaborativeEnvironmentEvent;
import drakkar.prow.facade.desktop.event.SeekerEvent;
import drakkar.prow.facade.desktop.event.SynchronousAwarenessAdapter;
import drakkar.prow.facade.desktop.event.SynchronousAwarenessEvent;
import drakkar.oar.Response;
import drakkar.oar.ScorePQT;
import drakkar.oar.Seeker;
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.ImageUtil;
import drakkar.oar.util.KeyAwareness;
import static drakkar.oar.util.KeyAwareness.*;
import drakkar.oar.util.KeySession;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.NotifyAction;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.cover.swing.facade.PQTFacade;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;


public class JCollabQueryPanel extends JPanel {

    private Map<String, Integer> users;
    private DefaultPQTModel model;
    private JButton btnSearch;
    private String alternativeText;
    private List<FacadeDesktopListener> listener;

    /** Creates new form JCollabQuery */
    public JCollabQueryPanel() {
        initComponents();

        this.users = new HashMap<>();
        this.model = new DefaultPQTModel();
        this.btnSearch = null;
        this.listener = new ArrayList<>();
        listener.add(syncAwaraness);
        listener.add(clientAdapter);
        listener.add(collabAdapter);
    }

    /** Creates new form JCollabQuery
     * @param model
     */
    public JCollabQueryPanel(DefaultPQTModel model) {
        initComponents();
        qFieldSingleQuery.setVisible(true);
        panelCollabQuery.setBorder(BasicBorders.getTextFieldBorder());
        this.model = model;
        this.btnSearch = null;
        this.listener = new ArrayList<>();
        listener.add(syncAwaraness);
        listener.add(clientAdapter);
        listener.add(collabAdapter);
    }

    /** Creates new form JCollabQuery
     * @param model
     * @param btnSearch
     */
    public JCollabQueryPanel(DefaultPQTModel model, JButton btnSearch) {
        initComponents();
        panelCollabQuery.setBorder(BasicBorders.getTextFieldBorder());
        this.model = model;
        this.btnSearch = btnSearch;
        this.listener = new ArrayList<>();
        listener.add(syncAwaraness);
        listener.add(clientAdapter);
        listener.add(collabAdapter);
    }

    /**
     *
     * @param model
     */
    public void setModel(DefaultPQTModel model) {
        this.model = model;
        this.users = new HashMap<>();
        this.panelCollabQuery.removeAll();
        ImageIcon icon = new ImageIcon(ImageUtil.toBufferedImage(model.getSeeker().getAvatar()));
        BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
        icon = new ImageIcon(resized);
        this.panelCollabQuery.add(new JCollabQueryField(model.getSeeker().getUser(), icon, true, model.getMenuPQTFacade()));
        int count = 0;
        this.users.put(model.getSeeker().getUser(), count);

        for (Seeker item : model.getSeekerList()) {
            BufferedImage resized1 = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(new ImageIcon(ImageUtil.toBufferedImage(item.getAvatar())).getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            this.panelCollabQuery.add(new JCollabQueryField(item.getUser(), new ImageIcon(resized1), getColor(), false, model.getMenuPQTFacade()));
            count++;
            this.users.put(item.getUser(), count);
        }
    }
    private SynchronousAwarenessAdapter syncAwaraness = new SynchronousAwarenessAdapter() {

        @Override
        public void notifyPuttingQueryTermsTogether(SynchronousAwarenessEvent evt) {
            Response response = evt.getResponse();
            String sessionName = response.get(SESSION_NAME).toString();
            int event = (Integer) response.get(DISTRIBUTED_EVENT);

            switch (event) {
                case KeyAwareness.ENABLE_PQT:
                    boolean flag = (Boolean) response.get(IS_CHAIRMAN);
                    ArrayList<Seeker> seekersList = (ArrayList<Seeker>) response.get(SEEKERS_SESSION);
                    setModel(new DefaultPQTModel(sessionName, model.getSeeker(), seekersList, model.getMenuPQTFacade()));
                    panelCollabQuery.setVisible(true);
                    qFieldSingleQuery.setVisible(false);
                    btnSearch.setEnabled(flag);
                    revalidate();
                    break;
                case KeyAwareness.DISABLE_PQT:
                    panelCollabQuery.setVisible(false);
                    qFieldSingleQuery.setVisible(true);
                    model.setSeekerList(null);
                    model.setSessionName("");
                    btnSearch.setEnabled(true);
                    qFieldSingleQuery.setText("");
                    revalidate();
                    break;
            }

        }

        @Override
        public void notifyQueryChange(SynchronousAwarenessEvent evt) {
            Response response = evt.getResponse();
            String session = response.get(SESSION_NAME).toString();
            String query = response.get(QUERY).toString();
            String user = response.get(SEEKER_NICKNAME).toString();
            Map<String, ScorePQT> statistics = (Map<String, ScorePQT>) response.get(SCORE_PQT);

            if (model.getSessionName().equals(session)) {
                JCollabQueryField item = getJCollabQueryField(user);
                if (item != null) {
                    item.setQuery(query);
                    item.setStatistics(statistics);
                }
            }
        }

        @Override
        public void notifyQueryTyped(SynchronousAwarenessEvent evt) {
            Response response = evt.getResponse();
            String session = response.get(SESSION_NAME).toString();
            String user = response.get(SEEKER_NICKNAME).toString();
            boolean typed = (Boolean) response.get(QUERY_TYPED);

            if (model.getSessionName().equals(session)) {
                JCollabQueryField item = getJCollabQueryField(user);
                if (item != null) {
                    item.showKeyBoard(typed);
                }
            }
        }

        @Override
        public void notifyQueryTermAcceptance(SynchronousAwarenessEvent evt) {
            Response response = evt.getResponse();
            String session = response.get(SESSION_NAME).toString();
            String user = response.get(SEEKER_NICKNAME).toString();
            String term = response.get(QUERY_TERM).toString();
            int event = (Integer) response.get(DISTRIBUTED_EVENT);

            if (model.getSessionName().equals(session)) {
                JCollabQueryField item = getJCollabQueryField(user);
                if (item != null) {
                    switch (event) {
                        case TERM_AGREE:
                            item.updateAgreeCounter(term);
                            break;
                        case TERM_DISAGREE:
                            item.updateDisagreeCounter(term);
                            break;
                    }
                }
            }

        }
    };
    private ProwAdapter clientAdapter = new ProwAdapter() {

        @Override
        public void notifyCloseConnection(SeekerEvent evt) {
            if (panelCollabQuery.isVisible()) {
                users.clear();
                panelCollabQuery.setVisible(false);
                qFieldSingleQuery.setVisible(true);
                panelCollabQuery.removeAll();
            }
        }

        @Override
        public void notifySeekerEvent(SeekerEvent evt) {
            if (panelCollabQuery.isVisible()) {
                Response rsp = evt.getResponse();
                int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
                Seeker seeker = (Seeker) rsp.get(SEEKER);
                Integer index = users.get(seeker.getUser());

                if (index != null) {
                    switch (event) {

                        case KeySession.SEEKER_LOGOUT:
                            panelCollabQuery.remove(index);
                            users.remove(seeker.getUser());

                            break;
                        case NotifyAction.UPDATE_SEEKER_PROFILE:
                            JCollabQueryField item = getJCollabQueryField(seeker.getUser());
                            item.setIcon(new ImageIcon(seeker.getAvatar()));

                            break;
                    }
                }

            }

        }
    };
    private CollaborativeEnvironmentAdapter collabAdapter = new CollaborativeEnvironmentAdapter() {

        @Override
        public void notifyCollabSessionAuthentication(CollaborativeEnvironmentEvent evt) {
            if (panelCollabQuery.isVisible()) {
                Response rsp = evt.getResponse();
                int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
                switch (event) {
                    case SEEKER_LOGIN_COLLAB_SESSION:
                        Seeker seeker = (Seeker) rsp.get(SEEKER);
                        String user = seeker.getUser();
                        int index = panelCollabQuery.getComponents().length;
                        users.put(user, index);
                        panelCollabQuery.add(new JCollabQueryField(seeker.getUser(), new ImageIcon(seeker.getAvatar()), getColor(), false, model.getMenuPQTFacade()));


                        break;

                    case SEEKER_LOGOUT_COLLAB_SESSION:
                        Seeker seeker1 = (Seeker) rsp.get(SEEKER);
                        String user1 = seeker1.getUser();
                        Integer index1 = users.get(user1);
                        if (index1 != null) {
                            users.remove(user1);
                            panelCollabQuery.remove(index1);
                        }


                        break;
                    case SELF_LOGOUT_COLLAB_SESSION:
                        if (panelCollabQuery.isVisible()) {
                            users.clear();
                            panelCollabQuery.setVisible(false);
                            qFieldSingleQuery.setVisible(true);
                            panelCollabQuery.removeAll();
                        }


                        break;
                }
            }

        }

        @Override
        public void notifyCollabSessionEnding(CollaborativeEnvironmentEvent evt) {
            if (panelCollabQuery.isVisible()) {
                users.clear();
                panelCollabQuery.setVisible(false);
                qFieldSingleQuery.setVisible(true);
                panelCollabQuery.removeAll();
            }
        }
    };

    private Color getColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);

        return new Color(r, g, b);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        qFieldSingleQuery = new drakkar.cover.swing.JQueryField();
        panelCollabQuery = new javax.swing.JPanel();
        jQueryField1 = new drakkar.cover.swing.JCollabQueryField();
        jQueryField2 = new drakkar.cover.swing.JCollabQueryField();
        jQueryField3 = new drakkar.cover.swing.JCollabQueryField();
        jQueryField4 = new drakkar.cover.swing.JCollabQueryField();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        qFieldSingleQuery.setMinimumSize(new java.awt.Dimension(120, 20));
        qFieldSingleQuery.setName("qFieldSingleQuery"); // NOI18N
        qFieldSingleQuery.setPreferredSize(new java.awt.Dimension(120, 20));
        add(qFieldSingleQuery);

        panelCollabQuery.setBackground(new java.awt.Color(255, 255, 255));
        panelCollabQuery.setBorder(BasicBorders.getTextFieldBorder());
        panelCollabQuery.setName("panelCollabQuery"); // NOI18N
        panelCollabQuery.setLayout(new javax.swing.BoxLayout(panelCollabQuery, javax.swing.BoxLayout.LINE_AXIS));

        jQueryField1.setName("jQueryField1"); // NOI18N
        panelCollabQuery.add(jQueryField1);

        jQueryField2.setName("jQueryField2"); // NOI18N
        panelCollabQuery.add(jQueryField2);

        jQueryField3.setName("jQueryField3"); // NOI18N
        panelCollabQuery.add(jQueryField3);

        jQueryField4.setName("jQueryField4"); // NOI18N
        panelCollabQuery.add(jQueryField4);

        panelCollabQuery.setVisible(false);

        add(panelCollabQuery);
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @return
     */
    public PQTFacade getComponentFacade() {
        return this.model.getMenuPQTFacade();
    }

    /**
     *
     * @param facade
     */
    public void setComponentFacade(PQTFacade facade) {

        this.model.setMenuPQTFacade(facade);
        this.qFieldSingleQuery.setComponentFacade(facade);
    }

    private JCollabQueryField getJCollabQueryField(String user) {
        int index = this.users.get(user);
        if (index >= 0) {
            return (JCollabQueryField) this.panelCollabQuery.getComponent(index);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String getQuery() {

        if (this.qFieldSingleQuery.isVisible()) {
            return this.qFieldSingleQuery.getText();
        } else {
            Component[] componentes = this.panelCollabQuery.getComponents();
            StringBuilder query = new StringBuilder();
            JCollabQueryField item;

            for (Component component : componentes) {
                item = (JCollabQueryField) component;
                query.append(item.getQuery());
                query.append(" ");
            }
            return query.toString();
        }
    }

    /**
     *
     * @param query
     */
    public void setQuery(String query) {

        if (this.qFieldSingleQuery.isVisible()) {
            this.qFieldSingleQuery.setText(query);
        } else {
            JCollabQueryField field = (JCollabQueryField) this.panelCollabQuery.getComponent(0);
            field.setQuery(query);
        }
    }

    /**
     *
     * @param font
     */
    public void setFontQueryField(Font font) {

        if (this.qFieldSingleQuery.isVisible()) {
            this.qFieldSingleQuery.setFont(font);
        } else {
            Component[] componentes = this.panelCollabQuery.getComponents();
            JCollabQueryField item;
            for (Component component : componentes) {
                item = (JCollabQueryField) component;
                item.setFontQueryField(font);
            }
        }
    }

    /**
     *
     * @param c
     */
    public void setForegroundQueryField(Color c) {

        if (this.qFieldSingleQuery.isVisible()) {
            this.qFieldSingleQuery.setForeground(c);
        } else {
            Component[] componentes = this.panelCollabQuery.getComponents();
            JCollabQueryField item;
            for (Component component : componentes) {
                item = (JCollabQueryField) component;
                item.setForegroundQueryField(c);
            }
        }
    }

    /**
     *
     * @return
     */
    public FacadeDesktopListener[] getFacadeListeners() {
        return listener.toArray(new FacadeDesktopListener[0]);

    }

    /**
     *
     * @return
     */
    public JButton getButtonSearch() {
        return btnSearch;
    }

    /**
     * 
     * @param btnSearch
     */
    public void addButtonSearch(JButton btnSearch) {
        this.btnSearch = btnSearch;
    }

    /**
     *
     * @return
     */
    public String getAlternativeText() {
        return alternativeText;
    }

    /**
     *
     * @param text
     */
    public void setAlternativeText(String text) {
        this.alternativeText = text;
    }

    /**
     *
     * @return
     */
    public JQueryField getFieldQuery() {
        return qFieldSingleQuery;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private drakkar.cover.swing.JCollabQueryField jQueryField1;
    private drakkar.cover.swing.JCollabQueryField jQueryField2;
    private drakkar.cover.swing.JCollabQueryField jQueryField3;
    private drakkar.cover.swing.JCollabQueryField jQueryField4;
    private javax.swing.JPanel panelCollabQuery;
    private drakkar.cover.swing.JQueryField qFieldSingleQuery;
    // End of variables declaration//GEN-END:variables
}
