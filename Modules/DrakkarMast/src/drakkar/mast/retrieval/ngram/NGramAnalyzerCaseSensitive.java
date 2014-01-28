/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */

package drakkar.mast.retrieval.ngram;

import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;

public class NGramAnalyzerCaseSensitive extends Analyzer {

    public NGramAnalyzerCaseSensitive() {
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream stream = new NGramTokenizer(reader, 1, 30);
        return stream;
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws
            IOException {
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


