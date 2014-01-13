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
 * Clase que contiene los objetos a enviar al cliente para el historial de búsqueda
 * 
 */
public class SearchTracker implements Serializable{
    
     private static final long serialVersionUID = 70000000000016L;

    private SearchResultData result;
    private List<MarkupData> evaluation;

    /**
     * Constructor de la clase
     *
     * @param result       objeto que contiene los datos referentes al documento
     * @param evaluation   evaluaciones realizadas al documento
     */
    public SearchTracker(SearchResultData result, List<MarkupData> evaluation) {
        this.evaluation = evaluation;
        this.result = result;
    }

    /**
     * @return the result
     */
    public SearchResultData getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(SearchResultData result) {
        this.result = result;
    }

    /**
     * @return the evaluation
     */
    public List<MarkupData> getEvaluation() {
        return evaluation;
    }

    /**
     * @param evaluation the evaluation to set
     */
    public void setEvaluation(List<MarkupData> evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return this.result.getName() +","+ this.result.getURI();
    }



}
