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
import java.util.List;

/**
 * Esta clase contiene los datos de las recomendaciones realizadas en una sesión
 *
 *
 */
public class RecommendTracker implements Serializable{
    
      private static final long serialVersionUID = 70000000000009L;

    private List<String> receptors;
    private String seeker;
    private String date;
    private String comment;
    private String uri;
    private int idDoc;//en la BD o cache
    private String name;//nombre del documento
    private String query;
    private int searcher;


    /**
     * Constructor de la clase
     *
     * @param receptors     lista de usuarios a quien está dirigida la recomendación
     * @param seeker        usuario que envia la recomendación
     * @param date          fecha de la recomendación
     * @param comment       comentario de la recomendación
     * @param uri           dirección del documento
     * @param idDoc         id del documento
     * @param query         consulta a que corresponde el documento como resultado de una búsqueda
     * @param searcher      id del buscador utilizado
     */
    public RecommendTracker(List<String> receptors, String seeker, String date, String comment, String uri, int idDoc, String query, int searcher) {

        this.receptors = receptors;
        this.seeker = seeker;
        this.date = date;
        this.comment = comment;
        this.uri = uri;
        this.idDoc = idDoc;
        this.searcher = searcher;
        this.query = query;

        String separator = System.getProperty("file.separator");
        int pos = uri.lastIndexOf(separator);
        this.name = uri.substring(pos+1);

    }


    /**
     * @return the receptors
     */
    public List<String> getReceptors() {
        return receptors;
    }

    /**
     * @param receptors the receptors to set
     */
    public void setReceptors(List<String> receptors) {
        this.receptors = receptors;
    }

    /**
     * @return the seeker
     */
    public String getSeeker() {
        return seeker;
    }

    /**
     * @param seeker the seeker to set
     */
    public void setSeeker(String seeker) {
        this.seeker = seeker;
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
     * @return the text
     */
    public String getText() {
        return comment;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.comment = text;
    }

    /**
     * @return the idDoc
     */
    public int getIdDoc() {
        return idDoc;
    }

    /**
     * @param idDoc the idDoc to set
     */
    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    @Override
    public String toString() {
        return this.name +","+this.uri;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
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
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return
     */
    public int getSearcher() {
        return searcher;
    }

    /**
     *
     * @param searcher
     */
    public void setSearcher(int searcher) {
        this.searcher = searcher;
    }

    /**
     *
     * @return
     */
    public String getURI() {
        return uri;
    }

    /**
     * 
     * @param uri
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    
}
