/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval.analysis;

import java.io.IOException;

/**
 * Analizador para indexar y buscar en documentos XML de la colección de Wikipedia con Lucene utilizando filtros para stopwords
 */
public class WikiCaseSensitiveAnalyzer extends DefaultCaseSensitiveAnalyzer {

    @Override
    public void loadDefaultStopWords() throws IOException { 
        super.loadDefaultStopWords();
        load("conf/wiki_stop_words.txt");      
    }
}
