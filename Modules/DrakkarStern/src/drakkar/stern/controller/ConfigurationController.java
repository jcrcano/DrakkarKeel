/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.controller;


import Ice.Current;
import drakkar.oar.Communication;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.exception.OperationNotExistException;
import drakkar.oar.slice.action.oneway.AMD_Send_sendAMID;
import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.util.KeyTransaction;
import drakkar.oar.util.SeekerAction;
import drakkar.stern.Stern;
import drakkar.stern.servant.ConfigurationServant;
import drakkar.stern.slice.config._ConfigurationDisp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ConfigurationController extends _ConfigurationDisp {

    ConfigurationServant servant;

    /**
     * Constructor de la clase.
     *
     * @param containers  lista de ContainerController.
     * @param comm        objeto Communnication.
     * @param server
     */
    public ConfigurationController(ArrayList<ContainerController> containers, Communication comm, Stern server) {
        this.servant = new ConfigurationServant(containers, comm, server);
    }

    /**
     * Este método permite la ejecución de una operación con despacho síncrono la
     * cual devuelve un objeto Response serializado.
     *
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @return  un objeto Response serializado con los resultados de la operación.
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public byte[] getSAMI(byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            byte[] array = this.getController(rqs);

            return array;

        } catch (IOException | ClassNotFoundException ex) {           
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb      - objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación junto con el objeto Response serializado
     *                  el cual contiene el resultado de la operación, e en caso
     *                  contrario lanzar una excepción del tipo RequestException.
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void getAMID_async(AMD_Get_getAMID cb, byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            byte[] array = this.getController(rqs);

            cb.ice_response(array);

        } catch (IOException | ClassNotFoundException ex) {
           
            cb.ice_exception(new RequestException(ex.getMessage()));
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho síncrono.
     *
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void sendSAMI(byte[] request, Current current) throws RequestException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb      - objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación ó lanzar una excepción en caso contrario
     *                  del tipo RequestException.
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void sendAMID_async(AMD_Send_sendAMID cb, byte[] request, Current current) throws RequestException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    


    /**
     * Este método traza un mapa entre la operaciones soportadas por la clase que
     * tienen valores de retorno y la invocada por el cliente, para determinar
     * cual efectuar. En caso de que cliente invoque una operación no implementada
     * este lanza una excepción del tipo OperationNotExistException.
     *
     * @param request - objeto que contiene la operación a realizar, con sus parámetros
     *                  de entrada.
     *
     * @return devuelve un objeto Response serializado.
     * @throws RequestException
     *
     */
    public byte[] getController(Request request) throws RequestException {
        boolean flag = false;
        try {
            Map<Object, Object> hash = request.getParameters();
            int operation = (Integer) hash.get(KeyTransaction.OPERATION);
            Response rsp = null;
            byte[] array = null;

            switch (operation) {

                case SeekerAction.GET_UUID_CONTAINER:
                    String uuid = servant.getUUIDContainer(request);
                    hash.clear();
                    hash.put(KeyTransaction.CONTAINER_UUID, uuid);
                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                case SeekerAction.REMOVE_NAME_SESSION:
                    flag = servant.removeNameSession(request);
                    hash.clear();
                    hash.put(KeyTransaction.RESULT, flag);
                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;
                case SeekerAction.REMOVE_ID_SESSION:
                    flag = servant.removeIdSession(request);
                    hash.clear();
                    hash.put(KeyTransaction.RESULT, flag);
                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;
                default:
                    throw new OperationNotExistException("Operation Not Exist");
            }
        } catch (IOException | OperationNotExistException ex) {
            throw new RequestException(ex.getMessage());
        }

    }
}
