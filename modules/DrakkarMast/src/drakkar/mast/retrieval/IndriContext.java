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
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lemurproject.indri.IndexEnvironment;
import lemurproject.indri.IndexStatus;
import lemurproject.indri.QueryEnvironment;
import lemurproject.indri.QueryRequest;
import lemurproject.indri.QueryResult;
import lemurproject.indri.QueryResults;
import lemurproject.indri.Specification;
import lemurproject.lemur.Index;
import lemurproject.lemur.IndexManager;

/**
 * Clase que implementa el motor de búsqueda Indri-Lemur, versión 4.10
 *
 * 
 */
public class IndriContext extends EngineContext {

    private Index theIndex;
    private boolean appendIndex;
    private File idxCS; //saving file for case sensitive index
    private File idxCI; //saving file for case insensitive index
    private IndexEnvironment envCS;
    private IndexEnvironment envCI;
    private IndexStatus status;
    private Specification spec;
    private String[] retval;
    private QueryEnvironment queryEnv;
    private QueryRequest qrequest;
    private QueryResults qresults;
    private File indexPathCI;//for case insensitive search
    private QueryResult[] queryResults;
    private QueryResult queryResultObj;

    /**
     *  constructor por defecto
     */
    public IndriContext() {
        super();
        this.defaultIndexPath = "./index/indri/";
    }

    /**
     * constructor
     *
     * @param listener  --oyente de los procesos realizados por este motor
     */
    public IndriContext(FacadeDesktopListener listener) {
        super(listener);
        this.defaultIndexPath = "./index/indri/";
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        this.queryEnv = new QueryEnvironment();
        this.qrequest = new QueryRequest();
        this.qresults = new QueryResults();

        try {
            setStartTimeOfSearch(new Date());

            if (checkIndexPath(this.indexPath.getPath())) {
                if (caseSensitive) {
                    this.queryEnv.addIndex(this.indexPath.getPath());
                    this.theIndex = lemurproject.lemur.IndexManager.openIndex(this.indexPath.getPath());
                    this.qrequest.query = query;
                    this.qrequest.resultsRequested = this.theIndex.docCount();
                    this.qresults = this.queryEnv.runQuery(this.qrequest);

                } else {
                    this.queryEnv.addIndex(this.indexPathCI.getPath());
                    this.theIndex = lemurproject.lemur.IndexManager.openIndex(this.indexPathCI.getPath());
                    this.qrequest.query = query;
                    this.qrequest.resultsRequested = this.theIndex.docCount();
                    this.qresults = this.queryEnv.runQuery(this.qrequest);
                }
                finalResultsList = saveResults(this.qresults, query, this.queryEnv);

                if (finalResultsList.size() > 1) {
                    deleteRepeated(finalResultsList);
                }

                this.finalMetaResult = finalResultsList;

                setEndTimeOfSearch(new Date());
                String message = "Indri retrieved " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for both ";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            } else {
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }



        } catch (Exception ex) {
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        tempList = search(query, caseSensitive); //search in all collections of documents
        finalResultsList = this.filterMetaDocuments(docType, tempList);

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Indri retrieved " + this.finalMetaResult.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctype " + docType;
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        for (int i = 0; i < docTypes.length; i++) {
            String doc = docTypes[i];
            tempList = search(query, doc, caseSensitive);
            finalResultsList.addAll(tempList);
        }

        if (finalResultsList.size() > 1) {
            deleteRepeated(finalMetaResult);
        }

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Indri retrieved " + this.finalMetaResult.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query;
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {
        this.collectionPath = new File(this.defaultCollectionPath);
        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;

        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;

    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;

        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        long indexedFiles = 0;
        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(ADD_INDEX);
        }


        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;
        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, ADD_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            indexedFiles = this.build(ADD_INDEX);
        } else {
            String message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        long indexedFiles = 0;
        if (collectionPath.size() == 0) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, ADD_INDEX);
        } else {
            String message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException {
        /*revisa solo uno de los directorios*/
        File defaultfileCI = new File(this.defaultIndexPath.concat("/caseinsensitive"));
        File defaultfile = new File(this.defaultIndexPath.concat("/casesensitive"));
        if (defaultfileCI.exists() && defaultfile.exists()) {
            OutputMonitor.printLine("Loading Indri... ");
            try {
                //verify that an index exists
                //verify that an index exists
                File manifest = new File(defaultfileCI.getPath(), "manifest");
                File manifest2 = new File(defaultfile.getPath(), "manifest");

                if (manifest.exists() && manifest2.exists()) {
                    // open the index
                    this.theIndex = lemurproject.lemur.IndexManager.openIndex(defaultfileCI.getPath());
                    // get the count of documents
                    int numDocuments = this.theIndex.docCount();
                    // get the average document length (in words)
                    float avgDocLength = this.theIndex.docLengthAvg();
                    // get the count of total terms
                    int totalTermCount = this.theIndex.termCount();
                    // get the count of _unique_ terms
                    int uniqueTermCount = this.theIndex.termCountUnique();
                    // print out our statistics
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Indri...");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# documents: " + numDocuments + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "Avg. Document Length: " + avgDocLength + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# terms: " + totalTermCount + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# unique terms: " + uniqueTermCount + "\n");

                    this.notifyLoadedDocument(numDocuments);
                    OutputMonitor.printLine("# documents: " + numDocuments);
                    //set path for search
                    this.indexPath = defaultfile;
                    this.indexPathCI = defaultfileCI;
                    
                    return true;
                } else {
                    throw new IndexException("Not found index in this directory: " + this.defaultIndexPath);
                }

            } catch (Exception e) {
                throw new IndexException(e.getMessage());
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IndexException {

        File newfileCI = new File(indexPath.getPath().concat("/casesensitive"));
        File newfile = new File(indexPath.getPath().concat("/caseinsensitive"));

        if (newfileCI.exists() && newfile.exists()) {
            OutputMonitor.printLine("Loading Indri... ");
            try {
                //verify that an index exists
                File manifest = new File(newfileCI.getPath(), "manifest");
                File manifest2 = new File(newfile.getPath(), "manifest");

                if (manifest.exists() && manifest2.exists()) {
                    // open the index
                    this.theIndex = lemurproject.lemur.IndexManager.openIndex(newfileCI.getPath());
                    // get the count of documents
                    int numDocuments = this.theIndex.docCount();
                    // get the average document length (in words)
                    float avgDocLength = this.theIndex.docLengthAvg();
                    // get the count of total terms
                    int totalTermCount = this.theIndex.termCount();
                    // get the count of _unique_ terms
                    int uniqueTermCount = this.theIndex.termCountUnique();
                    // print out our statistics
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Indri...");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# documents: " + numDocuments + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "Avg. Document Length: " + avgDocLength + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# terms: " + totalTermCount + "\n");
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "# unique terms: " + uniqueTermCount + "\n");

                    this.notifyLoadedDocument(numDocuments);
                    
                    //set path for search
                    this.indexPath = newfile;
                    this.indexPathCI = newfileCI;

                    OutputMonitor.printLine("# documents: " + numDocuments);
                    return true;
                } else {

                    throw new IndexException("Not found index in this directory: " + newfileCI.getPath());

                }
            } catch (Exception e) {
                throw new IndexException(e.getMessage());
            }

        }
        return false;
    }

    /**
     *  Método para construir el índice con la colección por defecto
     *
     *  @param  operación a realizar: MAKE o ADD
     */
    private long build(int operation) throws IndexException {
        long indexedFiles = 0;

        setStartTimeOfIndexation(new Date());
        String t1 = "Indexing to directory '" + this.indexPath + "'..." + "\n";
        this.notifyTaskProgress(INFORMATION_MESSAGE, t1);

        if (safeToBuildIndex(this.indexPath, operation)) {

            indexedFiles = indexDocs(this.collectionPath, operation);

            setEndTimeOfIndexation(new Date());
            String t3 = getIndexationTime() + " total milliseconds" + "\n";
            this.notifyTaskProgress(INFORMATION_MESSAGE, t3);
        } else {
            this.notifyTaskProgress(ERROR_MESSAGE, "Unable to build the index");
            throw new IndexException("Unable to build the index");
        }

        return indexedFiles;
    }

    /**
     *   Método para construir el índice a partir de una colección de files
     *
     *  @param operation          ----- operación a realizar: MAKE o ADD
     *  @param collectionPath     ----- lista de ficheros que representan la colección
     */
    private long build(List<File> collectionPath, int operation) throws IndexException {

        long indexedFiles = 0;
        String m1 = "Indexing to directory '" + this.indexPath + "'..." + "\n";
        this.notifyTaskProgress(INFORMATION_MESSAGE, m1);

        if (safeToBuildIndex(this.indexPath, operation)) { //inicia la indexacion
            setStartTimeOfIndexation(new Date());

            indexedFiles = indexDocs(collectionPath, operation);

            this.notifyTaskProgress(INFORMATION_MESSAGE, "Finalizó la indexación");
            setEndTimeOfIndexation(new Date());
            String m2 = getIndexationTime() + " total milliseconds" + "\n";
            this.notifyTaskProgress(INFORMATION_MESSAGE, m2);
        } else {
            this.notifyTaskProgress(ERROR_MESSAGE, "Unable to build the index");
            throw new IndexException("Unable to build the index");
        }

        return indexedFiles;

    }

    /**
     * Indexa una colección dado una lista de files
     */
    private int indexDocs(List<File> data, int operation) throws IndexException {
        int totalDocumentsIndexed = 0;

        this.envCS = new IndexEnvironment();
        this.envCI = new IndexEnvironment();

        this.status = new UIIndexStatus();
        this.spec = null;

        try {
            // memory
            this.envCS.setMemory(encodeMem());
            this.envCI.setMemory(encodeMem());

            //case sensitive index
            this.envCS.setNormalization(false);
            this.envCI.setNormalization(true);

            //stopwords
            this.envCS.setStopwords(ENGLISH_STOP_WORDS);
            this.envCI.setStopwords(ENGLISH_STOP_WORDS);

            //get all the filespaths
            String[] datafiles = dataFilesList(data, new ArrayList<String>());

            // create a new empty index (after parameters have been set).
            if (this.appendIndex) {
                this.envCS.open(this.idxCS.getPath(), this.status);
                this.envCI.open(this.idxCI.getPath(), this.status);
            } else {
                this.envCS.create(this.idxCS.getPath(), this.status);
                this.envCI.create(this.idxCI.getPath(), this.status);
            }

            // do the building
            String fname, fileClass;
            for (int i = 0; i < datafiles.length; i++) {
                fname = datafiles[i];
                fileClass = getFileExtension(fname);

                if (fileClass.equalsIgnoreCase("java") || fileClass.equalsIgnoreCase("pdf") || fileClass.equalsIgnoreCase("txt")) {
                    if (fileClass.equalsIgnoreCase("txt") || fileClass.equalsIgnoreCase("pdf")) {
                        this.spec = this.envCS.getFileClassSpec(fileClass);
                        this.envCS.addFileClass(this.spec);
                        this.envCS.addFile(fname, fileClass);
                        /////////////////////
                        this.spec = this.envCI.getFileClassSpec(fileClass);
                        this.envCI.addFileClass(this.spec);
                        this.envCI.addFile(fname, fileClass);
                        //this.notifyIndexedDocument();

                    } else if (fileClass.equalsIgnoreCase("java")) {

                        this.spec = this.envCS.getFileClassSpec("txt");
                        this.envCS.addFileClass(this.spec);
                        this.envCS.addFile(fname, "txt");
                        //////////////////
                        this.spec = this.envCI.getFileClassSpec("txt");
                        this.envCI.addFileClass(this.spec);
                        this.envCI.addFile(fname, "txt");
                        //  this.notifyIndexedDocument();

                    }
                    if (operation == ADD_INDEX) {
                        this.notifyAddedDocument();
                    } else if (operation == MAKE_INDEX) {
                        this.notifyIndexedDocument();
                    }
                    ///stadistics
                    totalDocumentsIndexed = this.envCS.documentsIndexed();
                    this.indexedDocsCount = totalDocumentsIndexed;
                } else {

                    String message = "There are files in the collection that are not: .java, pdf o txt documents" + "\n" + "so, they could not be indexed.";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                }

            }
            this.envCS.close();
            this.envCI.close();

        } catch (Exception e) {
            // a lemur exception was tossed
            this.notifyTaskProgress(ERROR_MESSAGE, this.idxCS.getPath() + "\n" + e + "\n");
            this.notifyTaskProgress(ERROR_MESSAGE, this.idxCI.getPath() + "\n" + e + "\n");
            e.printStackTrace();

        }
        this.notifyTaskProgress(INFORMATION_MESSAGE, "Finished building " + this.idxCS.getPath() + "\n");
        this.notifyTaskProgress(totalDocumentsIndexed, "Total documents indexed: " + totalDocumentsIndexed + "\n\n");

        return totalDocumentsIndexed;
    }

    /**
     * Indexa una colección dado un file
     */
    private int indexDocs(File data, int operation) throws IndexException {
        int totalDocumentsIndexed = 0;
        this.envCS = new IndexEnvironment();
        this.envCI = new IndexEnvironment();
        this.status = new UIIndexStatus();
        this.spec = null;

        try {
            // memory
            this.envCS.setMemory(encodeMem());
            this.envCI.setMemory(encodeMem());

            //indice case sensitive
            this.envCS.setNormalization(false);
            this.envCI.setNormalization(true);

            //stopwords
            this.envCS.setStopwords(ENGLISH_STOP_WORDS);
            this.envCI.setStopwords(ENGLISH_STOP_WORDS);


            //get all the filespaths
            String[] datafiles = dataFilesList(data, new ArrayList<String>());

            // create a new empty index (after parameters have been set).
            if (this.appendIndex) {
                this.envCS.open(this.idxCS.getPath(), this.status);
                this.envCI.open(this.idxCI.getPath(), this.status);
            } else {
                this.envCS.create(this.idxCS.getPath(), this.status);
                this.envCI.create(this.idxCI.getPath(), this.status);
            }

            // do the building
            String fname, fileClass;
            for (int i = 0; i < datafiles.length; i++) {
                fname = datafiles[i];
                fileClass = getFileExtension(fname);

                if (fileClass.equalsIgnoreCase("java") || fileClass.equalsIgnoreCase("pdf") || fileClass.equalsIgnoreCase("txt")) {

                    if (fileClass.equalsIgnoreCase("txt") || fileClass.equalsIgnoreCase("pdf")) {
                        this.spec = this.envCS.getFileClassSpec(fileClass);
                        this.envCS.addFileClass(this.spec);
                        this.envCS.addFile(fname, fileClass);
                        /////////////////////
                        this.spec = this.envCI.getFileClassSpec(fileClass);
                        this.envCI.addFileClass(this.spec);
                        this.envCI.addFile(fname, fileClass);
                        //this.notifyIndexedDocument();

                    } else if (fileClass.equalsIgnoreCase("java")) {

                        this.spec = this.envCS.getFileClassSpec("txt");
                        this.envCS.addFileClass(this.spec);
                        this.envCS.addFile(fname, "txt");
                        //////////////////
                        this.spec = this.envCI.getFileClassSpec("txt");
                        this.envCI.addFileClass(this.spec);
                        this.envCI.addFile(fname, "txt");
                        //this.notifyIndexedDocument();

                    }
                    if (operation == ADD_INDEX) {
                        this.notifyAddedDocument();
                    } else if (operation == MAKE_INDEX) {
                        this.notifyIndexedDocument();
                    }
                    ///stadistics
                    totalDocumentsIndexed = this.envCS.documentsIndexed();
                    this.indexedDocsCount = totalDocumentsIndexed;
                } else {
                    String message = "There are files in the collection that are not: .java, pdf o txt documents" + "\n" + "so, they could not be indexed.";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                }


            }
            this.envCS.close();
            this.envCI.close();
        } catch (Exception e) {
            // a lemur exception was tossed
            this.notifyTaskProgress(ERROR_MESSAGE, this.idxCS.getPath() + "\n" + e + "\n");
            this.notifyTaskProgress(ERROR_MESSAGE, this.idxCI.getPath() + "\n" + e + "\n");
            e.printStackTrace();
        }
        this.notifyTaskProgress(INFORMATION_MESSAGE, "Finished building " + this.idxCS.getPath() + "\n");
        this.notifyTaskProgress(totalDocumentsIndexed, "Total documents indexed: " + totalDocumentsIndexed + "\n\n");

        return totalDocumentsIndexed;
    }

    /**
     * codifica la memoria a utilizar
     */
    private long encodeMem() {
        String s = "512000000";
        long localRetval = 0;
        try {
            localRetval = Long.parseLong(s);
        } catch (Exception e) {
        }
        return localRetval;
    }

    private String[] dataFilesList(File dirPath, List<String> list) {

        // do not try to index files that cannot be read
        if (dirPath.canRead()) {
            if (dirPath.isDirectory()) {
                File[] files = dirPath.listFiles();
                // an IO error could occur
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        dataFilesList(files[i], list);
                    }
                }
            } else {
                list.add(dirPath.getAbsolutePath());
            }

        }
        this.retval = new String[0];
        this.retval = list.toArray(this.retval);
        return this.retval;
    }

    private String[] dataFilesList(List<File> dirPath, List<String> list) {
        File file;
        for (int i = 0; i < dirPath.size(); i++) {
            file = dirPath.get(i);
            if (file.canRead()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (int j = 0; j < files.length; j++) {
                            dataFilesList(files[j], list);
                        }
                    }
                } else {
                    list.add(file.getAbsolutePath());
                }

            }
        }

        this.retval = new String[0];
        this.retval = list.toArray(this.retval);
        return this.retval;
    }

    /**
     * Guarda los resultados de búsqueda
     *
     *
     */
    private ArrayList<DocumentMetaData> saveResults(QueryResults sd, String queryT, QueryEnvironment q) throws IOException {

        ArrayList<DocumentMetaData> arraylist = new ArrayList<DocumentMetaData>();
        DocumentMetaData metaDoc = null;
        String pathdoc = null;
        String summary = null;
        this.queryResults = sd.results;

        try {
            this.theIndex = lemurproject.lemur.IndexManager.openIndex(this.indexPath.getPath());

        } catch (Exception ex) {
            OutputMonitor.printStream("", ex);
        }

        for (int i = 0; i < this.queryResults.length; i++) {
            metaDoc = new DocumentMetaData();
            try {
                this.queryResultObj = this.queryResults[i];
                int iddoc = this.queryResultObj.docid;
                double score = this.queryResultObj.score;

                summary = this.queryResultObj.snippet;
                if (summary == null) {
                    summary = " ";
                }
                pathdoc = this.theIndex.document(iddoc);
                File file = new File(pathdoc);

                metaDoc.setIndex(iddoc);
                metaDoc.setScore(score);
                metaDoc.setPath(pathdoc);
                metaDoc.setName(file.getName());
                metaDoc.setSize(file.length());
                metaDoc.setSynthesis(summary);
                metaDoc.setType(getFileExtension(pathdoc));
                metaDoc.setSearcher(KeySearchable.INDRI_SEARCH_ENGINE);
                arraylist.add(metaDoc);
            } catch (Exception ex) {
                OutputMonitor.printStream("", ex);
            }

        }
        return arraylist;
    }

    /**
     * {@inheritDoc}
     *
     * 
     */
    public boolean safeToBuildIndex(File indexP, int operation) throws IndexException {
        this.appendIndex = false;
        String idxstring = indexP.getPath();
        String message = null;
        boolean flag = true;

        if (indexP.exists()) { //verifica si ya esta creado el indice

            if (indexP.listFiles().length == 2) {
                this.idxCS = new File(idxstring.concat("/casesensitive"));
                this.idxCI = new File(idxstring.concat("/caseinsensitive"));
            } else { //create folders
                this.idxCS = new File(idxstring.concat("/casesensitive"));
                this.idxCI = new File(idxstring.concat("/caseinsensitive"));
                if (!this.idxCS.mkdirs() && !this.idxCI.mkdirs()) {
                    //ensure that the index folder exists
                    message = "ERROR: Could not create the index folders at: " + this.idxCS.getPath() + ".\n" + this.idxCI.getPath() + "Aborting indexing process.";
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    flag = false;
                    throw new IndexException(message);
                }

            }
        } else { //create folders
            this.idxCS = new File(idxstring.concat("/casesensitive"));
            this.idxCI = new File(idxstring.concat("/caseinsensitive"));
            if (!this.idxCS.mkdirs() && !this.idxCI.mkdirs()) {
                //ensure that the index folder exists
                message = "ERROR: Could not create the index folders at: " + this.idxCS.getPath() + ".\n" + this.idxCI.getPath() + "Aborting indexing process.";
                this.notifyTaskProgress(ERROR_MESSAGE, message);
                flag = false;
                throw new IndexException(message);
            }
        }

        /* if (!this.idxCS.exists() && !this.idxCI.exists()) {
        this.idxCS = new File(idxstring.concat("/casesensitive"));
        this.idxCI = new File(idxstring.concat("/caseinsensitive"));
        if (!this.idxCS.mkdirs() && !this.idxCI.mkdirs()) {
        //ensure that the index folder exists
        message = "ERROR: Could not create the index folders at: " + this.idxCS.getPath() + ".\n" + this.idxCI.getPath() + "Aborting indexing process.";
        this.notifyTaskProgress(Assignable.ERROR_MESSAGE, message);
        return false;
        }
        }*/
        File manifest = new File(this.idxCS.getPath(), "manifest");
        File manifest2 = new File(this.idxCI.getPath(), "manifest");

        if (manifest.exists() && manifest2.exists()) {
            switch (operation) {
                case MAKE_INDEX:

                    message = "Overwriting index " + idxstring + "\n";
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    deleteFiles(this.idxCS);
                    deleteFiles(this.idxCI);
                    flag = true;
                    break;

                case ADD_INDEX:

                    message = "Appending new files to index " + idxstring + "\n";
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    this.appendIndex = true;
                    flag = true;
                    break;

                default:
                    message = "Not building index " + idxstring + "\n";
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    flag = false;
                    throw new IndexException(message);

            }


        } else if (operation == ADD_INDEX) {
            message = "ERROR: No Indri index exist in this address" + idxstring;
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            flag = false;
            throw new IndexException(message);
        }


        return flag;
    }

    /**
     * Verifica que en un path dado exista un índice indri
     * 
     * @param path
     * @return
     * @throws IndexException
     */
    public boolean checkIndexPath(String path) throws IndexException {

        if (!path.contains("casesensitive") && !path.contains("caseinsensitive")) {
            this.indexPath = new File(path.concat("/casesensitive"));
            this.indexPathCI = new File(path.concat("/caseinsensitive"));
        } else {
            this.indexPath = new File(path);
            this.indexPathCI = new File(path);
        }

        Index theNewIndex = null;
        try {
            if (this.indexPath.exists()) {
                theNewIndex = IndexManager.openIndex(this.indexPath.getPath());
            } else {
                this.notifyTaskProgress(ERROR_MESSAGE, "Index does not exist");
                throw new IndexException("Index does not exist");
            }

        } catch (Exception ex) {
            OutputMonitor.printStream("", ex);
        }

        if (theNewIndex != null) {
            return true;
        } else {
            return false;
        }
    }

    class UIIndexStatus extends IndexStatus {

        public void status(int code, String documentFile, String error,
                int documentsIndexed, int documentsSeen) {
            if (code == action_code.FileOpen.swigValue()) {
                notifyTaskProgress(INFORMATION_MESSAGE, "Documents: " + documentsIndexed + "\n");
                notifyTaskProgress(INFORMATION_MESSAGE, "Opened " + documentFile + "\n");
            } else if (code == action_code.FileSkip.swigValue()) {
                notifyTaskProgress(INFORMATION_MESSAGE, "Skipped " + documentFile + "\n");
            } else if (code == action_code.FileError.swigValue()) {
                notifyTaskProgress(INFORMATION_MESSAGE, "Error in " + documentFile + " : " + error + "\n");
            } else if (code == action_code.DocumentCount.swigValue()) {
                if ((documentsIndexed % 500) == 0) {
                    notifyTaskProgress(INFORMATION_MESSAGE, "Documents: " + documentsIndexed + "\n");
                }
            }

        }
    }
    private static final String[] ENGLISH_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it", "no", "not", "of",
        "on", "or", "such", "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with"
    };
}
