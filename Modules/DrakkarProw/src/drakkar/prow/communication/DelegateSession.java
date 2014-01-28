/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.communication;

import drakkar.oar.Seeker;
import drakkar.oar.Delegate;
import drakkar.oar.Communication;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.RolePrx;
import drakkar.oar.slice.session.SearchSessionPrx;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.OutputMonitor;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase tiene el objetivo de crear un objeto proxy(RolePrx) para que actúe
 * como embajador, del respectivo objeto registrado en el servidor,pudiendo así
 * ejecutar las operaciones soportadas por el mismo.
 */
public class DelegateSession extends Delegate implements Serializable{
    private static final long serialVersionUID = 80000000000017L;

    private SearchSessionPrx sessionPrx;

    /**
     * Constructor vacio.
     */
    public DelegateSession() {
        super();
    }

    /**
     * Constructor de la Clase.
     *
     * @param comm       instancia de Communication.
     * @param name       nombre del servidor
     * @param ip         dirección host
     * @param porNumber  puerto por el cual el servidor recibe las peticiones.
     */
    public DelegateSession(Communication comm, String name, String ip, int porNumber) {
        super(comm, name, ip, porNumber);
    }

    public DelegateSession(Communication comm, String name, String ip, int porNumber, SearchSessionPrx sessionPrx) {
        super(comm, name, ip, porNumber, sessionPrx);
        this.sessionPrx = sessionPrx;

    }

    public DelegateSession(SearchSessionPrx sessionPrx) {
        this.sessionPrx = sessionPrx;
    }

    /**
     * Este método reemplaza el objeto SearchSessionPrx de la clase.
     *
     * @param sessionPrx  nuevo SearchSessionPrx.
     */
    public void setSearchSessionPrx(SearchSessionPrx sessionPrx) {
        this.sessionPrx = sessionPrx;
    }

    /**
     * Este método devuelve el objeto SearchSessionPrx de la clase.
     *
     * @return instancia de SearchSessionPrx.
     */
    public SearchSessionPrx getSearchSessionPrx() {
        return this.sessionPrx;
    }

    /**
     * Registrar al usuario en la sesión de comunicación del servidor
     *
     * @param user      usuario
     * @param password  contraseña
     *
     * @return el objeto Seeker que representa al usuario, ó un objeto null de existir
     *         problemas con el usuario ó contraseña introducidos
     * 
     * @throws NullPointerException si el objeto proxy de la sesión es nulo
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public Seeker login(String user, String password) throws NullPointerException, RequestException {

        Seeker seeker = null;
        if (sessionPrx == null) {
            throw new NullPointerException("The proxy is null");
        }
        try {

            Request request = new Request();
            request.put(OPERATION, LOGIN_SEEKER);
            request.put(SEEKER_NICKNAME, user);
            request.put(SEEKER_PASSWORD, password);
            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());

            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());

            if (response.getParameters().size() > 0) {
                // se obtiene el proxy RolePrx del objeto response
                seeker = (Seeker) response.get(SEEKER);
            }

            return seeker;

        } catch (ClassNotFoundException | IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    public int getSeekerID() throws RequestException {
        if (sessionPrx == null) {
            throw new RequestException("The proxy is null");
        }

        try {

            Request request = new Request();
            request.put(OPERATION, GET_SEEKER_ID);


            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());
            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());
            // se obtiene el proxy RolePrx del objeto response
            int id = (Integer) response.get(SEEKER_ID);

            return id;

        } catch (ClassNotFoundException | IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    public String getChairmanName(String sessionName) throws RequestException {
        if (sessionPrx == null) {
            throw new RequestException("The proxy is null");
        }

        try {

            Request request = new Request();
            request.put(OPERATION, GET_CHAIRMAN_NAME);
            request.put(SESSION_NAME, sessionName);


            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());
            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());
            // se obtiene el proxy RolePrx del objeto response
            String chairman = response.get(CHAIRMAN_NAME).toString();

            return chairman;

        } catch (ClassNotFoundException | IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    /**
     * Determina la disponibilidad del usuario seleccionado
     *
     * @param userName usuario
     *
     * @return true si el usuario está disponible, false en caso contrario
     *
     * @throws NullPointerException si el objeto proxy de la sesión es nulo
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public boolean isAvailableUser(String userName) throws RequestException {

        if (sessionPrx == null) {
            throw new RequestException("The proxy is null");
        }

        try {

            Request request = new Request();
            request.put(OPERATION, IS_AVAILABLE_USER);
            request.put(SEEKER_NICKNAME, userName);

            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());
            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());
            // se obtiene el proxy RolePrx del objeto response
            boolean answer = (Boolean) response.get(ANSWER);
            return answer;

        } catch (ClassNotFoundException | IOException ex) {
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Solicita el regitro de usuario en el base de datos del servidor de la aplicación
     *
     * @param name      nombre
     * @param lastName  apellidos
     * @param password  contraseña
     * @param userEmail correo
     * @param nickname  usuario
     * @param avatar    foto
     *
     * @throws NullPointerException si el objeto proxy de la sesión es nulo
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public void registerSeeker(String name, String description, String password, String userEmail, String nickname, byte[] avatar) throws NullPointerException, RequestException {
        if (sessionPrx == null) {
            throw new NullPointerException("The proxy is null");
        }

        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, REGISTER_SEEKER);
        hash.put(SEEKER_NAME, name);
        hash.put(SEEKER_DESCRIPTION, description);
        hash.put(SEEKER_PASSWORD, password);
        hash.put(SEEKER_EMAIL, userEmail);
        hash.put(SEEKER_NICKNAME, nickname);
        hash.put(SEEKER_AVATAR, avatar);
        Request request = new Request(hash);
        try {
            // se invoca la operación asíncronamente
            this.sessionPrx.sendAMID(request.toArray());
        } catch (IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    public void recoverPassword(String user) throws NullPointerException, RequestException {
        if (sessionPrx == null) {
            throw new NullPointerException("The proxy is null");
        }

        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, RECOVER_PASSWORD);
        hash.put(SEEKER_NICKNAME, user);

        Request request = new Request(hash);
        try {
            // se invoca la operación asíncronamente
            this.sessionPrx.sendAMID(request.toArray());
        } catch (IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    public boolean changePassword(String user, String oldPassword, String newPassword) throws NullPointerException, RequestException {
        if (sessionPrx == null) {
            throw new NullPointerException("The proxy is null");
        }

        try {

            Request request = new Request();
            request.put(OPERATION, CHANGE_PASSWORD);
            request.put(SEEKER_NICKNAME, user);
            request.put(OLD_PASSWORD, oldPassword);
            request.put(NEW_PASSWORD, newPassword);

            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());
            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());
            // se obtiene el proxy RolePrx del objeto response
            boolean answer = (Boolean) response.get(ANSWER);

            return answer;

        } catch (ClassNotFoundException | IOException ex) {
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Este método devuelve una instancia de DelegateRole.
     *
     * @return instancia de DelegateRole
     * @throws NullPointerException 
     */
    public DelegateRole getDlgRole() throws NullPointerException {
        try {
            // se crea una instancia del objeto response y se agrega la operación
            // a realizar: key=op, value= getRolePrx
            Request request = new Request();
            request.put(OPERATION, GET_ROLEPRX);

            // se invoca la operación síncronamente
            byte[] array = this.sessionPrx.getSAMI(request.toArray());
            // se deserializa el objeto Response devuelto por el método anterior

            Response response = Response.arrayToResponse(array, this.communication.getCommunicator());
            // se obtiene el proxy RolePrx del objeto response
            RolePrx rolePrx = (RolePrx) response.get(PROXY);
            if (rolePrx == null) {
                throw new NullPointerException("The proxy doesnt exist");
            }
            // se retorna el Delegado del Objeto Ice Role.
            DelegateRole role = new DelegateRole(rolePrx);
            return role;

        } catch (ClassNotFoundException | RequestException | IOException ex) {
            OutputMonitor.printStream("", ex);
        }

        return null;
    }
}
