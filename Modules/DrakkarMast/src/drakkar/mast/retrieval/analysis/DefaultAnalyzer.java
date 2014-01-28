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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Analizador para indexar y buscar en documentos XML de la colección de Wikipedia con Lucene utilizando filtros para stopwords
 */
public class DefaultAnalyzer extends Analyzer {
    private static final long serialVersionUID = 1879843534577L;
    /**
     *
     */
    protected Language lang;
    /**
     *
     */
    protected Set<String> stopWords;

    /**
     *
     */
    public DefaultAnalyzer() {
        this.lang = Language.EN;
        this.stopWords = new HashSet<String>();
    }

    /**
     *
     * @param lang
     */
    public DefaultAnalyzer(Language lang) {
        this.lang = lang;
        this.stopWords = new HashSet<String>();
    }
    
    /**
     *
     * @param fieldName
     * @param reader
     * @return
     */
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream stream = new LowerCaseTokenizer(reader);
        stream = new StopFilter(true, stream, stopWords, true);
//        stream = new PorterStemFilter(stream);
      
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
            streams.source = new LowerCaseTokenizer(reader);
            streams.result = new StopFilter(true, streams.source, stopWords, true);
//            streams.result = new PorterStemFilter(streams.source);
           
            setPreviousTokenStream(streams);
        } else {
            streams.source.reset(reader);
        }
        return streams.result;
    }
    
    /** Filters LowerCaseTokenizer with StopFilter,PorterStemFilter */
    protected class SavedStreams {
        Tokenizer source;
        TokenStream result;
    };

    /**
     *
     * @throws IOException 
     */
    public void loadDefaultStopWords() throws IOException {        
        load("conf/" + lang + "_stop_words.txt");      
    }

    /**
     *
     * @param lang
     * 
     * @throws IOException  
     */
    public void loadDefaultStopWords(Language lang) throws IOException {
        setLanguage(lang);
        loadDefaultStopWords();
    }
    
    /**
     *
     * @param filePath
     * @throws IOException  
     */
    public void loadStopWords(String filePath) throws IOException {
         load(filePath);    
    }
    
    /**
     *
     * @param filePath
     * @throws IOException
     */
    protected void load(String filePath) throws IOException{
        BufferedReader bf = null;
        try {
            // TODO to update to JDK 7
            bf = new BufferedReader(new FileReader(filePath));
            String cad = bf.readLine();
            while (cad != null) {
                StringTokenizer token = new StringTokenizer(cad, ",");
                while (token.hasMoreTokens()) {
                    stopWords.add(token.nextToken());
                }
                cad = bf.readLine();
            }
        }finally{
            try {
                bf.close();
            } catch (IOException ex) {
                Logger.getLogger(DefaultAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * @return the Language
     */
    public Language getLanguage() {
        return lang;
    }

    /**
     * @param lang the language to set
     */
    public void setLanguage(Language lang) {
        this.lang = lang;
    }

    /**
     * @return the stop words
     */
    public Set<String> getStopWords() {
        return Collections.unmodifiableSet(stopWords);
    }

    /**
     * @param stopWords the stop words to set
     */
    public void setStopords(Set<String> stopWords) {
        this.stopWords.clear();
        this.stopWords.addAll(stopWords);
    }
}
