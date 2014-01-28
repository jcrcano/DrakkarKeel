/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant;

import Ice.Current;
import drakkar.oar.Communication;
import drakkar.oar.slice.management.ManagerPrx;
import drakkar.oar.slice.management._ServerManagerDisp;
import drakkar.stern.controller.ContainerController;
import java.util.ArrayList;


public class SternManagerServant extends _ServerManagerDisp {

    private String name;
    private String description;
    private ArrayList<ContainerController> containersList;
    private Communication comm;

    /**
     *
     * @param comm
     */
    public SternManagerServant(Communication comm) {
        this.name = "";
        this.description = "";
        this.containersList = new ArrayList<>();
        this.comm = comm;
    }

    /**
     *
     * @param comm
     * @param name
     * @param description
     */
    public SternManagerServant(Communication comm, String name, String description) {
        this.comm = comm;
        this.name = name;
        this.description = description;
        this.containersList = new ArrayList<>();

    }

    /**
     *
     * @return
     */
    public ArrayList<ContainerController>  getContainers() {
        return this.containersList;
    }

    /**
     *
     * @param controller
     */
    public void add(ContainerController controller) {
       this.containersList.add(controller);
    }
    /**
     *
     * @param controller
     * @return
     */
    public boolean remove(ContainerController controller) {
        return this.containersList.remove(controller);
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param descrip
     */
    public void setDescription(String descrip) {
        this.description = descrip;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     *
     * @param current
     * @return
     */
    public ManagerPrx getManager(Current current) {
//        Manager manager =  new ManagerController(this.containersList);
//        Ice.ObjectPrx adminPrx = this.comm.getAdapter().addWithUUID(manager);
//
//        return ManagerPrxHelper.uncheckedCast(adminPrx);
        return null;
    }
}
