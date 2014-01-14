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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uk.ac.gla.terrier.indexing.BasicIndexer;
import uk.ac.gla.terrier.indexing.BasicSinglePassIndexer;
import uk.ac.gla.terrier.indexing.BlockIndexer;
import uk.ac.gla.terrier.indexing.BlockSinglePassIndexer;
import uk.ac.gla.terrier.indexing.Collection;
import uk.ac.gla.terrier.indexing.Indexer;
import uk.ac.gla.terrier.indexing.SimpleFileCollection;
import uk.ac.gla.terrier.matching.ResultSet;
import uk.ac.gla.terrier.querying.Manager;
import uk.ac.gla.terrier.querying.SearchRequest;
import uk.ac.gla.terrier.querying.parser.Query;
import uk.ac.gla.terrier.querying.parser.QueryParser;
import uk.ac.gla.terrier.structures.Index;
import uk.ac.gla.terrier.utility.ApplicationSetup;
import uk.ac.gla.terrier.utility.Files;

/**
 * Clase que instancia el motor de búsqueda Terrier versión 2.1
 *
 * 
 */
public final class TerrierContext extends EngineContextAdapter {

    public Indexer indexer;
    public Manager queryingManager;
    public String mModel = ApplicationSetup.getProperty("desktop.matching", "Matching");
    public String wModel = ApplicationSetup.getProperty("desktop.model", "PL2");
    public Index diskIndex;
    public SimpleFileCollection sfc; //to build the index
    public Query queryTerrier;
    public SearchRequest searchRequest;
    public ResultSet result;
    private boolean indexing;
    String message;

    /**
     * default constructor
     */
    public TerrierContext() {
        this.defaultIndexPath = "./index/terrier/";
        setIndexing(false);
    }

    /**
     * constructor
     *
     * @param listener  --oyente de los procesos de este motor
     */
    public TerrierContext(FacadeDesktopListener listener) {
        super(listener);
        this.defaultIndexPath = "./index/terrier/";
        setIndexing(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {

        setStartTimeOfSearch(new Date());
        boolean flag = false;
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> docindexed = new ArrayList<DocumentMetaData>();
        
        ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();

        //Verificar el directorio donde se encuentra el indice
        if (this.indexPath == null || this.indexPath.listFiles().length == 0) {
            this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
        } else {
            flag = true;
        }

        if (flag) {

            try {
                this.queryTerrier = null;
                try {
                    this.queryTerrier = QueryParser.parseQuery(query);
                } catch (Exception e) {
                    //remove everything except character and spaces, and retry
                    //   this.queryTerrier = QueryParser.parseQuery(query.replaceAll("[^a-zA-Z0-9 ]", ""));
                    OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    throw new SearchException(e.getMessage());
                }
                if (setManager()) {
                    this.searchRequest = this.queryingManager.newSearchRequest();
                    this.searchRequest.setQuery(this.queryTerrier);
                    this.searchRequest.addMatchingModel(this.mModel, this.wModel);
                    this.searchRequest.setControl("c", "1.0d"); //nombre y valor del controlador

                    this.queryingManager.runPreProcessing(this.searchRequest);
                    this.queryingManager.runMatching(this.searchRequest);
                    this.queryingManager.runPostProcessing(this.searchRequest);
                    this.queryingManager.runPostFilters(this.searchRequest);
                    this.result = this.searchRequest.getResultSet();

                    docindexed = saveResults(this.result);
                    //eliminar repetidos
                    deleteRepeated(docindexed);
                    finalResultsList = docindexed;
                    this.finalMetaResult = docindexed;
                    setEndTimeOfSearch(new Date());
                    message = "Terrier retrieved " + this.result.getResultSize() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'.";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);

                } else {
                    this.notifyTaskProgress(ERROR_MESSAGE, "The manager wasn't charged");
                }
            } catch (Exception e) {
                message = "An exception when running the query: '" + query + "', Error: " + e.getMessage();
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, message);
                throw new SearchException(message);
            }
        }

        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        setStartTimeOfSearch(new Date());
        ArrayList<DocumentMetaData> searchResults = search(query, caseSensitive);
        finalResultsList = this.filterMetaDocuments(docType, searchResults);

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        message = "Terrier retrieved " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for " + docType;
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        message = "Terrier retrieved " + this.finalMetaResult.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "for doctypes";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex() throws IndexException {

        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = new File(this.defaultCollectionPath);

        long indexedFiles = 0;

        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();
            indexedFiles = this.build();
        }

        this.indexedDocsCount += indexedFiles;
        return indexedFiles;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(File collectionPath) throws IndexException {

        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;

        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();
            indexedFiles = this.build();
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(List<File> collectionPath) throws IndexException {

        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;

        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();
            indexedFiles = this.build(collectionPath);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        long indexedFiles = 0;
        this.collectionPath = collectionPath;
        this.indexPath = indexPath;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            message = collectionPath + "does not exist or is empty";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();
            indexedFiles = this.build();
        } else {
            message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {

        long indexedFiles = 0;
        this.indexPath = indexPath;

        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            ApplicationSetup.TERRIER_INDEX_PATH = this.indexPath.getAbsolutePath();
            indexedFiles = this.build(collectionPath);
        } else {
            message = "indexPath is null";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadIndex() throws IndexException {
        File defaultFile = new File(this.defaultIndexPath);
        ApplicationSetup.TERRIER_INDEX_PATH = defaultFile.getAbsolutePath();

        if (defaultFile.exists() && Index.existsIndex(ApplicationSetup.TERRIER_INDEX_PATH, "data")) {
            OutputMonitor.printLine("Loading Terrier... ");
            if (this.diskIndex == null) {
                this.diskIndex = Index.createIndex();
            }
            File[] list = defaultFile.listFiles();

            if (this.diskIndex == null || list.length == 0) {
                if (this.diskIndex != null) {
                    this.diskIndex.close();
                }
                this.diskIndex = null;
                return false;
            } else if (this.diskIndex != null && list.length != 0) {
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Terrier...");
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Documents: " + this.diskIndex.getCollectionStatistics().getNumberOfDocuments());
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Tokens: " + this.diskIndex.getCollectionStatistics().getNumberOfTokens());
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Unique Terms: " + this.diskIndex.getCollectionStatistics().getNumberOfUniqueTerms());
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Pointers: " + this.diskIndex.getCollectionStatistics().getNumberOfPointers());


                if (isIndexing()) {
                    this.notifyIndexedDocumentTerrier(this.diskIndex.getCollectionStatistics().getNumberOfDocuments());
                } else {
                    this.notifyLoadedDocument(this.diskIndex.getCollectionStatistics().getNumberOfDocuments());

                    message = "Number of docs loaded: " + this.diskIndex.getCollectionStatistics().getNumberOfDocuments();
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

                    //set path for search
                    this.indexPath = new File(ApplicationSetup.TERRIER_INDEX_PATH);
                }

            }
        } else {
            this.notifyTaskProgress(ERROR_MESSAGE, "The index has not been created");
            throw new IndexException("The index has not been created");
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadIndex(File indexPath) throws IndexException {
        boolean flag = false;

        // verificar que sea un indice terrier
        if (Index.existsIndex(indexPath.getPath(), "data")) {
            OutputMonitor.printLine("Loading Terrier... ");
            ApplicationSetup.TERRIER_INDEX_PATH = indexPath.getPath();
            if (this.diskIndex == null) {
                this.diskIndex = Index.createIndex();
            }
            File[] list = indexPath.listFiles();

            if (this.diskIndex != null && list.length != 0) {

                this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Terrier...");
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Documents: " + this.diskIndex.getCollectionStatistics().getNumberOfDocuments() + "\n");
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Tokens: " + this.diskIndex.getCollectionStatistics().getNumberOfTokens() + "\n");
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Unique Terms: " + this.diskIndex.getCollectionStatistics().getNumberOfUniqueTerms() + "\n");
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Number of Pointers: " + this.diskIndex.getCollectionStatistics().getNumberOfPointers() + "\n");

                if (isIndexing()) {
                    this.notifyIndexedDocumentTerrier(this.diskIndex.getCollectionStatistics().getNumberOfDocuments());
                } else if (isIndexing() == false) {
                    this.notifyLoadedDocument(this.diskIndex.getCollectionStatistics().getNumberOfDocuments());

                    message = "Number of docs loaded: " + this.diskIndex.getCollectionStatistics().getNumberOfDocuments();
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

                    
                    //set path for search
                    this.indexPath = new File(ApplicationSetup.TERRIER_INDEX_PATH);
                }


                flag = true;
            } else {
                this.notifyTaskProgress(ERROR_MESSAGE, "no se puede cargar el índice");
            }

        } else {
            throw new IndexException("No existe un índice en este directorio: " + indexPath.getPath());
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     * 
     */
    public boolean safeToBuildIndex(File idx) throws IndexException {
        boolean flag = true;
        if (!idx.exists()) {

            if (!idx.mkdirs()) {
                //ensure that the index folder exists
                String msg = "ERROR: Could not create the index folders at: " + idx.getPath() + ".\n" + "Aborting indexing process.";
                this.notifyTaskProgress(ERROR_MESSAGE, msg);
                flag = false;
                throw new IndexException(msg);
            }
        } else if (idx.exists() && idx.listFiles().length != 0) {

            message = "Overwriting index " + idx + "\n";
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            deleteFiles(idx);
            flag = true;

        }

        return flag;
    }

    /**
     *  Método para construir el índice con la colección por defecto
     *
     */
    private long build() throws IndexException {

        long indexedFiles = 0;

        setStartTimeOfIndexation(new Date());
        message = "Indexing directory '" + this.collectionPath + "'...";
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        try {
            this.diskIndex = Index.createIndex();
            //deleting existing files
            if (this.diskIndex != null) {
                this.diskIndex.close();
                this.diskIndex = null;
            }

            if (safeToBuildIndex(this.indexPath)) {
                //determinar indexer
                final boolean useSinglePass = Boolean.parseBoolean(ApplicationSetup.getProperty("desktop.indexing.singlepass", "false"));
                this.indexer = ApplicationSetup.BLOCK_INDEXING
                        ? useSinglePass
                        ? new BlockSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : new BlockIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : useSinglePass
                        ? new BasicSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : new BasicIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);


                //get all the filespaths to index
                List<String> foldersList = dataFilesList(this.collectionPath, new ArrayList<String>());

                List<String> newList = verifyDocumentType(foldersList);
                if (!newList.isEmpty()) {
                    this.indexedDocsCount = foldersList.size();
                    this.sfc = new SimpleFileCollection(foldersList, true);
                    this.indexer.index(new Collection[]{this.sfc});   //crea el indice
                    System.gc();

                    //abrir la lista de files de SimpleFileCollection
                    List<String> fileList = this.sfc.getFileList();
                    savePathsList(new File(ApplicationSetup.makeAbsolute(ApplicationSetup.getProperty("desktop.directories.filelist", "data.filelist"),
                            ApplicationSetup.TERRIER_INDEX_PATH)), fileList);

                    //verificar que se creo el indice
                    setIndexing(true);
                    if (loadIndex()) {
                        message = "El proceso de indexación ha terminado correctamente.\n"
                                + "--------- Indexed files ---------";
                        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

                        indexedFiles = fileList.size();
                        String file;
                        for (int i = 0; i < indexedFiles; i++) {
                            file = fileList.get(i);
                            this.notifyTaskProgress(INFORMATION_MESSAGE, " - " + file);
                        }

                    } else {
                        this.notifyTaskProgress(ERROR_MESSAGE, "Problems when charging the index");
                        throw new IndexException("Problems when charging the index");
                    }
                    setIndexing(false);
                    setEndTimeOfIndexation(new Date());
                    String time = "IndexationTime " + getIndexationTime() + " milliseconds";
                    this.notifyTaskProgress(INFORMATION_MESSAGE, time);

                } else {
                    this.notifyTaskProgress(ERROR_MESSAGE, "There are not files for indexing.");
                    throw new IndexException("There are not files for indexing.");
                }

            }
        } catch (Exception e) {
            message = "Class: SearchEngineTerrier\n"
                    + " Message: An unexpected exception occured while indexing. Indexing has been aborted.\n";
            this.notifyTaskProgress(INFORMATION_MESSAGE, message + " Error: " + e.getMessage());
            throw new IndexException(message);

        }

        return indexedFiles;

    }

    /**
     *   Método para construir el índice a partir de una colección de files
     *
     */
    private long build(List<File> collectionPath) throws IndexException {

        long indexedFiles = 0;
        setStartTimeOfIndexation(new Date());
        message = "Indexing to directory '" + this.indexPath + "'...";
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        try {
            this.diskIndex = Index.createIndex();
            //deleting existing files
            if (this.diskIndex != null) {
                this.diskIndex.close();
                this.diskIndex = null;
            }

            if (safeToBuildIndex(this.indexPath)) {
                //determinar indexer
                final boolean useSinglePass = Boolean.parseBoolean(ApplicationSetup.getProperty("desktop.indexing.singlepass", "false"));
                this.indexer = ApplicationSetup.BLOCK_INDEXING
                        ? useSinglePass
                        ? new BlockSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : new BlockIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : useSinglePass
                        ? new BasicSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
                        : new BasicIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);

                //get all the filespaths to index
                List<String> foldersList = dataFilesList(collectionPath, new ArrayList<String>());

                List<String> newList = verifyDocumentType(foldersList);
                if (!newList.isEmpty()) {
                    this.indexedDocsCount = foldersList.size();
                    this.sfc = new SimpleFileCollection(foldersList, true);
                    this.indexer.index(new Collection[]{this.sfc});   //crea el indice
                    System.gc();
                    //abrir la lista de files de SimpleFileCollection
                    List<String> fileList = this.sfc.getFileList();
                    savePathsList(new File(ApplicationSetup.makeAbsolute(ApplicationSetup.getProperty("desktop.directories.filelist", "data.filelist"),
                            ApplicationSetup.TERRIER_INDEX_PATH)), fileList);
                    //verificar que se creo el indice
                    setIndexing(true);
                    if (loadIndex()) {
                        message = "El proceso de indexación ha terminado correctamente.\n"
                                + "--------- Indexed files ---------";
                        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                        String file;
                        for (int i = 0; i < fileList.size(); i++) {
                            file = fileList.get(i);
                            this.notifyTaskProgress(INFORMATION_MESSAGE, " - " + file);
                        }

                    } else {
                        this.notifyTaskProgress(ERROR_MESSAGE, "Problems when charging the index");
                        throw new IndexException("There are not files for indexing.");
                    }
                    setIndexing(false);
                    setEndTimeOfIndexation(new Date());
                    String time = "IndexationTime " + getIndexationTime() + " milliseconds";
                    this.notifyTaskProgress(INFORMATION_MESSAGE, time);

                } else {
                    this.notifyTaskProgress(ERROR_MESSAGE, "There are not files for indexing.");
                    throw new IndexException("There are not files for indexing.");
                }
            }
        } catch (Exception e) {
            message = "Class: SearchEngineTerrier\n"
                    + " Message: An unexpected exception occured while indexing. Indexing has been aborted.\n";
            this.notifyTaskProgress(INFORMATION_MESSAGE, message + " Error: " + e.getMessage());
            throw new IndexException(message +e.getMessage());
        }

        indexedFiles = this.indexedDocsCount;
        return indexedFiles;
    }

    /**
     * Devuelve en un a lista de String los paths de todos los files de una lista de directorios
     *
     * @param dirPath --lista de los files
     * @param list    --lista vacía
     *
     * @return        --lista con los path
     */
    private List<String> dataFilesList(List<File> dirPath, List<String> list) {
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
                    list.add(file.getPath());
                }

            }
        }

        return list;
    }

    /**
     * Devuelve en un a lista de String los paths de todos los files de un directorio
     *
     * @param dirPath --directorio de files
     * @param list    --lista vacía
     *
     * @return        --lista con los path
     */
    private List<String> dataFilesList(File dirPath, List<String> list) {

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
                list.add(dirPath.getPath());
            }

        }

        return list;
    }

    /**
     * Guarda una lista de paths en un file
     * @param file
     * @param list
     */
    private void savePathsList(File file, List<String> list) {
        try {
            PrintWriter writer = new PrintWriter(
                    Files.writeFileWriter(file));
            for (int i = 0; i < list.size(); i++) {
                writer.println(list.get(i));
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Saving " + list.get(i));
            }
            writer.close();
        } catch (IOException ioe) {
            String error = "Error writing to file : " + file + " : " + ioe.getMessage();
            this.notifyTaskProgress(ERROR_MESSAGE, error);
            OutputMonitor.printStream(error, ioe);
            return;
        }
    }

    /**
     * Carga una lista de los archivos de un directorio
     * @param file
     * @return
     */
    private List<String> loadList(File file) {
        if (file == null || !file.exists()) {
            return new ArrayList<String>();
        }
        ArrayList<String> out = new ArrayList<String>();
        try {
            BufferedReader buf = Files.openFileReader(file);
            String line;
            while ((line = buf.readLine()) != null) {
                //ignore empty lines, or lines starting with # from the methods
                // file.
                if (line.startsWith("#") || line.equals("")) {
                    continue;
                }
                out.add(line.trim());
            }
            buf.close();
        } catch (IOException ex) {
            OutputMonitor.printStream("IO", ex);
        }
        return out;
    }

    /**
     * Prepara la aplicación para iniciar el proceso de consultas
     *
     * @return
     */
    private boolean setManager() {
        Index otherindex = Index.createIndex();
        String managerName = ApplicationSetup.getProperty("desktop.manager", "Manager");
        try {
            if (managerName.indexOf('.') == -1) {
                managerName = "uk.ac.gla.terrier.querying." + managerName;
            }
            this.queryingManager = (uk.ac.gla.terrier.querying.Manager) (Class.forName(managerName).getConstructor(new Class[]{Index.class}).newInstance(new Object[]{otherindex}));

        } catch (Exception e) {
            String error = "Problem loading Manager (" + managerName + "): " + e.getMessage();
            this.notifyTaskProgress(ERROR_MESSAGE, error);
            return false;
        }
        if (this.queryingManager == null) {
            return false;
        }
        return true;
    }

    /**
     * Guarda los resultados obtenidos
     * @param r
     * @return
     * @throws IOException
     */
    private ArrayList<DocumentMetaData> saveResults(ResultSet r) throws IOException {

        ArrayList<DocumentMetaData> list = new ArrayList<DocumentMetaData>();
        DocumentMetaData metaDoc;
        List<String> indexedFiles = loadList(new File(ApplicationSetup.makeAbsolute(
                ApplicationSetup.getProperty("desktop.directories.filelist",
                "data.filelist"), ApplicationSetup.TERRIER_INDEX_PATH)));

        int[] docIds = r.getDocids();
        double[] scores = r.getScores();
        int docId;
        String path, name, fileType;
        File f;
        long size;

        for (int i = 0; i < r.getResultSize(); i++) {
            metaDoc = new DocumentMetaData();
            docId = docIds[i];
            path = indexedFiles.get(docId);

            f = new File(path);
            name = f.getName();
            fileType = getFileExtension(path);

            size = f.length();

            metaDoc.setIndex(docId);
            metaDoc.setName(name);
            metaDoc.setPath(path);
            metaDoc.setSize(size);
            metaDoc.setType(fileType);
            metaDoc.setSynthesis(null); //doesn´t have
            metaDoc.setScore(scores[i]);
            metaDoc.setSearcher(KeySearchable.TERRIER_SEARCH_ENGINE);
            list.add(metaDoc);
        }

        this.retrievedDocsCount = list.size();
        return list;
    }

    /**
     * Devuelve una lista con los documentos a indexar
     * de tipo java txt o pdf
     * 
     * @param listPath
     * @return
     */
    public List<String> verifyDocumentType(List<String> listPath) {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < listPath.size(); i++) {
            String string = listPath.get(i);
            String type = getFileExtension(string);
            if (type.equalsIgnoreCase("java") || type.equalsIgnoreCase("pdf") || type.equalsIgnoreCase("txt")) {
                list.add(string);
            } else {
                message = "There are files in the collection that are not: .java, pdf o txt documents" + "\n" + "so, they could not be indexed.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            }
        }
        return list;
    }

    /**
     * @return the indexing
     */
    public boolean isIndexing() {
        return indexing;
    }

    /**
     * @param indexing the indexing to set
     */
    public void setIndexing(boolean indexing) {
        this.indexing = indexing;
    }

    public void setTerrierLocation() {
        File ftest = new File("./terrier/");

        if (ftest.exists()) {
            System.setProperty("terrier.home", ftest.getAbsolutePath());
        } else {
            OutputMonitor.printLine("Problem loading terrier configuration files", OutputMonitor.ERROR_MESSAGE);
        }

        ApplicationSetup.BLOCK_INDEXING = true;

        if ((ApplicationSetup.getProperty("querying.allowed.controls", null)) == null) {
            ApplicationSetup.setProperty("querying.allowed.controls", "c,start,end,qe");
        }
        if ((ApplicationSetup.getProperty("querying.postprocesses.order", null)) == null) {
            ApplicationSetup.setProperty("querying.postprocesses.order", "QueryExpansion");
        }
        if ((ApplicationSetup.getProperty("querying.postprocesses.controls", null)) == null) {
            ApplicationSetup.setProperty("querying.postprocesses.controls", "qe:QueryExpansion");
        }
        ApplicationSetup.setProperty("indexing.max.tokens", "10000");
        ApplicationSetup.setProperty("invertedfile.processterms", "25000");
        ApplicationSetup.setProperty("ignore.low.idf.terms", "false");
        ApplicationSetup.setProperty("matching.dsms", "BooleanFallback");
    }
}
