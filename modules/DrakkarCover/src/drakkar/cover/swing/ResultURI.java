/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.cover.swing;

import drakkar.oar.util.OutputMonitor;
import java.io.File;
import java.net.MalformedURLException;

public class ResultURI {

    String originalPath;
    String fixPath;
    private String serverHost;

    public ResultURI() {
    }

    public ResultURI(String originalPath, String serverHost) {
        this.originalPath = originalPath;
        this.fixPath = "" + ((serverHost == null)?"localhost":serverHost) + "://" + processPath(originalPath);
    }

    public String getFixPath() {
        return fixPath;
    }

    public void setFixPath(String fixPath) {
        this.fixPath = fixPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    /**
     * @return the serverHost
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * @param serverHost the serverHost to set
     */
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    private String processPath(String path) {
        
        File f = new File(path);
       
        String newPath = null;
        try {
            newPath = f.toURI().toURL().toString().substring(9);
        } catch (MalformedURLException ex) {
            OutputMonitor.printStream("", ex);
        }
       
        return newPath;

    }

    @Override
    public String toString() {
        return this.fixPath;
    }


}
