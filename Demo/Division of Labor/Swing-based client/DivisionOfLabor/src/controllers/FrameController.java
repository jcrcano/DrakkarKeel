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
import drakkar.prow.RequestSearchFactory;
import drakkar.prow.facade.desktop.event.ProwAdapter;
import drakkar.prow.facade.desktop.event.SearchAdapter;
import drakkar.prow.facade.desktop.event.SearchEvent;
import drakkar.prow.facade.desktop.event.SeekerEvent;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.util.Invocation;
import static drakkar.oar.util.KeySession.CONNECTION_FAILED;
import static drakkar.oar.util.KeySession.CONNECTION_SUCCESSFUL;
import static drakkar.oar.util.KeyTransaction.REPLY;
import static drakkar.oar.util.KeyTransaction.SEARCH_RESULTS;
import drakkar.oar.util.OutputMonitor;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import ui.SearchTableModel;

public class FrameController {

    static int status = 0;
    private DrakkarProw client;
    private SearchTableModel tableModel;
    private JLabel label;
    @SuppressWarnings("FieldMayBeFinal")
    ProwAdapter clientAdapter = new ProwAdapter() {

        @Override
        public void notifyRequestConnection(SeekerEvent evt) {
            Response response = evt.getResponse();
            int reply = Integer.valueOf(response.get(REPLY).toString());
            switch (reply) {
                case CONNECTION_FAILED:
                    JOptionPane.showMessageDialog(null, "Failed Connection!!", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                    break;
                case CONNECTION_SUCCESSFUL:
                    break;
            }
        }
    };
    SearchAdapter searchAdapter = new SearchAdapter() {

        @Override
        public void notifySearchResults(SearchEvent se) {
            Response response = se.getResponse();
            ResultSetMetaData list = (ResultSetMetaData) response.get(SEARCH_RESULTS);
            tableModel.setDataModel((ArrayList) list.getAllResultList(), client.getCnxServer().getServerHost());
            label.setText("<html>&nbsp;" + list.getAllResultList().size() + "&nbsp;matching documents&nbsp;</html>");
        }
    };

    public FrameController(ui.MainFrame mainFrame) throws RequestException {
        this.client = mainFrame.getClient();
        this.label = mainFrame.getLabel();
        tableModel = new SearchTableModel();
        client.getClientListenerManager().addClientListener(clientAdapter);
        client.getClientListenerManager().addSearchListener(searchAdapter);
        client.loginSearchCollabSession();
    }

    public void search(String query) {
        Request request = RequestSearchFactory.create(query, RequestSearchFactory.META_SEARCH_AND_SPLIT);
        client.send(request, Invocation.SYNCHRONOUS_METHOD_INVOCATION);
    }

    public void logout() {
        client.logout();
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public void getFile(String url) {
        try {
            client.downloadAndOpenFile(url);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed when opening the file.", "Collaborative Search System", 0);
        }
    }

    public SearchTableModel getTableModel() {
        return tableModel;
    }

    public String processPath(String path) {
        File f = new File(path);
        String newPath = null;
        try {
            newPath = f.toURI().toURL().toString().substring(9);
        } catch (MalformedURLException ex) {
            OutputMonitor.printStream("", ex);
        }
        return newPath;
    }

}
