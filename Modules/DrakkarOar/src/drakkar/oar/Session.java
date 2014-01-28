/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que contiene datos referentes a una sesión colaborativa de búsqueda
 */
public class Session implements Serializable {
    
    private static final long serialVersionUID = 70000000000019L;

    String topic;
    String description;
    private String chairman;

    /**
     *
     * @param topic          tema de la sesión
     * @param description    descripción de la sesión
     * @param chairman       jefe de la sesión
     */
    public Session(String topic, String description, String chairman) {
        this.topic = topic;
        this.description = description;
        this.chairman = chairman;
    }

    /**
     *
     */
    public Session() {
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     *
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the chairman
     */
    public String getChairman() {
        return chairman;
    }

    /**
     * @param chairman the chairman to set
     */
    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        Session session = (Session) obj;
        if (session.getTopic().equals(this.topic)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.topic);
        return hash;
    }

    @Override
    public String toString() {
        return this.topic;
    }
}
