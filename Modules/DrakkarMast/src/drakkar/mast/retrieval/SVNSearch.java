/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.IndexException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SVNSearch extends CVSSearch {

    SVNContext svnContext;

    /**
     *
     * @param svnContext
     */
    public SVNSearch(SVNContext svnContext) {
        this.svnContext = svnContext;
    }

    /**
     *
     * @param listener
     */
    public SVNSearch(FacadeDesktopListener listener) {
        this.svnContext = new SVNContext(listener);
    }

   

    @Override
    public Searchable getSearchable() {
        return this;
    }

    @Override
    public int getID() {
        return KeySearchable.SVN_SEARCHER;
    }

    @Override
    public String getName() {
        return "SVN Searcher";
    }

    public long makeIndex() throws IndexException {
        try {
            return svnContext.makeIndex();
        } catch (drakkar.mast.IndexException ex) {
            OutputMonitor.printStream("", ex);
        }

        return 0;
    }

    public long makeIndex(File indexPath) throws IndexException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean loadIndex() throws IndexException {
        try {
            return svnContext.loadIndex();
        } catch (drakkar.mast.IndexException ex) {
            OutputMonitor.printStream("", ex);
        } catch (IOException ex) {
            OutputMonitor.printStream("IO", ex);
        }

        return false;
    }

    public boolean loadIndex(File indexPath) throws IndexException {
        try {
            return svnContext.loadIndex(indexPath);
        } catch (drakkar.mast.IndexException ex) {
            OutputMonitor.printStream("", ex);
        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
        }

        return false;
    }

    @Override
    public void setProperties(Properties p) {
       svnContext.setProperties(p);
    }

    @Override
    public void setListener(FacadeDesktopListener listener) {
        this.svnContext = new SVNContext(listener);
    }

    public Contextable getContext() {
        return svnContext;
    }
}
