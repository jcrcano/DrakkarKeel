/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.DocumentMetaData;
import drakkar.mast.SearchException;
import java.util.ArrayList;

/**
 * Esta class implementada los métodos soportados por el servicio web de búsqueda
 * de Google
 *
 *
 */
public class GoogleContext implements  WebServiceContextable {

    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
