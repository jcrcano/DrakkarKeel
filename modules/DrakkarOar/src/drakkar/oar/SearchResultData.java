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
import java.util.Date;

/**
 * Esta clase contiene los datos de un resultado de búsqueda
 *
 *
 *
 */
public class SearchResultData implements Serializable {

    private static final long serialVersionUID = 70000000000015L;

    private int id;
    private String uri;
    private int index;
    private float size;
    private String name;
    private boolean review;
    private double score;
    private String type;
    private String summary;
    private String session;
    private String lastModified;
    private String author;
    int searcher;

    /**
     * Constructor de la clase
     *
     * @param id id del documento
     * @param uri dirección del documento
     * @param index posición que corresponde al documento en el índice
     * @param size tamaño del documento
     * @param name nombre del documento
     * @param review true si el documento fue revisado, false si no
     * @param score puntuación dada al documento por el buscador de acuerdo a la
     * consulta realizada
     * @param type tipo de documento
     * @param session nombre de la sesión donde se realizó la búsqueda
     * @param summary sumarización del documento
     * @param searcher id buscador que recuperó el documento
     */
    public SearchResultData(int id, String uri, int index, float size, String name, boolean review, double score, String type, String session, String summary, int searcher, String author, String lastModified) {

        this.id = id;
        this.uri = uri;
        this.index = index;
        this.size = size;
        this.name = name;
        this.review = review;
        this.score = score;
        this.type = type;
        this.session = session;
        this.summary = summary;
        this.searcher = searcher;
        this.lastModified = lastModified;
        this.author = author;
    }

    /**
     *
     * @param metaDoc
     * @param review
     * @param session
     */
    public SearchResultData(DocumentMetaData metaDoc, boolean review, String session) {

        this.id = metaDoc.getIndex();
        this.uri = metaDoc.getPath();
        this.index = this.id;
        this.size = metaDoc.getSize();
        this.name = metaDoc.getName();
        this.review = review;
        this.score = metaDoc.getScore();
        this.type = metaDoc.getType();
        this.session = session;
        this.summary = metaDoc.getSynthesis();
        this.searcher = metaDoc.getSearcher();
        this.lastModified = metaDoc.getLastModified();
        this.author = metaDoc.getAuthor();
    }

    /**
     * @return the url
     */
    public String getURI() {
        return uri;
    }

    /**
     * @param uri the url to set
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the review
     */
    public boolean isReview() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(boolean review) {
        this.review = review;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the session
     */
    public String getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    
}
