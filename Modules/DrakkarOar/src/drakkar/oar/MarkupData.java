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
 * Esta clase contiene los datos de las evaluaciones realizadas a un documento
 *
 * 
 *
 */
public class MarkupData implements Serializable{
    
    private static final long serialVersionUID = 70000000000007L;

    private String comment;
    private byte relevance;
    private String user;
    private String docUri;
    private String date;
    
   
    /**
     * Constructor de la clase
     *
     * @param comment      comentario sobre el documento
     * @param relevance    valor de relevancia otorgado por el usuario
     * @param user         usuario que realizó la evaluación
     * @param uri          dirección del documento
     * @param date         fecha de la evaluación
     */
    public MarkupData(String comment, byte relevance, String user, String uri, String date) {
        this.comment = comment;
        this.relevance = relevance;
        this.user = user;
        this.docUri = uri;
        this.date = date;
        
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the relevance
     */
    public byte getRelevance() {
        return relevance;
    }

    /**
     * @param relevance the relevance to set
     */
    public void setRelevance(byte relevance) {
        this.relevance = relevance;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the docUri
     */
    public String getDocUri() {
        return docUri;
    }

    /**
     * @param docUri the docUri to set
     */
    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    @Override
    public String toString() {
        return this.user +","+this.relevance +","+this.date;
    }

 
}
