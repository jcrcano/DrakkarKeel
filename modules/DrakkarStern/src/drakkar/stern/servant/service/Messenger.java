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

import drakkar.oar.Response;
import drakkar.oar.Seeker;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.Count;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>Tracker</code> class is....
 * Esta clase implementa todos los métodos de mensajería soportados por el
 * framework DrakkarKeel, además de ofrecer información sobre los mismos
 */
public class Messenger extends Service implements Sendable {

    /**
     * Esta variable representa el número de mensajes enviados durante la actual
     * sesión del servidor
     */
    private long messagesCount = 0;

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName 
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     */
    public Messenger(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param dbController
     */
    public Messenger(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, dbController);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     * @param listener             oyente de la aplicación servidora
     * @param dbController
     */
    public Messenger(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(String sessionName, Seeker emitter, String message) throws SessionException, SeekerException, IOException {

        SessionProfile recordSession = getSessionProfile(sessionName);

        SeekerInfo seekersRecord = recordSession.getSeekerInfo();
        boolean flag = seekersRecord.record.containsKey(emitter);

        if (flag) {
            int countMembers = seekersRecord.record.size();

            if (countMembers > 1) {

                Map<Seeker, ClientSidePrx> record = seekersRecord.record;
                @SuppressWarnings("unchecked")
                List<Seeker> receptors = new ArrayList<>(record.keySet());
                receptors.remove(emitter);

                Response response = this.buildResponse(sessionName, emitter, message);
                byte[] array = response.toArray(); // se serializa el objeto response

                ClientSidePrx receptorPrx;
                for (Seeker item : receptors) {
                    receptorPrx = record.get(item);
                    receptorPrx.notify_async(new NotifyAMICallback(item, "sendMessage"), array);
                    // save message to BD
                    saveMessage(sessionName, message, emitter, item);
              
                   notifySentMessage();
                }

                // se incrementa el valor de los mensajes enviados por emisor
                this.updateMessagesCount(sessionName, emitter);

            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }

    }

    private void notifySentMessage() {
        if (listener != null) {
            this.listener.notifySentMessage();
        }
    }

    /**
     * {@inheritDoc}
     * 
     */
    public void sendMessage(final String sessionName, final Seeker emitter, final Seeker receptor, final String message) throws SessionException, SeekerException, IOException {
        SessionProfile recordSession = getSessionProfile(sessionName);

        SeekerInfo seekersRecord = recordSession.getSeekerInfo();
        boolean flag = flag = seekersRecord.record.containsKey(emitter);
        if (flag) {
            // se solicita el objeto proxy correspondiente al miembro receptor,
            // si este devuelve un valor distinto de nulo, se realiza el envió del
            // mensaje y la notificación de conclusión de la operación al miembro
            // emisor, en caso contrario se notifica al emisor el error y se lanza
            // una excepción del tipo UserNotExistException.
            flag = seekersRecord.record.containsKey(receptor);

            if (flag) {

                Response response = this.buildResponse(sessionName, emitter, message);
                byte[] array = response.toArray(); // se serializa el objeto response

                // se notifica a cada miembro el mensaje apartir de su objeto proxy
                ClientSidePrx clientPrx = seekersRecord.record.get(receptor);
                clientPrx.notify_async(new NotifyAMICallback(receptor, "sendMessage"), array);
//
                // se incrementa el valor de los mensajes enviados por emisor
                this.updateMessagesCount(sessionName, emitter);
                notifySentMessage();

            } else {
                throw new SeekerException("The seeker '" + receptor.getUser() + "' is not registered in the session '" + sessionName + "'.");
            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }

        // save message to BD
        saveMessage(sessionName, message, emitter, receptor);
    }

    private void saveMessage(final String sessionName, final String message, final Seeker emitter, final Seeker receptor) {

        Thread thread = new Thread(
                new Runnable() {

                    public void run() {
                        try {

                            if (dbController != null && dbController.isOpen()) {
                                dbController.saveMessage(sessionName, emitter.getUser(), message, receptor);
                            }
                        } catch (SQLException ex) {
                            OutputMonitor.printStream("SQL", null);
                        }
                    }
                });

        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(final String sessionName, final Seeker emitter, final List<Seeker> receptors, final String message) throws SessionException, SeekerException, IOException {
        SessionProfile recordSession = getSessionProfile(sessionName);
        String notFound = null;

        SeekerInfo members = recordSession.getSeekerInfo();
        boolean flag = members.record.containsKey(emitter);

        if (flag) {

            Response response = this.buildResponse(sessionName, emitter, message);
            byte[] array = response.toArray();// se serializa el objeto response

            ClientSidePrx clientPrx;
            // se notifica a cada miembro el mensaje apartir de su objeto proxy
            for (Seeker seeker : receptors) {
                // se solicita el objeto proxy correspondiente al miembro receptor
                clientPrx = members.record.get(seeker);
                if (clientPrx != null) {
                    clientPrx.notify_async(new NotifyAMICallback(seeker, "sendMessage"), array);
                    // save message to BD
                    saveMessage(sessionName, message, emitter, seeker);
                } else {
                    notFound.concat(seeker.getUser()).concat(", ");
                }
            }
            // se incrementa el valor de los mensajes enviados por emisor
            this.updateMessagesCount(sessionName, emitter);
            notifySentMessage();


        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }


        // se comprueba si algunos de los receptores no pudo ser notificado
        if (notFound != null) {
            throw new SeekerException("The members '" + notFound + "' is not registered in the session '" + sessionName + "'.");
        }


    }

    private Response buildResponse(String sessionName, Seeker emitter, String message) {

        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, NOTIFY_TEXT_MESSAGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_EMITTER, emitter);
        hash.put(MESSAGE, message);
        Response response = new Response(hash);

        return response;
    }

    /**
     * Este método se emplea para actualizar la relación de mensajes enviados
     * por los miembros de un sesión y el total general de la misma.
     *
     * @param sessionName nombre de la sesión a la que pertenece el miembro.
     * @param seeker      miembro que ejecuta la búsqueda.
     */
    private void updateMessagesCount(String sessionName, Seeker seeker) throws SessionException {
        SessionProfile recordSession = getSessionProfile(sessionName);

        int count = 0;
        //se obtiene la relación de mensajes enviados por los
        // miembros de la sesión.
        Count membMsgsCount = recordSession.getSeekersMessages();
        boolean flag = !membMsgsCount.record.isEmpty();
        if (flag) {
            try {
                // se obtiene e incrementa el número de mensajes del miembro
                count = membMsgsCount.record.get(seeker);
                count++;
                // si el miembro no se encuentra registrado, se procede
                // a realizar su registro al capturar la expeción.
            } catch (NullPointerException e) {
                membMsgsCount.record.put(seeker, 1);
            }

        } else {
            membMsgsCount.record.put(seeker, 1);
        }

        // se incrementa el valor de los mensajes enviados durante la
        // actual sesión del servidor.
        this.messagesCount++;
        // se incrementa el valor de los mensajes enviados por la
        // la sesión durante actual sesión del servidor.
        count = recordSession.getMessagesCount();
        count++;

    }

    /**
     * {@inheritDoc}
     */
    public long getMessagesCount() {
        return this.messagesCount;
    }

    /**
     * {@inheritDoc}
     */
    public long getMessagesCount(String sessionName) throws SessionException {
        SessionProfile recordSession = getSessionProfile(sessionName);
        int count = recordSession.getMessagesCount();
        return count;

    }

    /**
     * {@inheritDoc}
     */
    public long getMessagesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException {

        SessionProfile recordSession = getSessionProfile(sessionName);
        Count members = recordSession.getSeekersMessages();
        boolean flag = members.record.containsKey(seeker);
        if (flag) {
            int count = members.record.get(seeker);
            return count;
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }


    }
}
