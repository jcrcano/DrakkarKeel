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

import java.io.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

/**
 * Esta clase representa una consulta analizada con el buscador Apache Lucene
 *
 *
 */
public class NGramQuery extends BooleanQuery {

    /**
     *
     * @param analyzer
     * @param queryTerm
     * @param field
     * @throws IOException
     */
    public NGramQuery(Analyzer analyzer, String queryTerm, String field)
            throws IOException {
        String words[] = null;

        //remove white spaces
        if (queryTerm.contains(" ")) {
            words = queryTerm.split(" ");
        } else {
            words = new String[1];
            words[0] = queryTerm;
        }

        //one term
        if (words.length > 1) {
            for (int i = 0; i < words.length; i++) {
                String string = words[i];
                Term t = new Term(field, string);
                TermQuery pquery = new TermQuery(t);
                add(pquery, org.apache.lucene.search.BooleanClause.Occur.SHOULD);
            }

        } else {
            //more than one term
            for (int i = 0; i < words.length; i++) {
                String wordToAnalyze = words[i];
                TokenStream tokens = analyzer.tokenStream(field, new StringReader(wordToAnalyze));
                TermAttribute termAtt = (TermAttribute) tokens.addAttribute(TermAttribute.class);
                tokens.reset();
                TermQuery pquery;
                for (; tokens.incrementToken(); add(new BooleanClause(pquery, org.apache.lucene.search.BooleanClause.Occur.MUST))) {
                    Term t = new Term(field, termAtt.term());
                    pquery = new TermQuery(t);
                }

                tokens.end();
                tokens.close();
            }

        }
    }
}
