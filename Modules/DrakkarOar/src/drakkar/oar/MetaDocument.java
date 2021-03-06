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
 * Esta clase tiene el objetivo de representar los campos de un documento indexado,
 * para su posterior visualización en la aplicación del cliente
 * 
 *
 * @deprecated As of DrakkarKeel version 1.1,
 * replaced by <code>DocumentMetaData</code>.
 *
 * @see drakkar.oar.DocumentMetaData
 *
 */
public class MetaDocument implements Comparable<MetaDocument>, Cloneable, Serializable {

    private static final long serialVersionUID = 70000000000008L;
    private String name;
    private String path;
    private float size;
    private String synthesis;
    private String type;
    private int index;//iddoc
    private double score;
    private int searcher;
    

    /**
     * Constructor por defecto
     */
    public MetaDocument() {
        this.name = null;
        this.path = null;
        this.size = 0;
        this.synthesis = null;
        this.type = null;
        this.index = 0;
        this.score = 0;
    }

    /**
     * Parametros de la Clase.
     *
     * @param name       nombre del documento
     * @param path       uri del documento
     * @param modified   última modificación del documento
     * @param size       tamaño del documento
     * @param synthesis  síntesis del documento
     * @param type       extensión del documento
     * @param index      índice del documento en la lista.
     * @param searcher   id del buscador
     * @param score      valor de relevancia
     */
    public MetaDocument(String name, String path, float size, String synthesis, String type, int index, double score, int searcher) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.synthesis = synthesis;
        this.type = type;
        this.index = index;
        this.score = score;
        this.searcher = searcher;
    }

    /**
     * Devuelve  el nombre del documento
     *
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Modifica el valor del nombre del documento.
     *
     * @param name  nombre del documento
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el URI del documento.
     *
     * @return uri
     */
    public String getPath() {
        return path;
    }

    /**
     * Modifica el valor URI del documento.
     *
     * @param path  nuevo uri
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Devuelve el tamaño del documento
     *
     * @return tamaño
     */
    public float getSize() {
        return size;
    }

    /**
     * Modifica el valor del tamaño del documento.
     *
     * @param size nuevo tamaño
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * Devuelve la síntesis del documento.
     *
     * @return síntesis
     */
    public String getSynthesis() {
        return synthesis;
    }

    /**
     * Modifica el valor de la síntesis del documento
     *
     * @param synthesis  nueva síntesis
     */
    public void setSynthesis(String synthesis) {
        this.synthesis = synthesis;
    }

    /**
     * Devuelve el índice del documento
     *
     * @return índice
     */
    public int getIndex() {
        return index;
    }

    /**
     * Modifica el valor del índice del documento
     *
     * @param index  nuevo índice
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Devuelve la extensión del documento
     *
     * @return extensión
     */
    public String getType() {
        return type;
    }

    /**
     * Modifica el valor de la extensión del documento
     *
     * @param type extensión
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Modifica el valor de relevancia del documento
     *
     * @param score relevancia
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Deveuelve el valor de relevancia del documento
     *
     * @return valor de relevancia
     */
    public double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaDocument other = (MetaDocument) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.path != null ? this.path.hashCode() : 0);

        return hash;
    }

    /**
     * Compara la igualdad de los MetaDocument mediante el valor de relevancia
     *
     * @param o metadocument
     *
     * @return 
     */
    @Override
    public int compareTo(MetaDocument o) {
        return Double.compare(score, o.getScore());
    }

    @Override
    public String toString() {
        return this.name + "," + this.path;
    }

    /**
     * @return the searcher
     */
    public int getSearcher() {
        return searcher;
    }

    /**
     * @param searcher the searcher to set
     */
    public void setSearcher(int searcher) {
        this.searcher = searcher;
    }
}
