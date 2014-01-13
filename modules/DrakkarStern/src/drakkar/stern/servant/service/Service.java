/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Documents;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.exception.SessionException;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.SessionProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>Service</code> class is....
 * Esta clase constituye la raiz de todos los servicios soportados por el
 * framework DrakkarKeel
 */
public class Service {

    /**
     *
     */
    protected Map<String, SessionProfile> collaborativeSessions;
    /**
     *
     */
    protected FacadeListener listener;
    /**
     *
     */
    protected DataBaseController dbController;
    /**
     *
     */
    protected String defaultSessionName;
    /**
     *
     */
    protected SessionProfile defaultSessionProfile;
    /**
     * 
     */
    protected Map<Integer, MetaResults> resultsStore;
    protected Map<String, SessionProfile> htTempSessions;

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     */
    public Service(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
        this.defaultSessionName = defaultSessionName;
        this.defaultSessionProfile = defaultSessionProfile;
        defaultSessionProfile.getProperties().setSessionName(defaultSessionName);
        this.collaborativeSessions = collaborativeSessions;
        resultsStore = new HashMap<>();
        this.htTempSessions = htTempSessions;

    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param dbController
     */
    public Service(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
        this.defaultSessionName = defaultSessionName;
        this.defaultSessionProfile = defaultSessionProfile;
        this.collaborativeSessions = collaborativeSessions;
        this.dbController = dbController;
        resultsStore = new HashMap<>();
        defaultSessionProfile.getProperties().setSessionName(defaultSessionName);
        this.htTempSessions = htTempSessions;

    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param listener             oyente de la aplicación servidora
     * @param dbController 
     */
    public Service(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        this.defaultSessionName = defaultSessionName;
        this.defaultSessionProfile = defaultSessionProfile;
        this.collaborativeSessions = collaborativeSessions;
        this.listener = listener;
        this.dbController = dbController;
        resultsStore = new HashMap<>();
        defaultSessionProfile.getProperties().setSessionName(defaultSessionName);
        this.htTempSessions = htTempSessions;

    }

    /**
     * Devuelve la tabla hash de las sesiones
     *
     * @return sesiones
     */
    public Map<String, SessionProfile> getCollaborativeSessions() {
        return collaborativeSessions;
    }

    /**
     * Modifica el valor de la tabla hash de las sesiones
     *
     * @param collaborativeSessions nuevo valor
     */
    public void setCollaborativeSessions(Map<String, SessionProfile> collaborativeSessions) {
        this.collaborativeSessions = collaborativeSessions;
    }

    /**
     * Devuelve el oyente de la aplicación servidor
     *
     * @return oyente
     */
    public FacadeListener getListener() {
        return listener;
    }

    /**
     * Modifica el oyente de la aplicación servidor
     *
     * @param listener nuevo oyente
     */
    public void setListener(FacadeListener listener) {
        this.listener = listener;
    }

    /**
     *
     * @return
     */
    public String getDefaultSessionName() {
        return defaultSessionName;
    }

    /**
     *
     * @param sessionNameDefault
     */
    public void setDefaultSessionName(String sessionNameDefault) {
        this.defaultSessionName = sessionNameDefault;
    }

    /**
     *
     * @return
     */
    public Map<Integer, MetaResults> getResultsStore() {
        return resultsStore;
    }

    /**
     *
     * @param resultsStore
     */
    public void setResultsStore(Map<Integer, MetaResults> resultsStore) {
        this.resultsStore = resultsStore;
    }

    /**
     * actualizar
     * @param list
     */
    public synchronized void updateResultsStore(final ResultSetMetaData list) {

        MetaResults temp;
        Set<Integer> searchers = list.getResultsMap().keySet();
        for (Integer item : searchers) {
            if (resultsStore.containsKey(item)) {
                temp = resultsStore.get(item);
                temp.add(list.getResultList(item));
            } else {
                temp = new MetaResults();
                temp.add(list.getResultList(item));
                resultsStore.put(item, temp);
            }
        }
    }

    /**
     *
     * @return
     */
    public String getCommunicationSessionName() {
        return defaultSessionProfile.getProperties().getSessionName();

    }

    public boolean existCollabSession(String sessionName) {
        boolean flag = false;
        try {
            if (collaborativeSessions.containsKey(sessionName)) {
                flag = true;
            }
        } catch (NullPointerException e) {
            OutputMonitor.printStream("", e);
        }
        return flag;
    }

    protected SessionProfile getSessionProfile(String sessionName) throws SessionException {
        SessionProfile recordSession = null;
        if (sessionName.equals(getCommunicationSessionName())) {
            // se actualiza el role del miembro en el registro de la sesión
            recordSession = this.defaultSessionProfile;           

        } else if (existCollabSession(sessionName)) {
            recordSession = this.collaborativeSessions.get(sessionName);
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
        return recordSession;
    }

    /**
     * actualizar
     * @param list
     */
    public synchronized void updateResultsStore(final List<ResultSetMetaData> list) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                for (ResultSetMetaData documentsList : list) {
                    updateResultsStore(documentsList);
                }
            }
        });

        t.start();
    }

    /**
     * 
     * @param docs
     * @return
     */
    public ResultSetMetaData getDocumentsList(Documents docs) {

        Map<Integer, List<Integer>> documents = docs.getDocs();

        List<Integer> indexList = null;
        MetaResults meta;
        DocumentMetaData metaDoc;
         Set<Integer> searchersID = documents.keySet();
         ResultSetMetaData results = new ResultSetMetaData();

         for (Integer item : searchersID) {
             if (resultsStore.containsKey(item)) {
                meta = resultsStore.get(item);
                indexList = documents.get(item);
                List<DocumentMetaData> list = new ArrayList<>();
                for (Integer integer : indexList) {
                    metaDoc = meta.get(integer);
                    if (metaDoc != null) {
                        list.add(metaDoc);
                    }
                }

                results.add(item, list);

            }
        }

        return results;
    }

    /**
     * 
     */
    public void clear() {
        this.resultsStore.clear();
    }

    public SessionProfile getDefaultSessionProfile() {
        return defaultSessionProfile;
    }
}
