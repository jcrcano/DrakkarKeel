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
import java.util.*;

/**
 * Clase que contiene la relación de consultas por cada usuario
 */
public class SeekerQuery implements Serializable {

    private static final long serialVersionUID = 70000000000018L;
    Map<String, List<String>> map = new HashMap<>();

    /**
     *
     * @param map
     */
    public SeekerQuery(Map<String, List<String>> hash) {
        this.map = hash;
    }

    /**
     *
     */
    public SeekerQuery() {
    }

    /**
     *
     * @return @deprecated use getValues
     */
    public Map<String, List<String>> getHash() {
        return map;
    }

    /**
     *
     * @param map
     * @deprecated use setValues
     */
    public void setHash(Map<String, List<String>> hash) {
        this.map = hash;
    }

    /**
     *
     * @return
     */
    public Map<String, List<String>> getValues() {
        return map;
    }

    /**
     *
     * @param map
     */
    public void setValues(Map<String, List<String>> hash) {
        this.map = hash;
    }

    /**
     *
     * @param seeker
     * @return
     */
    public List<String> getQueries(String seeker) {
        return this.map.get(seeker);
    }

    /**
     * Devuelve todas las consultas emitidas por los usuarios.
     *
     * Nota: No devuelve consultas repetidas
     *
     * @return lista de consultas
     */
    public List<String> getQueries() {
        Set<String> temp = new HashSet<>();
        for (List<String> list : map.values()) {
            temp.addAll(list);
        }
        return new ArrayList<>(temp);
    }

    /**
     * Devuelve todas los usuarios que emitieron la consulta especificada
     *
     * @param query
     * @return
     */
    public List<String> getSeekers(String query) {
        List<String> list = new ArrayList<>();
        for (String seeker : map.keySet()) {
            if (getQueries(seeker).contains(query)) {
                list.add(seeker);
            }
        }
        return list;
    }

    /**
     * Devuelve todas los usuarios
     *
     * @return
     */
    public List<String> getSeekers() {        
        return new ArrayList<>(map.keySet());
    }

    /**
     *
     * @param seeker
     * @param list
     */
    public void setQueries(String seeker, List<String> list) {
        this.map.remove(seeker);
        this.map.put(seeker, list);
    }

    /**
     *
     * @param seeker
     * @param list
     */
    public void add(String seeker, List<String> list) {
        this.map.put(seeker, list);
    }
}
