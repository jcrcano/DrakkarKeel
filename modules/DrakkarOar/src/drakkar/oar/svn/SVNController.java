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

import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.facade.event.FacadeDesktopListener;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.NotifyAction;
import drakkar.oar.util.OutputMonitor;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Clase que contiene métodos de acceso a un repositorio SVN
 */
public class SVNController {

    //identificador string: nombre del repositorio
    //SVNData: datos de ese repositorio guardados en la BD
    private Map<String, SVNData> svnRepositoriesDB = new HashMap<>();
    FacadeDesktopListener listener;

    /**
     * 
     * @param listener
     */
    public SVNController(FacadeDesktopListener listener) {
        this.listener = listener;
    }

    /**
     * Verifies if a repository exists in a given url
     *
     * @param url
     * @param name
     * @param password
     * @return
     */
    public boolean existRepository(String url, String name, String password) {

        setupLibrary();
        SVNRepository repository = null;
        try {
            /*
             * Creates an instance of SVNRepository to work with the repository.
             * All user's requests to the repository are relative to the
             * repository location used to create this SVNRepository.
             * SVNURL is a wrapper for URL strings that refer to repository locations.
             */
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            /*
             * Perhaps a malformed URL is the cause of this exception
             */
            String message = "Error while creating an SVNRepository for location " + url + "\n" + svne.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
            return false;
        }

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
        try {
            repository.testConnection();
            OutputMonitor.printLine("Repository Root: " + repository.getRepositoryRoot(true), OutputMonitor.INFORMATION_MESSAGE);
           
            repository.closeSession();
        } catch (SVNException ex) {
            String message = "Error: " + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
            return false;
        }
        return true;
    }

    /**
     * Get the number of documents of an SVN repository
     *
     * @param url
     * @param name
     * @param password
     * @return
     */
    public int getRepositorySize(String url, String name, String password) {

        SVNRepository repository = null;
        int count = 0;
        try {

            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {

            String message = "Error while creating an SVNRepository for location '"
                    + "\n" + url + "': " + "\n" + svne.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        }


        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);

        SVNNodeKind nodeKind = null;
        try {
            nodeKind = repository.checkPath("", -1);
        } catch (SVNException ex) {
            String message = "Error " + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
        }
        if (nodeKind == SVNNodeKind.NONE) {

            String message = "There is no entry at '" + url + "'.";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        } else if (nodeKind == SVNNodeKind.FILE) {

            String message = "The entry at '" + url + "' is a file while a directory was expected.";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        }
        try {
            /*
             * Get the number of files in the repository
             */
            count = listEntries(repository, "", count);
        } catch (SVNException ex) {
            String message = "Error: " + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        }

        return count;
    }

    /**
     * Gets the file's content located in an SVN Repository
     *
     * @param url      address of the repository (Example: https://your-c7096bbd5b:8443/svn/MyRepository/)
     * @param user     user name for authenticate in the SVN repository
     * @param password password for authenticate in the SVN repository
     * @param filePath path of the file to open (/SVNDemo/src/util/MainSearch.java)     
     * @param outPath  file where will be save the file
     * 
     *
     */
    public void displayFile(String url, String user, String password, String filePath, String outPath) {

        SVNRepository repository = null;

        setupLibrary();

        try {
            /*
             * Creates an instance of SVNRepository to work with the repository.
             * All user's requests to the repository are relative to the
             * repository location used to create this SVNRepository.
             * SVNURL is a wrapper for URL strings that refer to repository locations.
             */
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            /*
             * Perhaps a malformed URL is the cause of this exception
             */
            String message = "Error while creating an SVNRepository for location " + url;
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        }

        /*
         * User's authentication information (name/password) is provided via  an
         * ISVNAuthenticationManager  instance.  SVNWCUtil  creates  a   default
         * authentication manager given user's name and password.
         *
         * Default authentication manager first attempts to use provided user name
         * and password and then falls back to the credentials stored in the
         * default Subversion credentials storage that is located in Subversion
         * configuration area. If you'd like to use provided user name and password
         * only you may use BasicAuthenticationManager class instead of default
         * authentication manager:
         *
         *  authManager = new BasicAuthenticationsManager(userName, userPassword);
         *
         * You may also skip this point - anonymous access will be used.
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, password);
        repository.setAuthenticationManager(authManager);

        /*
         * This Map will be used to get the file properties. Each Map key is a
         * property name and the value associated with the key is the property
         * value.
         */
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            /*
             * Checks up if the specified path really corresponds to a file. If
             * doesn't the program exits. SVNNodeKind is that one who says what is
             * located at a path in a revision. -1 means the latest revision.
             */
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);

            if (nodeKind == SVNNodeKind.NONE) {

                String message = "There is no entry at '" + url + "'.";
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                notifyTaskProgress(ERROR_MESSAGE, message);
            } else if (nodeKind == SVNNodeKind.DIR) {

                String message = "The entry at '" + url
                        + "' is a directory while a file was expected.";
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                notifyTaskProgress(ERROR_MESSAGE, message);
            }
            /*
             * Gets the contents and properties of the file located at filePath
             * in the repository at the latest revision (which is meant by a
             * negative revision number).
             */
            repository.getFile(filePath, -1, fileProperties, baos);

        } catch (SVNException svne) {
            String message = "Error while fetching the file contents and properties: " + "\n" + svne.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);
        }

        /*
         * Here the SVNProperty class is used to get the value of the
         * svn:mime-type property (if any). SVNProperty is used to facilitate
         * the work with versioned properties.
         */
        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);

        /*
         * SVNProperty.isTextMimeType(..) method checks up the value of the mime-type
         * file property and says if the file is a text (true) or not (false).
         */
        boolean isTextType = SVNProperty.isTextMimeType(mimeType);

        Iterator iterator = fileProperties.nameSet().iterator();
        /*
         * Displays file properties.
         */
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = fileProperties.getStringValue(propertyName);
            //esto muestra las propiedades del file svn
            OutputMonitor.printLine("File property: " + propertyName + "=" + propertyValue, OutputMonitor.INFORMATION_MESSAGE);
        }
        /*
         * Displays the file contents in the console if the file is a text.
         */

        if (isTextType) {
            try {
                try (FileOutputStream output = new FileOutputStream(outPath)) {
                    baos.writeTo(output);
                }
            } catch (IOException ex) {
                OutputMonitor.printStream("IO", ex);
            }

        } else {
            OutputMonitor.printLine("The mime-type property says that the file not a kind of a text file.", OutputMonitor.ERROR_MESSAGE);
        }
        /*
         * Gets the latest revision number of the repository
         */
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            String message = "Error while fetching the file contents and properties: " + "\n" + svne.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            notifyTaskProgress(ERROR_MESSAGE, message);

        }

    }

    /**
     * @return the svnRepositoriesDB
     */
    public Map<String, SVNData> getSvnRepositoriesDB() {
        return svnRepositoriesDB;
    }

    /**
     * @param svnRepositoriesDB the svnRepositoriesDB to set
     */
    public void setSvnRepositoriesDB(Map<String, SVNData> svnRepositoriesDB) {
        this.svnRepositoriesDB = svnRepositoriesDB;
    }

    /**
     * Obtiene la cantidad de repositorios que hay en la BD
     * 
     * @return
     */
    public int getRepositoryNumber() {
        return this.svnRepositoriesDB.size();
    }

    /**
     * Este método notifica al servidor el progreso de las actividades invocadas
     * para actualizar el tablón de Log y Monitor.
     *
     * @param messageType   tipo de mensage:<tt>INFORMATION_MESSAGE,ERROR_MESSAGE</tt>
     * @param message       contenido del mensaje
     */
    private void notifyTaskProgress(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_TEXT_MESSAGE);
            rs.put(MESSAGE_TYPE, messageType);
            rs.put(MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }


    }

    /**
     * Initializes the library to work with a repository via
     * different protocols.
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();

        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

    /**
     * Lists the content of an SVN repository
     *
     * @param repository
     * @param path
     * @param c
     * @return
     * @throws SVNException
     */
    private static int listEntries(SVNRepository repository, String path, int c)
            throws SVNException {

        Collection collection = null;
        SVNProperties prop = null;
        /*
         * Gets the contents of the directory specified by path at the latest
         * revision (for this purpose -1 is used here as the revision number to
         * mean HEAD-revision) getDir returns a Collection of SVNDirEntry
         * elements. SVNDirEntry represents information about the directory
         * entry. Here this information is used to get the entry name, the name
         * of the person who last changed this entry, the number of the revision
         * when it was last changed and the entry type to determine whether it's
         * a directory or a file. If it's a directory listEntries steps into a
         * next recursion to display the contents of this directory. The third
         * parameter of getDir is null and means that a user is not interested
         * in directory properties. The fourth one is null, too - the user
         * doesn't provide its own Collection instance and uses the one returned
         * by getDir.
         */

        Collection entries = repository.getDir(path, (long) SVNRevision.HEAD.getNumber(), prop, collection);


        Iterator iterator = entries.iterator();

        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            if (entry.getKind() == SVNNodeKind.FILE) {
                c++;
            }
            /* Checking up if the entry is a directory.
             */
            if (entry.getKind() == SVNNodeKind.DIR) {
                c = +listEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName(), c);
            }
        }
        return c;
    }
}
