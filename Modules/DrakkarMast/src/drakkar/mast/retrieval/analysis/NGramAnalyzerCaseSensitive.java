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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;

/**
 * Analizador case sensitive para indexar y buscar documentos con Lucene
 */
public class NGramAnalyzerCaseSensitive extends Analyzer {

    /**
     *
     */
    public NGramAnalyzerCaseSensitive() {
    }

    /**
     *
     * @param fieldName
     * @param reader
     * @return
     */
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream stream = new NGramTokenizer(reader, 1, 30);
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
        Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
        if (tokenizer == null) {
            tokenizer = new NGramTokenizer(reader, 1, 30);
            setPreviousTokenStream(tokenizer);
        } else {
            tokenizer.reset(reader);
        }
        return tokenizer;
    }
}
