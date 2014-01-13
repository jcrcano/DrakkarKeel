/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.file.transfer;

import Ice.ObjectPrx;
import drakkar.oar.Communication;
import drakkar.oar.security.DrakkarSecurity;
import drakkar.oar.slice.transfer.FileAccessException;
import drakkar.oar.slice.transfer.FilePrx;
import drakkar.oar.slice.transfer.FilePrxHelper;
import drakkar.oar.slice.transfer._FileStoreDisp;
import drakkar.oar.svn.SVNCache;
import drakkar.oar.svn.SVNController;
import drakkar.oar.svn.SVNData;
import drakkar.oar.util.OutputMonitor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 *
 * Clase que lee un fichero de su ubicacion original para transferir al cliente
 */
public class FileStoreServant extends _FileStoreDisp {

    Communication communication;
    File resourcesfile;
    String fileSeparator;
    File cache = new File("./cache");
    /**
     * Fichero local
     */
    public final static int LOCAL_FILE = 1;
    /**
     * Fichero de un repositorio SVN
     */
    public final static int SVN_FILE = 2;
    /**
     * Fichero de un ftp
     */
    public final static int FTP_FILE = 3;
    private SVNController svnController;
    SVNCache svnCache = new SVNCache();

    /**
     *
     * @param communication
     * @param svnController
     */
    public FileStoreServant(Communication communication, SVNController svnController) {
        this.communication = communication;
        this.svnController = svnController;

        fileSeparator = System.getProperty("file.separator");
        resourcesfile = new File("." + fileSeparator + "upload");
        cache = new File("." + fileSeparator + "cache");

    }

    /**
     *
     * @param name
     * @param num
     * @param current
     * @return
     * @throws FileAccessException
     */
    public FilePrx read(String name, int num, Ice.Current current) throws FileAccessException {
         OutputMonitor.printLine("Read File: ", OutputMonitor.INFORMATION_MESSAGE);    
        int option = 0;
        File analyzedFile = null;
        String newPath = null;
        FilePrx fileprx = null;

        option = analyzedRequestFile(name);

        //create cache dir
        if (!cache.exists()) {
            cache.mkdir();
        }

        switch (option) {
            case LOCAL_FILE:
                OutputMonitor.printLine("Local File: ", OutputMonitor.INFORMATION_MESSAGE);
                analyzedFile = new File(name);
                break;

            case SVN_FILE:

                getSVNRepoUrl(name, svnCache);
                getSVNFilePath(name, svnCache);

                Map<String, SVNData> list = svnController.getSvnRepositoriesDB();
                SVNData data = null;
                if (!list.isEmpty() && list.containsKey(svnCache.getRepoName())) {
                    data = list.get(svnCache.getRepoName());

                    newPath = getFilePathCache(SVN_FILE, svnCache.getFilePath());
                    if (newPath != null && (new File(newPath).exists() == false)) {

                        svnController.displayFile(svnCache.getUrl(), data.getUser(), DrakkarSecurity.decryptPassword(data.getPassword()), svnCache.getFilePath(), newPath);

                    } else {
                        OutputMonitor.printLine("File exists: ", OutputMonitor.ERROR_MESSAGE);
                    }
                    analyzedFile = new File(newPath);

                } else {
                    OutputMonitor.printLine("The repository " + svnCache.getRepoName() + " is not indexed", OutputMonitor.ERROR_MESSAGE);
                }

                break;

            case FTP_FILE:

                //todo> hacer el m'etodo que extrae el file del ftp
                newPath = getFilePathCache(FTP_FILE, "");
                analyzedFile = new File(newPath);
                break;

        }

        if (analyzedFile != null) {
            try {
                ObjectPrx file = this.communication.getAdapter().addWithUUID(
                        new FileServant(analyzedFile, num));
                fileprx = FilePrxHelper.uncheckedCast(file);
            } catch (FileNotFoundException ex) {
                throw new FileAccessException(ex.getMessage());
            }
        } else {
            OutputMonitor.printLine("File null.", OutputMonitor.ERROR_MESSAGE);
        }

        return fileprx;
    }

    /**
     *
     * @param name
     * @param offset
     * @param bytes
     * @param current
     */
    public void write(String name, int offset, byte[] bytes, Ice.Current current) {
        FileOutputStream out = null;
        try {
            checkUploadDir();
            out = new FileOutputStream(resourcesfile.getAbsolutePath() + "/" + name, true);
            out.write(bytes, offset, bytes.length);
            out.close();
        } catch (IOException ex) {
            OutputMonitor.printStream("IO", ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                OutputMonitor.printStream("IO", ex);
            }
        }


    }

    private void checkUploadDir() {
        if (!resourcesfile.exists()) {
            if (resourcesfile.mkdir()) {
                OutputMonitor.printLine("Resources file created: " + resourcesfile, OutputMonitor.INFORMATION_MESSAGE);
            }
        }

    }

    /**
     * Obtiene el tipo de fichero que pide el cliente
     */
    private int analyzedRequestFile(String name) {
        if (name.contains("svn")) {
            return SVN_FILE;
        } else if (name.contains("ftp")) {
            return FTP_FILE;
        } else {
            return LOCAL_FILE;
        }
    }

    /**
     * Procesa de acuerdo al tipo de fichero el filepath donde ser'a guardado en cache
     */
    private String getFilePathCache(int type, String filepath) {

        String path = null;
        //create cache dir
        if (!cache.exists()) {
            cache.mkdir();
        }
        String fileName = filepath.substring(filepath.lastIndexOf("/") + 1);

        switch (type) {

            case SVN_FILE:
                path = cache.getAbsolutePath().concat("/svn/" + svnCache.getRepoName() + "/");
                break;

            case FTP_FILE:
                path = cache.getAbsolutePath().concat("/ftp/");
                break;

        }

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path + fileName;

    }

    private String getSVNRepoUrl(String name, SVNCache svn) {

        String[] array = name.split("svn");
        String first = array[0];
        String second = array[1];

        String repoName = second.substring(1, second.indexOf("/", 2));
        svn.setRepoName(repoName);

        String url = first.concat("svn/" + repoName + "/");
        svn.setUrl(url);

        return url;

    }

    private String getSVNFilePath(String name, SVNCache svn) {

        String[] array = name.split(svn.getRepoName());
        svn.setFilePath(array[1]);

        return array[1];
    }

    /**
     * @return the svnController
     */
    public SVNController getSvnController() {
        return svnController;
    }

    /**
     * @param svnController the svnController to set
     */
    public void setSvnController(SVNController svnController) {
        this.svnController = svnController;
        Map<String, SVNData> list = svnController.getSvnRepositoriesDB();
    }
}
