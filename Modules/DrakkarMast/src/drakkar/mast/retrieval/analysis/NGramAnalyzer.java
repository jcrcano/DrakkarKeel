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
import java.io.Reader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ngram.NGramTokenizer;

/**
 * Analizador para indexar y buscar documentos con Lucene utilizando NGramTokenizer
 */
public class NGramAnalyzer extends Analyzer {

    /**
     *
     */
    public NGramAnalyzer() {
    }

    /**
     *
     * @param fieldName
     * @param reader
     * @return
     */
    public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream stream = new NGramTokenizer(reader, 1, 30);
        stream = new LowerCaseFilter(stream);
        
        return stream;
    }

    /**
     *
     * @param fieldName
     * @param reader
     * @return
     * @throws IOException
     */
    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        SavedStreams streams = (SavedStreams) getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            streams.source = new NGramTokenizer(reader, 1, 30);
            streams.result = new LowerCaseFilter(streams.source);
            setPreviousTokenStream(streams);
        } else {
            streams.source.reset(reader);
        }
        return streams.result;
    }

    /** Filters NGramTokenizer with LowerCaseFilter. */
    private class SavedStreams {

        Tokenizer source;
        TokenStream result;
    };
}
