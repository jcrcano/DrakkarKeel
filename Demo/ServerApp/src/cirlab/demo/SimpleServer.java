/*
 * @(#)SimpleServer.java   2.0.0   26/11/2013
 *
 * CIRLab - Collaborative Information Retrieval Laboratory
 * Webpage: unascribed
 * Contact: cirlab-dev@googlegroups.com
 * University of Granada - Department of Computing Science and Artificial
 * Intelligence
 * http://www.ugr.es/
 * University of the Informatics Sciences - Holguin Software Development Center
 * http://www.uci.cu/
 *
 * The contents of this file are subject under the terms described in the
 * CIRLAB_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License.
 *
 * Copyright (C) 2008-2013 CIRLab. All Rights Reserved.
 *
 * Contributor(s):
 *   RODRIGUEZ-AVILA, HUMBERTO <hrodrigueza{a.}facinf.uho.edu.cu>
 *   LEYVA-AMADOR, YOVIER <yovi{a.}hol.inv.cu>
 *   RODRIGUEZ-CANO, JULIO CESAR <jcrcano{a.}uci.cu>
 */
package cirlab.demo;

import drakkar.mast.retrieval.MinionSearchEngine;
import drakkar.mast.retrieval.LuceneSearchEngine;
import drakkar.mast.retrieval.SearchEngine;
import drakkar.mast.IndexException;
import drakkar.stern.DrakkarStern;
import java.io.File;

public class SimpleServer {

    public static void main(String[] args) {
        int status = 0;
        String collectionPath = "collection/";
        String[] config = {"-sp", "11900"};
        DrakkarStern server = DrakkarStern.getInstance(config);
        SearchEngine lucene = new LuceneSearchEngine();
        SearchEngine minion = new MinionSearchEngine();

        try {
//             By default, the indexes will be created at <application's root>/index/
            lucene.makeIndex(new File(collectionPath));
            minion.makeIndex(new File(collectionPath));
            lucene.setEnabled(true);
            minion.setEnabled(true);
            server.addSearcher(lucene);
            server.addSearcher(minion);
            server.start();
            server.waitForShutdown();
        } catch (IndexException ie) {
            System.err.println(ie.getMessage());
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        // Clean up
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);
    }
}
