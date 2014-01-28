/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.facade.desktop.event;

import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.OutputMonitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la encargada de registrar todos los oyentes de la aplicación
 * cliente, para su posterior notificación al ocurrir algún tipo de notificación
 * por parte de la aplicación servidora
 */
public class ListenerManager {

    private ArrayList<ProwListener> clientListeners;
    private ArrayList<TextMessageListener> messagesListeners;
    private ArrayList<SearchListener> searchListeners;
    private ArrayList<ExplicitRecommendationListener> recommendationListeners;
    private ArrayList<CollaborativeEnvironmentListener> collabEnvListeners;
    private ArrayList<SynchronousAwarenessListener> syncAwarenessEnvListeners;
    private ArrayList<ImplicitRecommendationListener> impRecommendationListeners;

    /**
     * Constructor de la clase
     */
    public ListenerManager() {
        this.clientListeners = new ArrayList<>();
        this.messagesListeners = new ArrayList<>();
        this.searchListeners = new ArrayList<>();
        this.recommendationListeners = new ArrayList<>();
        this.collabEnvListeners = new ArrayList<>();
        this.syncAwarenessEnvListeners = new ArrayList<>();
        this.impRecommendationListeners = new ArrayList<>();
    }

    /**
     * Adiciona el todos los oyente soportados por DrakkarKeel 1.1 en la lista de oyentes registrados en la
     * aplicación cliente.
     *
     * @param l oyente
     *
     */
    public synchronized void addFacadeListener(FacadeDesktopListener l) {

        if (l instanceof CollaborativeEnvironmentListener && !collabEnvListeners.contains((CollaborativeEnvironmentListener) l)) {
            collabEnvListeners.add((CollaborativeEnvironmentListener) l);
        } else if (l instanceof ExplicitRecommendationListener && !recommendationListeners.contains((ExplicitRecommendationListener) l)) {
            recommendationListeners.add((ExplicitRecommendationListener) l);
        } else if (l instanceof SearchListener && !searchListeners.contains((SearchListener) l)) {
            searchListeners.add((SearchListener) l);
        } else if (l instanceof TextMessageListener && !messagesListeners.contains((TextMessageListener) l)) {
            messagesListeners.add((TextMessageListener) l);
        } else if (l instanceof ProwListener && !clientListeners.contains((ProwListener) l)) {
            clientListeners.add((ProwListener) l);
        } else if (l instanceof SynchronousAwarenessListener && !syncAwarenessEnvListeners.contains((SynchronousAwarenessListener) l)) {
            syncAwarenessEnvListeners.add((SynchronousAwarenessListener) l);
        } else if (l instanceof ImplicitRecommendationListener && !impRecommendationListeners.contains((ImplicitRecommendationListener) l)) {
            impRecommendationListeners.add((ImplicitRecommendationListener) l);
        } else {
            OutputMonitor.printLine("Unknown FacadeDesktopListener.", OutputMonitor.ERROR_MESSAGE);
        }

        OutputMonitor.printLine("Adding " + l.getClass().getSimpleName() + " .", OutputMonitor.INFORMATION_MESSAGE);
    }

    /**
     * Adiciona el todos los oyente soportados por DrakkarKeel 1.1 en la lista de oyentes registrados en la
     * aplicación cliente.
     *
     * @param ls lista de oyentes
     *
     *
     */
    public synchronized void addFacadeListeners(FacadeDesktopListener[] ls) {
        for (FacadeDesktopListener facadeDesktopListener : ls) {
            addFacadeListener(facadeDesktopListener);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente
     *
     *
     */
    public synchronized void addClientListener(ProwListener l) {
        if (!clientListeners.contains(l)) {
            clientListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de mensajes de texto
     */
    public synchronized void addTextMessageListener(TextMessageListener l) {
        if (!messagesListeners.contains(l)) {
            messagesListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de búsquedas
     *
     */
    public synchronized void addSearchListener(SearchListener l) {
        if (!searchListeners.contains(l)) {
            searchListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de recomendaciones explícitas
     *
     */
    public synchronized void addExplicitRecommendationListener(ExplicitRecommendationListener l) {
        if (!recommendationListeners.contains(l)) {
            recommendationListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente del entorno colaborativo
     *
     */
    public synchronized void addCollaborativeEnvironmentListener(CollaborativeEnvironmentListener l) {
        if (!collabEnvListeners.contains(l)) {
            collabEnvListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de awareness
     *
     */
    public synchronized void addSynchronousAwarenessListener(SynchronousAwarenessListener l) {
        if (!syncAwarenessEnvListeners.contains(l)) {
            syncAwarenessEnvListeners.add(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de recomendaciones implícitas
     *
     */
    public synchronized void addImplicitRecommendationListener(ImplicitRecommendationListener l) {
        if (!impRecommendationListeners.contains(l)) {
            impRecommendationListeners.add(l);
        }
    }

    /**
     * Adiciona el todos los oyente soportados por DrakkarKeel 1.1 en la lista de oyentes registrados en la
     * aplicación cliente.
     *
     * @param l oyente adaptador
     *
     */
    public synchronized void addFacadeListenerAdapter(FacadeListenerAdapter l) {
        if (!collabEnvListeners.contains(l)) {
            collabEnvListeners.add(l);
        }
        if (!recommendationListeners.contains(l)) {
            recommendationListeners.add(l);
        }
        if (!searchListeners.contains(l)) {
            searchListeners.add(l);
        }
        if (!messagesListeners.contains(l)) {
            messagesListeners.add(l);
        }
        if (!clientListeners.contains(l)) {
            clientListeners.add(l);
        }
        if (!syncAwarenessEnvListeners.contains(l)) {
            syncAwarenessEnvListeners.add(l);
        }
        if (!impRecommendationListeners.contains(l)) {
            impRecommendationListeners.add(l);
        }
    }

    /**
     * Elimina el todos los oyente soportados por DrakkarKeel 1.1 de la lista de oyentes registrados en la
     * aplicación cliente.
     *
     * @param l oyente adaptador
     *
     */
    public synchronized void removeFacadeListenerAdapter(FacadeListenerAdapter l) {
        if (!collabEnvListeners.contains(l)) {
            collabEnvListeners.remove(l);
        }
        if (!recommendationListeners.contains(l)) {
            recommendationListeners.remove(l);
        }
        if (!searchListeners.contains(l)) {
            searchListeners.remove(l);
        }
        if (!messagesListeners.contains(l)) {
            messagesListeners.remove(l);
        }
        if (!clientListeners.contains(l)) {
            clientListeners.remove(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente del cliente
     *
     *
     */
    public synchronized void removeClientListener(ProwListener l) {
        if (!clientListeners.contains(l)) {
            clientListeners.remove(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de mensajes de texto
     *
     *
     */
    public synchronized void removeTextMessageListener(TextMessageListener l) {
        if (!messagesListeners.contains(l)) {
            messagesListeners.remove(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de búsquedas
     *
     */
    public synchronized void removeSearchListener(SearchListener l) {
        if (!searchListeners.contains(l)) {
            searchListeners.remove(l);
        }
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de recomendaciones explícitas
     *
     */
    public synchronized void removeExplicitRecommendationListener(ExplicitRecommendationListener l) {
        if (!recommendationListeners.contains(l)) {
            recommendationListeners.remove(l);
        }
    }

    public FacadeDesktopListener[] getFacadeDesktopListener() {

        List<FacadeDesktopListener> listeners = new ArrayList<>();
        listeners.addAll(clientListeners);
        listeners.addAll(messagesListeners);
        listeners.addAll(searchListeners);
        listeners.addAll(recommendationListeners);
        listeners.addAll(collabEnvListeners);
        listeners.addAll(syncAwarenessEnvListeners);
        listeners.addAll(impRecommendationListeners);

        return listeners.toArray(new FacadeDesktopListener[listeners.size()]);
    }

    /**
     * Adiciona el oyente especificado de la lista de oyentes registrados en la
     * aplicación cliente
     *
     * @param l oyente de entorno colaboartivo
     *
     */
    public synchronized void removeCollaborativeEnvironmentListener(CollaborativeEnvironmentListener l) {
        if (!collabEnvListeners.contains(l)) {
            collabEnvListeners.remove(l);
        }
    }

    /**
     * Notifica los oyentes TextMessageListener registrado en la aplicación del cliente
     * el mensaje recibido
     *
     * @param evt evento de mensaje de texto
     */
    @SuppressWarnings("unchecked")
    public void dispatchTextMessage(TextMessageEvent evt) {

        ArrayList<TextMessageListener> v;

        synchronized (this) {
            v = (ArrayList<TextMessageListener>) messagesListeners.clone();
        }

        for (TextMessageListener client : v) {
            client.notifyTextMessage(evt);
        }
    }

    /**
     * Notifica los oyentes ExplicitRecommendationListener registrado en la aplicación del cliente
     * la recomendación explícita recibida
     *
     * @param evt evento de recomendación explícita
     */
    @SuppressWarnings("unchecked")
    public void dispatchExplicitRecommendation(ExplicitRecommendationEvent evt) {

        ArrayList<ExplicitRecommendationListener> v;

        synchronized (this) {
            v = (ArrayList<ExplicitRecommendationListener>) recommendationListeners.clone();
        }
        for (ExplicitRecommendationListener client : v) {
            client.notifyRecommendation(evt);
        }
    }

    /**
     * Notifica los oyentes SearchListener registrado en la aplicación del cliente
     * los resultados de búsqueda recibidos
     *
     * @param evt evento de búsqueda
     */
    @SuppressWarnings("unchecked")
    public void dispatchSearchResults(SearchEvent evt) {

        ArrayList<SearchListener> v;
        synchronized (this) {
            v = (ArrayList<SearchListener>) searchListeners.clone();
        }
        for (SearchListener client : v) {
            client.notifySearchResults(evt);
        }
    }

    /**
     * Notifica los oyentes ProwListener registrado en la aplicación del cliente
 el evento de cambio de estado del servidor recibido.
     *
     * @param evt evento de estado del servidor
     */
    @SuppressWarnings("unchecked")
    public void dispatchServerState(ServerEvent evt) {

        ArrayList<ProwListener> v;

        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }
        for (ProwListener client : v) {
            client.notifyServerState(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * el listado de sesiones colaborativas disponibles recibidas.
     *
     * @param evt evento de entorno colaborativo
     */
    @SuppressWarnings("unchecked")
    public void dispatchAvailableCollabSession(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyAvailableCollabSession(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * la creación de la sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativo
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabSessionCreation(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyCollabSessionCreation(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * el cambio del chairman de la sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativo
     */
    @SuppressWarnings("unchecked")
    public void dispatchChairmanSetting(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyChairmanSetting(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * la confirmación de eliminación de la sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativa
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabSessionDeleted(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyCollabSessionDeleted(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * la confirmación de finalización de la sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativa
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabSessionEnding(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyCollabSessionEnding(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * la confirmación de autenticación a la sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativa
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabSessionAuthentication(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyCollabSessionAuthentication(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * la confirmación de aceptación en una sesión colaborativa de búsqueda.
     *
     * @param evt evento de entorno colaborativa
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabSessionAcceptance(CollaborativeEnvironmentEvent evt) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyCollabSessionAcceptance(evt);
        }
    }

    /**
     * Notifica los oyentes CollaborativeEnvironmentListener registrado en la aplicación del cliente
     * los resultados de la búsqueda en historiales.
     *
     * @param evt evento de entorno colaborativa
     */
    @SuppressWarnings("unchecked")
    public void dispatchActionTrack(CollaborativeEnvironmentEvent l) {

        ArrayList<CollaborativeEnvironmentListener> v;

        synchronized (this) {
            v = (ArrayList<CollaborativeEnvironmentListener>) collabEnvListeners.clone();
        }

        for (CollaborativeEnvironmentListener client : v) {
            client.notifyActionTrack(l);
        }
    }

    /**
     * Notifica los oyentes SynchronousAwarenessEvent registrado en la aplicación del cliente
     * la activación de la técnica Putting Query Terms Together (PQT).
     *
     * @param evt evento de awareness
     */
    @SuppressWarnings("unchecked")
    public void dispatchPuttingQueryTermsTogether(SynchronousAwarenessEvent evt) {

        ArrayList<SynchronousAwarenessListener> v;

        synchronized (this) {
            v = (ArrayList<SynchronousAwarenessListener>) syncAwarenessEnvListeners.clone();
        }

        for (SynchronousAwarenessListener client : v) {
            client.notifyPuttingQueryTermsTogether(evt);
        }
    }

    /**
     * Notifica los oyentes SynchronousAwarenessEvent registrado en la aplicación del cliente
     * la el cambio de la consulta de un miembro de la sesión colaborativa de búsqueda.
     *
     * @param evt evento de awareness
     */
    @SuppressWarnings("unchecked")
    public void dispatchQueryChange(SynchronousAwarenessEvent l) {

        ArrayList<SynchronousAwarenessListener> v;

        synchronized (this) {
            v = (ArrayList<SynchronousAwarenessListener>) syncAwarenessEnvListeners.clone();
        }

        for (SynchronousAwarenessListener client : v) {
            client.notifyQueryChange(l);
        }
    }

    /**
     * Notifica los oyentes SynchronousAwarenessEvent registrado en la aplicación del cliente
     * que un miembro de la sesión colaborativa de búsqueda está reformulado su consulta.
     *
     * @param evt evento de awareness
     */
    @SuppressWarnings("unchecked")
    public void dispatchQueryTyped(SynchronousAwarenessEvent l) {

        ArrayList<SynchronousAwarenessListener> v;

        synchronized (this) {
            v = (ArrayList<SynchronousAwarenessListener>) syncAwarenessEnvListeners.clone();
        }

        for (SynchronousAwarenessListener client : v) {
            client.notifyQueryTyped(l);
        }
    }

    /**
     * Notifica los oyentes SynchronousAwarenessEvent registrado en la aplicación del cliente
     * la aceptación o no de un término de la consulta colaborativa por un miembro de la
     * sesión colaborativa de búsqueda está reformulado su consulta.
     *
     * @param evt evento de awareness
     */
    @SuppressWarnings("unchecked")
    public void dispatchQueryTermAcceptance(SynchronousAwarenessEvent l) {

        ArrayList<SynchronousAwarenessListener> v;

        synchronized (this) {
            v = (ArrayList<SynchronousAwarenessListener>) syncAwarenessEnvListeners.clone();
        }

        for (SynchronousAwarenessListener client : v) {
            client.notifyQueryTermAcceptance(l);
        }
    }

    /**
     * Notifica los oyentes ImplicitRecommendationEvent registrado en la aplicación del cliente
     * la recomendación de términos de consulta de forma colaborativa.
     *
     * @param evt evento de recomendación implícita
     */
    @SuppressWarnings("unchecked")
    public void dispatchCollabTermsRecommendation(ImplicitRecommendationEvent l) {

        ArrayList<ImplicitRecommendationListener> v;

        synchronized (this) {
            v = (ArrayList<ImplicitRecommendationListener>) impRecommendationListeners.clone();
        }

        for (ImplicitRecommendationListener client : v) {
            client.notifyCollabTermsRecommendation(l);
        }
    }

    /**
     * Notifica los oyentes ImplicitRecommendationEvent registrado en la aplicación del cliente
     * la recomendación de términos de consulta.
     *
     * @param evt evento de recomendación implícita
     */
    @SuppressWarnings("unchecked")
    public void dispatchQueryTermsRecommendation(ImplicitRecommendationEvent l) {

        ArrayList<ImplicitRecommendationListener> v;

        synchronized (this) {
            v = (ArrayList<ImplicitRecommendationListener>) impRecommendationListeners.clone();
        }

        for (ImplicitRecommendationListener client : v) {
            client.notifyQueryTermsRecommendation(l);
        }
    }

    /**
     * Notifica los oyentes TransactionEvent registrado en la aplicación del cliente
     * el resultado final de una transacción.
     *
     * @param evt evento de conclusión de transacción
     */
    @SuppressWarnings("unchecked")
    public void dispatchCommitTransaction(TransactionEvent l) {

        ArrayList<ProwListener> v;
        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }

        for (ProwListener client : v) {
            client.notifyCommitTransaction(l);
        }
    }

    /**
     * Notifica los oyentes SeekerEvent registrado en la aplicación del cliente
     * la acciones de los usuarios.
     *
     * @param evt evento de usuario
     */
    public void dispatchSeekerEvent(SeekerEvent l) {

        ArrayList<ProwListener> v;

        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }
        for (ProwListener client : v) {
            client.notifySeekerEvent(l);
        }
    }

    /**
     * Notifica los oyentes SearchEvent registrado en la aplicación del cliente
     * los buscadores disponibles en el servidor.
     *
     * @param evt evento de búsqueda
     */
    public void dispatchAvailableSearchers(SearchEvent l) {
        ArrayList<SearchListener> v;
        synchronized (this) {
            v = (ArrayList<SearchListener>) searchListeners.clone();
        }
        for (SearchListener client : v) {
            client.notifyAvailableSearchers(l);
        }
    }

    /**
     * Notifica los oyentes SearchEvent registrado en la aplicación del cliente
     * los principios de busquedas disponibles en el servidor.
     *
     * @param evt evento de búsqueda
     */
    public void dispatchAvailableSearchPrinciples(SearchEvent l) {
        ArrayList<SearchListener> v;
        synchronized (this) {
            v = (ArrayList<SearchListener>) searchListeners.clone();
        }
        for (SearchListener client : v) {
            client.notifyAvailableSearchPrinciples(l);
        }
    }

    /**
     * Notifica los oyentes SearchEvent registrado en la aplicación del cliente
     * los repositorios SVN disponibles en el servidor.
     *
     * @param evt evento de búsqueda
     */
    public void dispatchAvailableSVNRepositories(SearchEvent l) {
        ArrayList<SearchListener> v;
        synchronized (this) {
            v = (ArrayList<SearchListener>) searchListeners.clone();
        }
        for (SearchListener client : v) {
            client.notifyAvailableSVNRepositories(l);
        }
    }

    /**
     * Notifica los oyentes SeekerEvent registrado en la aplicación del cliente
     * al respuesta de la petición de conexión con el servidor.
     *
     * @param evt evento de usuario
     */
    public void dispatchRequestConnection(SeekerEvent l) {
        ArrayList<ProwListener> v;

        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }

        for (ProwListener client : v) {
            client.notifyRequestConnection(l);
        }

    }

    /**
     * Notifica los oyentes SeekerEvent registrado en la aplicación del cliente
     * la consfirmacion de cierre de la conexión con el servidor.
     *
     * @param evt evento de usuario
     */
    public void dispatchCloseConnection(SeekerEvent l) {
        ArrayList<ProwListener> v;

        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }

        for (ProwListener client : v) {
            client.notifyCloseConnection(l);


        }

    }

    /**
     * Notifica los oyentes FacadeDesktopEvent registrado en la aplicación del cliente
     * el evento lanzado.
     *
     * @param evt evento
     */
    public void dispatch(FacadeDesktopEvent l) {
        ArrayList<ProwListener> v;

        synchronized (this) {
            v = (ArrayList<ProwListener>) clientListeners.clone();
        }

        for (ProwListener client : v) {
            client.notify(l);
        }
    }
}
