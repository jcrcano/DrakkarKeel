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
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.slice.action.oneway.AMD_Send_sendAMID;
import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.management._ManagerDisp;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.stern.servant.ManagerServant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ManagerController extends _ManagerDisp {

    public ManagerServant servant;

    public ManagerController(ArrayList<ContainerController> containersList) {
        this.servant = new ManagerServant(containersList);
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
//        try {
//
//            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
//            this.sendController(rqs);
//
//        } catch (IOException ex) {
//           
//        } catch (ClassNotFoundException ex) {
//           
//        }
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
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            this.sendController(rqs);
            cb.ice_response();

        } catch (IOException | ClassNotFoundException ex) {

            cb.ice_exception(new RequestException(ex.getMessage()));
        }

    }
    String name, password = null;

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
     *
     * @throws RequestException
     */
    public byte[] getController(Request request) throws RequestException {
        boolean flag = false;
        try {
            Map<Object, Object> hashRqs = request.getParameters();
            int operation = (Integer) hashRqs.get(OPERATION);
            Response rsp = null;
            byte[] array = null;
            switch (operation) {
                case SEEKER_LOGIN:
                    name = hashRqs.get(MANAGER_NAME).toString();
                    password = hashRqs.get(MANAGER_PASSWORD).toString();

                    flag = this.servant.login(name, password);
                    break;

                case GET_MANAGER_NAME:
                    name = this.servant.getName();
                    hashRqs.clear();
                    hashRqs.put(OPERATION, GET_MANAGER_NAME);
                    hashRqs.put(MANAGER_NAME, name);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;

                case GET_MANAGER_PASSWORD:
                    password = this.servant.getPassword();
                    hashRqs.clear();
                    hashRqs.put(OPERATION, GET_MANAGER_PASSWORD);
                    hashRqs.put(MANAGER_PASSWORD, password);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;
                case GET_CONTAINERS:
                    ArrayList<ContainerController> containers = this.servant.getContainersList();
                    hashRqs.clear();
                    hashRqs.put(OPERATION, GET_CONTAINERS);
                    hashRqs.put(CONTAINERS, containers);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;

                default:

                    throw new RequestException("The operation is not supported");

            }

            return null;

        } catch (IOException ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    /**
     * Este método traza un mapa entre la operaciones soportadas por la clase que
     * no retornan nigún parámetro y la invocada por el cliente, para determinar
     * cual efectuar. En caso de que cliente invoque una operación no implementada
     * este lanza una excepción del tipo OperationNotExistException.
     *
     * @param request - objeto que contiene la operación a realizar, con sus parámetros
     *                  de entrada.
     *
     *
     * @throws RequestException
     */
    public void sendController(Request request) throws RequestException {

        Map<Object, Object> hashRqs = request.getParameters();
        int operation = (Integer) hashRqs.get(OPERATION);

        switch (operation) {

            case SET_MANAGER_NAME:
                name = hashRqs.get(MANAGER_NAME).toString();
                this.servant.setName(name);

                break;

            case SET_MANAGER_PASSWORD:
                password = hashRqs.get(MANAGER_PASSWORD).toString();
                this.servant.setPassword(password);

                break;

            case SET_CONTAINERS:
                @SuppressWarnings("unchecked") ArrayList<ContainerController> containers = (ArrayList<ContainerController>) hashRqs.get(CONTAINERS);
                break;

            default:

                throw new RequestException("The operation is not supported");
        }
    }
}
