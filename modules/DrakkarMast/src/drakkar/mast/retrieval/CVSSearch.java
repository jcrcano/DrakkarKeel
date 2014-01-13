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

import drakkar.oar.DocumentMetaData;
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.security.DrakkarSecurity;
import drakkar.oar.svn.SVNData;
import drakkar.mast.SearchException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Clase que contiene los métodos de búsqueda para CVS
 */
public abstract class CVSSearch implements Searchable, RepositoryIndexable {

    protected boolean enabled = false;
    String fileSeparator = System.getProperty("file.separator");
    private StringBuilder repositoriesEnabled = new StringBuilder();
    String indexPath;
    private ArrayList<String> indexList = new ArrayList<String>(); //lista que contiene los indices notificados

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param query
     * @param svnRepository
     * @param fileType
     * @param sort
     * @param lastmodified
     * @param user
     * @param fileBody
     * @return
     * @throws SearchException
     */
    public ArrayList<DocumentMetaData> search(String query, SVNData svnRepository, String fileType, String sort, String lastmodified, String user, boolean fileBody) throws SearchException {

        Properties p = new Properties();
        p.setProperty("url", svnRepository.getUrl());
        p.setProperty("repListFile", svnRepository.getNames());
        p.setProperty("indexPath", svnRepository.getIndexPath());
        p.setProperty("user", svnRepository.getUser());
        p.setProperty("password", DrakkarSecurity.decryptPassword(svnRepository.getPassword()));
        // p.setProperty("mergeFactor", data.getMergeFactor());//max loadedDocs of files keept in memory by Lucene
        // p.setProperty("repListFile", this.getRepositoryName());
        // p.setProperty("indexPath", this.getIndexPath());
        
       ((SVNContext) this.getContext()).setProperties(p);

        ArrayList<DocumentMetaData> results = ((SVNContext) this.getContext()).search(query, sort, fileType, lastmodified, user, fileBody);

        return results;

    }

    /**
     * {@inheritDoc}
     */
    public Searchable getSearchable() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * {@inheritDoc}
     */
    public abstract int getID();

    /**
     * {@inheritDoc}
     */
    public abstract String getName();

  
    /**
     *
     * @param index
     * @param enable
     * @throws FileNotFoundException
     */
    public void setIndexPath(String index, boolean enable) throws FileNotFoundException {
        indexPath = index;
        if (enable) {
            if (!indexList.contains(indexPath)) {
                indexList.add(indexPath);
            }
        } else {
            if (indexList.contains(indexPath)) {
                indexList.remove(indexPath);
            }
        }

    }

    /**
     * 
     * @return
     */
    public String getIndexPath() {
        return this.indexPath;
    }

    /**
     * 
     * @param p
     */
    public abstract void setProperties(Properties p);

    /**
     *
     * @param listener
     */
    public abstract void setListener(FacadeDesktopListener listener);

    /**
     * @return the repositoriesEnabled
     */
    public StringBuilder getRepositoriesEnabled() {
        ArrayList<String> repoNames= new ArrayList<String>();
        if (!indexList.isEmpty()) {
            for (int i = 0; i < indexList.size(); i++) {
                String index = indexList.get(i);
                String name = this.getRepositoryName(index);
                repoNames.add(name);
                if (!repositoriesEnabled.toString().contains(name)) {
                    this.repositoriesEnabled.append(name).append(",");
                }
            }

            //verify an index that was not selected
            String array[] = repositoriesEnabled.toString().split(",");

            for (int i = 0; i < array.length; i++) {
                String string = array[i];
                if (!repoNames.contains(string)) {                    
                    int start = repositoriesEnabled.indexOf(string);
                    int end = start + string.length()+1;
                    repositoriesEnabled = repositoriesEnabled.delete(start, end);
                }
            }
        } else {
            repositoriesEnabled = new StringBuilder();
        }
        return repositoriesEnabled;
    }

    /**
     * @param repositoriesEnabled the repositoriesEnabled to set
     */
    public void setRepositoriesEnabled(StringBuilder repositoriesEnabled) {
        this.repositoriesEnabled = repositoriesEnabled;
    }

    private String getRepositoryName(String uriIndex) {
        String repoName = null;

        String[] array = uriIndex.split("svn");
        repoName = array[1].substring(1, array[1].indexOf("/", 2));
        
        return repoName;
    }
}
