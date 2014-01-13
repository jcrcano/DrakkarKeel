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
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;

/**
 * Analizador case sensitive para indexar y buscar documentos con Lucene utilizando filtros para stopwords y stemming
 */
public class StopStemAnalyzerCaseSensitive extends Analyzer {

    Set<String> stopwords = getStopWordsList();
    private static final String[] ENGLISH_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it", "no", "not", "of",
        "on", "or", "such", "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with",
        "@author", "@param", "@return", "@deprecated", "@see", "@serial", "@since", "@serial",
        "@throws", "@code", "@link", "<i>", "</i>", "<pre>", "</pre>", "<blockquote>", "</blockquote>",
        "<br>", "</br>", "<b>", "</b>", "<tt>", "</tt>", "@linkplain", "<p>", "<a href=", "</a>",
        "public", "void", "private", "protected", "final", "asbtract", "class", "sttatic", "return"};

    /**
     *
     */
    public StopStemAnalyzerCaseSensitive() {
    }

    /**
     *
     * @param fieldName
     * @param reader
     * @return
     */
    public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream stream = new WhitespaceTokenizer(reader);
        stream = new StopFilter(true, stream, stopwords, true);
        stream = new PorterStemFilter(stream);

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
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws
            IOException {
        SavedStreams streams = (SavedStreams) getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            streams.source = new WhitespaceTokenizer(reader);
            streams.result = new StopFilter(true, streams.source, stopwords, true);
            streams.result = new PorterStemFilter(streams.source);

            setPreviousTokenStream(streams);
        } else {
            streams.source.reset(reader);
        }
        return streams.result;
    }

    //TODO FIX
    private Set<String> getStopWordsList() {

        Set<String> list = new HashSet<String>();

        for (int i = 0; i < ENGLISH_STOP_WORDS.length; i++) {
            String string = ENGLISH_STOP_WORDS[i];
            list.add(string);
        }

        return list;

    }

    /** Filters WhitespaceTokenizer with PorterStemFilter,StopFilter. */
    private class SavedStreams {

        Tokenizer source;
        TokenStream result;
    };
}
