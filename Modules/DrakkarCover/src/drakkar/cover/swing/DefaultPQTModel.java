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

import drakkar.oar.Seeker;
import drakkar.cover.swing.facade.PQTFacade;
import java.util.ArrayList;
import java.util.List;

public class DefaultPQTModel {

    private String sessionName;
    private Seeker seeker;
    private List<Seeker> seekerList;
    private PQTFacade facade;

    /**
     *
     */
    public DefaultPQTModel() {
        this.sessionName = "";
        this.seeker = new Seeker();
        this.seekerList = new ArrayList<>();
        this.facade = null;

    }

    /**
     *
     * @param seeker
     * @param facade
     */
    public DefaultPQTModel(Seeker seeker, PQTFacade facade) {
        this.sessionName = "";
        this.seeker = seeker;
        this.seekerList = new ArrayList<>();
        this.facade = facade;
    }

    /**
     *
     * @param sessionName
     * @param seeker
     * @param seekerList
     * @param facade
     */
    public DefaultPQTModel(String sessionName, Seeker seeker, List<Seeker> seekerList, PQTFacade facade) {
        this.sessionName = sessionName;
        this.seeker = seeker;
        this.seekerList = seekerList;
        this.facade = facade;
    }

    /**
     *
     * @return
     */
    public PQTFacade getMenuPQTFacade() {
        return facade;
    }

    /**
     *
     * @param facade
     */
    public void setMenuPQTFacade(PQTFacade facade) {
        this.facade = facade;
    }

    /**
     *
     * @return
     */
    public Seeker getSeeker() {
        return seeker;
    }

    /**
     *
     * @param seeker
     */
    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    /**
     *
     * @return
     */
    public List<Seeker> getSeekerList() {
        return seekerList;
    }

    /**
     *
     * @param seekerList
     */
    public void setSeekerList(List<Seeker> seekerList) {
        this.seekerList = seekerList;
    }

    /**
     *
     * @return
     */
    public String getSessionName() {
        return sessionName;
    }

    /**
     *
     * @param sessionName
     */
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
