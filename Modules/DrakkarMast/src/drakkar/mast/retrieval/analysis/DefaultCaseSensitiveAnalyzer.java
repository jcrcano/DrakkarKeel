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
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;

public class DefaultCaseSensitiveAnalyzer extends DefaultAnalyzer {
    private static final long serialVersionUID = 7721511321313L;    
    
    /**
     *
     * @param fieldName
     * @param reader
     * @return
     */
    @Override
      public TokenStream tokenStream(String fieldName, Reader reader) {

        TokenStream stream = new WhitespaceTokenizer(reader);
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
            streams.source = new WhitespaceTokenizer(reader);
            streams.result = new StopFilter(true, streams.source, stopWords, true);
//            streams.result = new PorterStemFilter(streams.source);

            setPreviousTokenStream(streams);
        } else {
            streams.source.reset(reader);
        }
        return streams.result;
    }
    
   

}
