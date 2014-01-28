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

import drakkar.oar.Seeker;
import drakkar.oar.exception.ChairmanNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionAlreadyRegisterException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.exception.SessionPropertyException;
import drakkar.oar.exception.UserStatusNotSupportedException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.slice.error.RequestException;
import java.io.IOException;
import java.util.List;

/**
 * The <code>Sessionable</code> class is....
 * Esta interfaz declara todas las operaciones soportadas por el framework DrakkarKeel
 * para el trabajo con sesiones
 */
public interface Sessionable {

    /**
     * Crea una sesión de búsqueda colaborativa
     *
     * @param newSessionName     nombre de la nueva sesión
     * @param seeker             usuario que invoca la operación
     * @param membersMinNumber   cantidad mínima de usuarios
     * @param membersMaxNumber   cantidad máxima de usuarios
     * @param integrityCriteria  criterio de integridad de la sesión
     * @param membershipPolicy   política de membresía de la sesión
     * @param seekerList         lista de usuario a integrar la sesión     
     * @param description        descripción de la sesión
     *
     * @return true si se pudo crear la sesión, en caso contrario lanzará algunas
     *         de las excepciones siguientes.
     *
     * @throws SeekerException 
     * @throws ChairmanNotExistException        si no existe un usuario con el role de Chairman
     * @throws SessionAlreadyRegisterException  si existe ya una sesión registrada
     *                                          con ese nombre
     * @throws SessionPropertyException         si especifica una propiedad de sesión
     *                                          no soprtada.
     */
    public boolean createCollabSession(String newSessionName, Seeker seeker, int membersMinNumber, int membersMaxNumber, int integrityCriteria, int membershipPolicy, List<Seeker> seekerList, String description) throws SeekerException, ChairmanNotExistException, SessionAlreadyRegisterException, SessionPropertyException;

    /**
     * Adiciona un usuario a una sesión
     *
     * @param sessionName    nombre de la sesión
     * @param seeker         usuario
     * @param seekerPrx      objeto proxy del usuario
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws IOException      si ocurre alguna excepción durante el proceso de
     *                          serialización del objeto Response
     */
    public void loginSeeker(String sessionName, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, IOException;

    /**
     * Solicita la unión de un usuario a una sesión de búsqueda colaborativa
     *
     * @param sessionName nombre de la sesión
     * @param seeker      usuario que emite la solicitud
     * @param seekerPrx   obejeto proxy del usuario
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws IOException      si ocurre alguna excepción durante el proceso de
     *                          serialización del objeto Response
     */
    public void joinCollabSession(String sessionName, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, IOException;

    /**
     * Solicita la entrada de un usuario a una sesión de búsqueda colaborativa
     * a la cual ya pertenece
     *
     * @param sessionName nombre de la sesión
     * @param seeker      usuario que emite la solicitud
     * @param seekerPrx   obejeto proxy del usuario
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws IOException      si ocurre alguna excepción durante el proceso de
     *                          serialización del objeto Response
     */
    public void enterCollabSession(String sessionName, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, IOException;

    /**
     * Actualiza el nuevo estado de un usuario a todos los usuarios de su sesión
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario de la actualización
     * @param status       nuevo estado del usuario
     *
     * @throws SessionException                si la sesión no se encuentra registrada
     *                                         en el servidor
     * @throws UserStatusNotSupportedException si nuevo estado no es soportado
     * @throws SeekerException
     * @throws IOException si ocurre alguna excepción durante el proceso de
     *                     serialización del objeto Response
     */
    public void updateStateSeeker(String sessionName, Seeker seeker, int status) throws SessionException, UserStatusNotSupportedException, SeekerException, IOException;

    /**
     * Actualiza el nuevo avatar de un usuario a todos los usuarios de su sesión
     *
     * @param sessionName nombre de la sesión
     * @param seeker      usuario de la actualización
     * @param avatar      nueva imagen
     *
     * @throws SessionException       si la sesión no se encuentra registrada
     *                                en el servidor
     * @throws SeekerException
     * @throws IOException            si ocurre alguna excepción durante el proceso de
     *                                serialización del objeto Response
     */
    public void updateAvatarSeeker(String sessionName, Seeker seeker, byte[] avatar) throws SessionException, SeekerException, IOException;

    /**
     * Elimina un usuario de una session.
     *
     * @param sessionName     nombre de la sesión
     * @param seeker          usuario a eliminar.
     *
     * @throws SessionException  si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void logoutSeeker(String sessionName, Seeker seeker) throws SessionException, SeekerException, IOException;

    /**
     * Elimina un usuario(Seeker) de una session colaborativa
     *
     * @param sessionName    nombre de la sesión
     * @param seeker         objeto seeker
     *
     * @throws SessionException  si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException   si el objeto seeker no se encuentra registrado
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void logoutCollabSession(String sessionName, Seeker seeker) throws SessionException, SeekerException, IOException;

    /**
     * Notifica la admisión de un usuario en una sesión de búsqueda colaborativa
     *
     * @param sessionName nombre de la sesión
     * @param chairman    usuario jefe de la sesión
     * @param seeker      usuario que solicitó la admisión
     *
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void notifyConfirmCollabSession(String sessionName, Seeker chairman, Seeker seeker) throws SessionException,  SeekerException, IOException;

    /**
     * Notifica la no admisión de un usuario en una sesión de búsqueda colaborativa
     *
     * @param sessionName    nombre de la sesión
     * @param chairman       usuario jefe de la sesión
     * @param seeker         usuario que solicitó la admisión
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException  si el usuario a registrar no existe ó el chairman no es el respectivo
     *                        chairman de dicha sesión
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void notifyDeclineCollabSession(String sessionName, Seeker chairman, Seeker seeker) throws SessionException,  SeekerException, IOException;

    /**
     * Finaliza la sesión colaborativa de búsqueda
     *
     * @param sessionName   nombre de la sesión
     * @param chairman      jefe de la sesión
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void finalizeCollabSession(String sessionName, Seeker chairman) throws SessionException,  IOException;

    /**
     * Devuelve la lista de nombres de las sesiones existentes en el servidor
     *
     * @return nombres de las sesiones
     */
    public String[] getSessionsNames();

    /**
     * Devuelve los usuarios registrados en la sesión especificada
     *
     * @param sessionName nombre de la sesión
     *
     * @return  lista de usuarios
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     */
    public java.util.List<Seeker> getOnlineMembers(String sessionName) throws SessionException;

    /**
     * Gestiona si el nombre de usuario se encuentra disponible
     *
     * @param userName nombre de usuario
     *
     * @return true de estar disponible el usuario, false en caso contrario
     *
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public boolean isAvailableUser(String userName) throws RequestException;

    /**
     * Solicita el registro de usuario en el servidor
     *
     * @param name      nombre
     * @param password  contraseña
     * @param description 
     * @param email
     * @param nickname  usuario
     * @param avatar    foto de usuario
     *
     */
    public void registerSeeker(String nickname, String name, String password, String description, String email, byte[] avatar);

    /**
     * Registra al usuario en la sesión de comunicación del servidor
     *
     * @param user      usuario
     * @param password  contraseña
     * @param clientPrx objeto proxy del cliente
     *
     * @return el objeto Seeker que representa al usuario, ó un objeto null de existir
     *         problemas con el usuario ó contraseña introducidos
     *
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public Seeker login(String user, String password, ClientSidePrx clientPrx) throws RequestException;

    /**
     * Recupera la contraseña del usurio especificado
     *
     * @param user usuario
     *
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public void recoverPassword(String user) throws RequestException;

    /**
     * Solicita el cambio de contraseña de un usuario
     *
     * @param user        usuario
     * @param oldPassword antigua contraseña
     * @param newPassword nueva contraseña
     *
     * @return true si se pudo cambiar la contraseña, false en caso contrario
     *
     * @throws RequestException si ocurre algún en el transcurso de la solicitud
     */
    public boolean changePassword(String user, String oldPassword, String newPassword) throws RequestException ;

    /**
     * Devuelve el total de sesiones registradas en el servidor
     *
     * @return total de sesiones
     */
    public int getSessionsCount();

    /**
     * Devuelve el total de usuarios registrados en el servidor
     *
     * @return total de usuarios
     */
    public int getSeekersCount();

    /**
     * Devuelve el total de usuarios registrados en la sesión especificada
     *
     * @param sessionName nombre de la sesión
     *
     * @return total de usuarios
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     */
    public int getSeekersCount(String sessionName) throws SessionException;


    /**
     *
     * @param sessionName
     * @param chairman
     * @param chairmanProxy
     * @throws SessionException
     * @throws SeekerException 
     */
    public void deleteCollabSession(String sessionName, Seeker chairman,ClientSidePrx chairmanProxy)throws SessionException, SeekerException;

   
    /**
     * 
     * @param sessionName
     * @param seeker
     * @param seekerProxy
     * @throws SessionException
     * @throws SeekerException
     */
    public void declineSeekerCollabSession(String sessionName, Seeker seeker, ClientSidePrx seekerProxy) throws SessionException, SeekerException;

}
