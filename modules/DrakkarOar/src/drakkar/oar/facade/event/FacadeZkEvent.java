/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.facade.event;

import drakkar.oar.Response;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class FacadeZkEvent  extends Event{

    /**
     *
     */
    protected Response response;

    /**
     *
     * @param name
     * @param response
     */
    public FacadeZkEvent(String name,  Response response) {
        super(name);
        this.response = response;
    }

    
    /**
     *
     * @param name
     * @param target
     * @param data
     * @param response
     */
    public FacadeZkEvent(String name, Component target, Object data, Response response) {
        super(name, target, data);
        this.response = response;
    }

    /**
     *
     * @param name
     * @param target
     * @param response
     */
    public FacadeZkEvent(String name, Component target, Response response) {
        super(name, target);
        this.response = response;
    }

    /**
     *
     * @return
     */
    public Response getResponse() {
        return response;
    }

    /**
     *
     * @param response
     */
    public void setResponse(Response response) {
        this.response = response;
    }
}
