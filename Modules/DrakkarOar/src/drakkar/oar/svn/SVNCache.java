/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.svn;

/**
 * 
 * Contiene los datos necesarios para salvar un fichero de un repositorio SVN
 */
public class SVNCache {

    String repoName;
    String url;
    String filePath;
 
    /**
     * Constructor por defecto
     */
    public SVNCache() {
    }
    
    /**
     * Constructor de la clase
     *
     * @param repoName      nombre del repositorio
     * @param url           dirección donde está el repositorio
     * @param filePath      dirección del fichero
     */
    public SVNCache(String repoName, String url, String filePath) {
        this.repoName = repoName;
        this.url = url;
        this.filePath = filePath;
    }

    /**
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     *
     * @return
     */
    public String getRepoName() {
        return repoName;
    }

    /**
     *
     * @param repoName
     */
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

 
    
}
