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

/**
 * Esta clase representa todas las propiedades de una sesión colaborativa de
 * búsqueda
 *
 */
public class SessionProperty implements Serializable{
    
    private static final long serialVersionUID = 70000000000020L;

    private String sessionName;
    private String chairman;
    private int membersMinNumber;
    private int membersMaxNumber;
    private int membersCurrentNumber;
    private int integrityCriteria;
    private int membershipPolicy;
    private String description;



    /**
     * Constructor por defecto de la clase
     */
    public SessionProperty() {
        this.sessionName = "";
        this.chairman = "";
        this.membersMinNumber = 0;
        this.membersMaxNumber = 0;
        this.membersCurrentNumber = 0;
        this.integrityCriteria = 0;
        this.membershipPolicy = 0;
    }

     /**
     * Constructor por defecto de la clase
      *
      * @param sessionName          nombre de la sesión
      * @param sessionDescription   descripción de la sesión
      */
    public SessionProperty(String sessionName, String sessionDescription)  {
        this.sessionName = sessionName;
        this.description = sessionDescription;
        this.chairman = "";
        this.membersMinNumber = 0;
        this.membersMaxNumber = 0;
        this.membersCurrentNumber = 0;
        this.integrityCriteria = 0;
        this.membershipPolicy = 0;
    }

    /**
     * Constructor de la clase
     *
     * @param sessionName          nombre de la sesión
     * @param chairman             jefe de la sesión
     * @param membersMinNumber     cantidad mínima de miembros
     * @param membersMaxNumber     cantidad maxima de miembros
     * @param membersCurrentNumber cantidad actual de miembros
     * @param integrityCriteria    criterio de integridad
     * @param membershipPolicy     política de membresía
     * @param description          descripción de la sesión
     */
    public SessionProperty(String sessionName, String chairman, int membersMinNumber, int membersMaxNumber, int membersCurrentNumber, int integrityCriteria, int membershipPolicy, String description) {
        this.sessionName = sessionName;
        this.chairman = chairman;
        this.membersMinNumber = membersMinNumber;
        this.membersMaxNumber = membersMaxNumber;
        this.membersCurrentNumber = membersCurrentNumber;
        this.integrityCriteria = integrityCriteria;
        this.membershipPolicy = membershipPolicy;
        this.description = description;
    }

    /**
     * Devuelve el nombre de jefe de la sesión
     *
     * @return nombre
     */
    public String getChairman() {
        return chairman;
    }

    /**
     * Modifica el nombre del jefe de la sesión
     *
     * @param chairman nombre
     */
    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    /**
     * Devuelve el criterio de integridad
     *
     * @return criterio
     */
    public int getIntegrityCriteria() {
        return integrityCriteria;
    }

    /**
     * Modifica el valor del criterio de integridad
     *
     * @param integrityCriteria criterio
     */
    public void setIntegrityCriteria(int integrityCriteria) {
        this.integrityCriteria = integrityCriteria;
    }

    /**
     * Devuelve la cantidad de miembros actuales
     *
     * @return miembros
     */
    public int getMembersCurrentNumber() {
        return membersCurrentNumber;
    }

    /**
     * Modifica la cantidad de miembros actuales de la sesión
     *
     * @param membersCurrentNumber miembros actuales
     */
    public void setMembersCurrentNumber(int membersCurrentNumber) {
        this.membersCurrentNumber = membersCurrentNumber;
    }

    /**
     * Devuelve el número máximo de miembros
     *
     * @return max
     */
    public int getMembersMaxNumber() {
        return membersMaxNumber;
    }

    /**
     * Modifica el valor del número máximo de miembros
     *
     * @param membersMaxNumber máximo de miembros
     */
    public void setMembersMaxNumber(int membersMaxNumber) {
        this.membersMaxNumber = membersMaxNumber;
    }

    /**
     * Devuelve el número minímo de miembros
     *
     * @return min
     */
    public int getMembersMinNumber() {
        return membersMinNumber;
    }

    /**
     * Modifica el valor del número minímo de miembros
     *
     * @param membersMinNumber minímo de miembros
     */
    public void setMembersMinNumber(int membersMinNumber) {
        this.membersMinNumber = membersMinNumber;
    }

    /**
     * Deveuelve la política de membresia
     *
     * @return política
     */
    public int getMembershipPolicy() {
        return membershipPolicy;
    }

    /**
     * Modifica la política de membresia
     *
     * @param membershipPolicy nueva política de membresia
     */
    public void setMembershipPolicy(int membershipPolicy) {
        this.membershipPolicy = membershipPolicy;
    }

    /**
     * Devuelve el nombre de la sesión
     *
     * @return nombre
     */
    public String getSessionName() {
        return sessionName;
    }

    /**
     * Modifica el nombre de la sesión
     *
     * @param sessionName nuevo nombre
     */
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
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


}
