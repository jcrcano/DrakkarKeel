/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.recommender;

import drakkar.mast.util.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AWEntropy {

//    static Map<Integer, Integer> docsIDsMap;
    static Map<Integer, Integer> termsAbsFrecMap;
    static double[][] vals;

    public static double[][] assignmentOfWeights(CollectionInfo collection) {

        createMatriz(collection);

        int t = collection.getTermCount();
        int d = collection.getDocsCount();

        double[][] newVals = new double[t][d];
        double logN = Math.log2(d);

        for (int i = 0; i < t; i++) {
            for (int j = 0; j < d; j++) {
                newVals[i][j] = getGi(i, d, logN) * java.lang.Math.log(getValue(i, j) + 1);
            }
        }

        return newVals;
    }

    private static double[][] createMatriz(CollectionInfo collection) {
        termsAbsFrecMap = new HashMap<Integer, Integer>();

        Map<TermInfo, List<DocTermInfo>> termsMap = collection.getTermsMap();

        int rows = collection.getTermCount();
        int columns = collection.getDocsCount();

        vals = new double[rows][columns];

        List<TermInfo> terms = new ArrayList<TermInfo>(termsMap.keySet());
        Collections.sort(terms);
        List<DocTermInfo> docs;

        TermInfo term;
        for (int i = 0; i < terms.size(); i++) {
            term = terms.get(i);
            docs = termsMap.get(term);
            termsAbsFrecMap.put(i, term.getAbsTermFrec());
            for (DocTermInfo docInfo : docs) {
                vals[i][docInfo.getDocNum()] = (double) docInfo.getTermFreq();
            }
        }

        return vals;
    }

    private static double getGi(int i, int d, double logN) {
        double value = 1;
        if (d > 1) {

            double addtion = 0;
            double pij = 0;
            for (int j = 0; j < d; j++) {
                pij = getPij(i, j);
                if (pij > 0) {
                    addtion += (pij * Math.log2(pij)) / logN;
                }

            }
            value += addtion;
        }
        return value;
    }

    @SuppressWarnings("element-type-mismatch")
    private static double getPij(int iTerm, int iDoc) {
        int termAbsFec = 0;
        double termRelFec = 0;

        termRelFec = getValue(iTerm, iDoc);
        termAbsFec = termsAbsFrecMap.get(iTerm);
        double value = termRelFec / termAbsFec;

        return value;
    }

    private static double getValue(int i, int j) {
        return vals[i][j];
    }
}
