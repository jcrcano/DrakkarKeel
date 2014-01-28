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
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ngram.NGramTokenizer;

public class NGramAnalyzer extends Analyzer {

    Set<String> stopwords = getStopWordsList();
    
    private static final String[] ENGLISH_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it", "no", "not", "of",
        "on", "or", "such", "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with"
    };

    public NGramAnalyzer() {
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream stream = new NGramTokenizer(reader, 1, 30);
        stream = new LowerCaseFilter(stream);
        stream = new PorterStemFilter(stream);
        stream = new StopFilter(false, stream, stopwords, true);

        return stream;
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws
            IOException {
        SavedStreams streams = (SavedStreams) getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            streams.source = new NGramTokenizer(reader, 1, 30);
            streams.result = new LowerCaseFilter(streams.source);
            streams.result = new PorterStemFilter(streams.source);
            streams.result = new StopFilter(false, streams.source, stopwords, true);

            setPreviousTokenStream(streams);
        } else {
            streams.source.reset(reader);
        }
        return streams.result;
    }

    private Set<String> getStopWordsList() {

        Set<String> list = new HashSet<String>();

        for (int i = 0; i < ENGLISH_STOP_WORDS.length; i++) {
            String string = ENGLISH_STOP_WORDS[i];
            list.add(string);
        }

        return list;

    }

    /** Filters NGramCaseTokenizer with LowerCaseFilter,PorterStemFilter,StopFilter. */
    private class SavedStreams {

        Tokenizer source;
        TokenStream result;
    };
}


