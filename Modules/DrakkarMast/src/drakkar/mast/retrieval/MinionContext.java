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
import static drakkar.oar.util.KeyField.*;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import drakkar.mast.retrieval.parser.JavaParser;
import drakkar.mast.retrieval.parser.PdfParser;
import com.sun.labs.minion.FieldInfo;
import com.sun.labs.minion.Passage;
import com.sun.labs.minion.PassageBuilder;
import com.sun.labs.minion.Result;
import com.sun.labs.minion.ResultSet;
import com.sun.labs.minion.SearchEngineException;
import com.sun.labs.minion.SearchEngineFactory;
import com.sun.labs.minion.SimpleIndexer;
import com.sun.labs.minion.TextHighlighter;
import com.sun.labs.minion.query.Element;
import com.sun.labs.minion.query.Or;
import com.sun.labs.minion.query.Term;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase que implementa el motor de búsqueda Minion, versión 1.0
 *
 * 
 */
public class MinionContext extends AdvEngineContext {

    private com.sun.labs.minion.SearchEngine engine;      //clase que controla el motor de minion
    private ResultSet resultSet;                          //estructura donde se almacenan los resultados de búsqueda
    private List<Element> collectionElements;           //tiene los elementos de la consulta
    private List<Result> allResults;
    private PassageBuilder passageBuilder;
    private TextHighlighter tHighlighter;

    /**
     *
     */
    public MinionContext() {
        defaultIndexPath = "./index/minion";
    }

    /**
     *
     * @param listener
     */
    public MinionContext(FacadeDesktopListener listener) {
        super(listener);
        defaultIndexPath = "./index/minion";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {

        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        this.finalMetaResult = new ArrayList<DocumentMetaData>();
        this.resultSet = null;
        String[] words = null;
        this.collectionElements = null;

        String[] codeAndBooks = new String[4];
        codeAndBooks[0] = getDocumentField(FIELD_CODE_ALL_SOURCE);
        codeAndBooks[1] = getDocumentField(FIELD_DOC_TEXT);
        codeAndBooks[2] = getDocumentField(FIELD_NAME);
        codeAndBooks[3] = getDocumentField(FIELD_DOC_BOOK);

        try {
            setStartTimeOfSearch(new Date());
            if (isMinionIndex(this.indexPath)) {
                this.engine = SearchEngineFactory.getSearchEngine(this.indexPath.getPath());
                //para analizar termino a termino de la consulta
                if (query.contains(" ")) {
                    words = query.split(" ");
                } else {
                    words = new String[1];
                    words[0] = query;
                }

                if (caseSensitive) {
                    this.collectionElements = new ArrayList<Element>();
                    String termonly, fieldtoprocess;
                    Element elem;
                    for (int i = 0; i < words.length; i++) {
                        termonly = words[i];
                        for (int j = 0; j < codeAndBooks.length; j++) {
                            fieldtoprocess = codeAndBooks[j];
                            elem = new Term(termonly, EnumSet.of(Term.Modifier.CASE, Term.Modifier.WILDCARD));
                            elem.addField(fieldtoprocess);
                            this.collectionElements.add(elem);
                        }
                    }
                    this.resultSet = this.engine.search(new Or(this.collectionElements));

                } else if (caseSensitive == false) {
                    this.collectionElements = new ArrayList<Element>();
                    String termonly, fieldToProcess;
                    Element elem;

                    for (int i = 0; i < words.length; i++) {
                        termonly = words[i];
                        for (int j = 0; j < codeAndBooks.length; j++) {
                            fieldToProcess = codeAndBooks[j];
                            elem = new Term(termonly, EnumSet.of(Term.Modifier.WILDCARD, Term.Modifier.STEM));
                            elem.addField(fieldToProcess);
                            this.collectionElements.add(elem);
                        }
                    }
                    this.resultSet = this.engine.search(new Or(this.collectionElements));
                }
                this.allResults = this.resultSet.getAllResults(true);
                //guardar resultados
                this.finalMetaResult = saveResults(this.allResults);

                //eliminar repetidos
                if (this.finalMetaResult.size() > 1) {
                    deleteRepeated(this.finalMetaResult);
                }

                this.engine.close();
                finalResultsList = this.finalMetaResult;
                setEndTimeOfSearch(new Date());
                String message = "Minion retrieved " + this.finalMetaResult.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }


        } catch (com.sun.labs.minion.SearchEngineException ex) {
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;

    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        //busca en toda la colección de documentos
        tempList = search(query, caseSensitive);

        //filtra los resultados por tipo de documento
        finalResultsList = this.filterMetaDocuments(docType, tempList);

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Minion retrieved  " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctype " + docType;
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        this.finalMetaResult = new ArrayList<DocumentMetaData>();
        String[] words = null;

        try {
            setStartTimeOfSearch(new Date());
            if (isMinionIndex(this.indexPath)) {
                this.engine = SearchEngineFactory.getSearchEngine(this.indexPath.getPath());
                if (query.contains(" ")) {
                    words = query.split(" ");
                } else {
                    words = new String[1];
                    words[0] = query;
                }
                if (caseSensitive) {
                    this.collectionElements = new ArrayList<Element>();
                    String termonly;
                    Element elem;
                    for (int i = 0; i < words.length; i++) {
                        termonly = words[i];
                        elem = new Term(termonly, EnumSet.of(Term.Modifier.CASE, Term.Modifier.WILDCARD));
                        elem.addField(getDocumentField(field));
                        this.collectionElements.add(elem);
                    }

                    this.resultSet = this.engine.search(new Or(this.collectionElements));

                } else if (caseSensitive == false) {
                    String termonly;
                    Element elem;
                    this.collectionElements = new ArrayList<Element>();
                    for (int i = 0; i < words.length; i++) {
                        termonly = words[i];
                        elem = new Term(termonly, EnumSet.of(Term.Modifier.WILDCARD, Term.Modifier.STEM));
                        elem.addField(getDocumentField(field));
                        this.collectionElements.add(elem);
                    }
                    this.resultSet = this.engine.search(new Or(this.collectionElements));
                }
                this.allResults = this.resultSet.getAllResults(true);

                //guardar resultados
                this.finalMetaResult = saveResults(this.allResults);

                //eliminar repetidos
                if (this.finalMetaResult.size() > 1) {
                    deleteRepeated(this.finalMetaResult);
                }

                finalResultsList = this.finalMetaResult;
                setEndTimeOfSearch(new Date());
                String message = "Minion retrieved  " + this.resultSet.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for" + field;
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                this.engine.close();
            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }

        } catch (com.sun.labs.minion.SearchEngineException ex) {
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        tempList = search(query, field, caseSensitive);

        finalResultsList = this.filterMetaDocuments(docType, tempList);

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Minion retrieved  " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for field and doctype.";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null, finalResult = new ArrayList<DocumentMetaData>();

        String docSource;
        for (int i = 0; i < this.documentalSource.size(); i++) {
            docSource = this.documentalSource.get(i);
            if (docSource.equalsIgnoreCase(docType)) {
                if (fields != null && fields.length > 0) {
                    for (Integer field : fields) {
                        if (field != 0) {
                            tempList = search(query, docType, field, caseSensitive);
                            if (tempList != null) {
                                finalResult.addAll(tempList);
                            }
                        }
                        this.deleteRepeated(finalResult);
                    }
                } else {
                    tempList = search(query, docType, caseSensitive);
                    this.retrievedDocsCount += tempList.size();
                    return tempList;
                }
            } else if (docType == null) {
                tempList = search(query, caseSensitive);
                this.retrievedDocsCount += tempList.size();
                return tempList;
            }
        }


        this.retrievedDocsCount += finalResult.size();
        return finalResult;
    }

    @Override
    public ArrayList<DocumentMetaData> search(String query, String[] docType, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null;
        ArrayList<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();

        String doc;
        for (int i = 0; i < docType.length; i++) {
            doc = docType[i];
            if (doc.equals("documents")) {
                tempList = search(query, doc, caseSensitive);
            } else {
                tempList = search(query, doc, fields, caseSensitive);
            }

            documents.addAll(tempList);
        }

        this.deleteRepeated(documents);
        this.retrievedDocsCount += documents.size();
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, int[] fields, boolean caseSensitive) throws SearchException {

        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        int fieldAnalize;
        for (int i = 0; i < fields.length; i++) {
            fieldAnalize = fields[i];
            tempList = search(query, fieldAnalize, caseSensitive);
            finalResultsList.addAll(tempList);
        }

        if (finalResultsList.size() > 1) {
            deleteRepeated(finalMetaResult);
        }

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Minion retrieved  " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for field and doctype.";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {

        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();

        setStartTimeOfSearch(new Date());

        for (int i = 0; i < docTypes.length; i++) {
            String doc = docTypes[i];
            if (doc.equals("documents")) {
                tempList = search(query, doc, caseSensitive);
            } else {
                tempList = search(query, doc, field, caseSensitive);
            }
            finalResultsList.addAll(tempList);

        }
        if (finalResultsList.size() > 1) {
            deleteRepeated(finalMetaResult);
        }

        this.finalMetaResult = finalResultsList;
        setEndTimeOfSearch(new Date());
        String message = "Minion retrieved  " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for field and doctype.";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;

    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {

        ArrayList<DocumentMetaData> tempList = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();

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
        String message = "Minion retrieved  " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctypes ";
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
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
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
    @Override
    public long makeIndex(File collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;

        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
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
    @Override
    public long makeIndex(List<File> collectionPath) throws IndexException {

        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;
        if (collectionPath.isEmpty()) {
            OutputMonitor.printLine("The collection does not have files", OutputMonitor.INFORMATION_MESSAGE);
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
    @Override
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {

        this.indexPath = indexPath;
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
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

        this.indexPath = indexPath;
        long indexedFiles = 0;
        if (collectionPath.isEmpty()) {
            OutputMonitor.printLine("The collection does not have files", OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateIndex(File collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
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
    @Override
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
    @Override
    public long updateIndex(File collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
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
    @Override
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        long indexedFiles = 0;

        if (collectionPath.isEmpty()) {
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, ADD_INDEX);
        } else {
            String message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
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
    public boolean loadIndex(File indexPath) throws IndexException {

        if (!indexPath.isDirectory() || !indexPath.exists() || indexPath == null || isMinionIndex(indexPath) == false) {
            throw new IndexException("Not found index in this path");

        } else {

            try {
                OutputMonitor.printLine("Loading Minion... ");
                this.engine = SearchEngineFactory.getSearchEngine(indexPath.getPath());
                int cant = this.engine.getNDocs();
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Minion...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.notifyLoadedDocument(cant);

                this.notifyTaskProgress(INFORMATION_MESSAGE, "Total of documents of the index: " + cant);

                //set path for search
                this.indexPath = indexPath;
                
                OutputMonitor.printLine("Total of documents of the index: " + cant, OutputMonitor.INFORMATION_MESSAGE);
                return true;
            } catch (com.sun.labs.minion.SearchEngineException ex) {
                this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
                throw new IndexException(ex.getMessage());
            }
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadIndex() throws IndexException {

        File defaultFile = new File(this.defaultIndexPath);

        try {
            if (!defaultFile.isDirectory() || !defaultFile.exists()) {
                throw new IndexException("Not found index");

            } else if (isMinionIndex(defaultFile)) {

                this.engine = SearchEngineFactory.getSearchEngine(this.defaultIndexPath);
                int cant = this.engine.getNDocs();
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Loading Minion...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.notifyLoadedDocument(cant);
                this.notifyTaskProgress(INFORMATION_MESSAGE, "Total of documents of the index: " + cant);

                //set path for search
                this.indexPath = defaultFile;
                
                return true;
            } else {
                return false;
            }
        } catch (com.sun.labs.minion.SearchEngineException ex) {
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new IndexException(ex.getMessage());
        }


    }

    /**
     * {@inheritDoc}
     * @throws IndexException 
     */
    @Override
    public boolean safeToBuildIndex(File indexPath, int operation) throws IndexException {
        File idx = indexPath;
        String idxpath = idx.getPath();
        boolean flag = true;

        if (!idx.exists()) {
            if (!idx.mkdirs()) {
                String message = "ERROR: Could not create the index folders at: " + idx.getPath() + ".\n" + "Aborting indexing process.";
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, message);
                flag = false;
                throw new IndexException(message);
            }
        }
        if (idx.exists() && isMinionIndex(idx)) {
            /*Proceso de indexación en Minion por default:
            si el directorio ya tiene un indice dentro este lo añade,
            si el doc que va a añadir tiene el mismo(key) que tiene uno previamente indexado
            entonces lo que hace es reemplazarlo.*/

            switch (operation) {
                case MAKE_INDEX:
                    // sobreescribir
                    String message = "Overwriting index " + idxpath + "\n";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    deleteFiles(idx);
                    flag = true;
                    break;

                case ADD_INDEX:
                    //añadir
                    message = "Appending new files to index " + idxpath + "\n";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    flag = true;
                    break;

                default:
                    message = "Not building index " + idxpath + "\n";
                    OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    flag = false;
                    throw new IndexException(message);

            }
        } else if (operation == ADD_INDEX) {
            String message = "ERROR: No Minion index exist in this address" + idx;
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            flag = false;
            throw new IndexException(message);
        }

        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentField(int field) {
        switch (field) {
            case FIELD_FILEPATH:
                return "filepath";
            case FIELD_NAME:
                return "name";
            case FIELD_CODE_PACKAGE:
                return "package";
            case FIELD_CODE_CLASSES_NAMES:
                return "classesnames";
            case FIELD_CODE_METHODS_NAMES:
                return "methodsnames";
            case FIELD_CODE_ALL_COMMENTS:
                return "allcomments";
            case FIELD_CODE_ALL_SOURCE:
                return "allsource";//todo el contenido del codigo
            case FIELD_CODE_VARIABLES_NAMES:
                return "classesvariables";
            case FIELD_CODE_JAVADOCS:
                return "javadocs";
            case FIELD_DOC_TEXT:
                return "content"; //todo el contenido del pdf
            case FIELD_DOC_BOOK:
                return "book";
            default:
                return null;
        }

    }

    /**
     *  Método para construir el índice con la colección por defecto
     *
     * @param operation --tipo de operacion a realizar con el índice: MAKE o ADD
     */
    private long build(int operation) throws IndexException {

        long indexedFiles = 0;

        setStartTimeOfIndexation(new Date());
        String message = "Minion index will be created at [" + this.indexPath + "]";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        //inicia la indexacion
        try {
            if (safeToBuildIndex(this.indexPath, operation)) {
                this.engine = SearchEngineFactory.getSearchEngine(this.indexPath.getPath()); //Gets the default configuration for an index in the given directory.
                defineFields(this.engine);
                SimpleIndexer si = this.engine.getSimpleIndexer();

                indexedFiles = indexDocs(si, this.collectionPath, operation);
                message = "Optimizing...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                setEndTimeOfIndexation(new Date());
                message = "Indexation Time " + this.getIndexationTime() + " milliseconds.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                si.finish();

                this.engine.close();
            }
        } catch (Exception e) {
            message = " caught a " + e.getClass() + "\n with message: " + e.getMessage() + ".";
            OutputMonitor.printStream(message, e);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        return indexedFiles;


    }

    /**
     *  Método para construir el índice a partir de una colección dada
     *
     * @param collectionPath    -- colección de files a indexar
     * @param operation         -- tipo de operacion a realizar con el índice: MAKE o ADD
     */
    private long build(List<File> collectionPath, int operation) throws IndexException {

        long indexedFiles = 0;
        setStartTimeOfIndexation(new Date());
        String message = "Minion index will be created at [" + this.indexPath + "]";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        //inicia la indexacion
        try {
            if (safeToBuildIndex(this.indexPath, operation)) {

                //obtiene la configuración por defecto para un índice en el directorio dado
                this.engine = SearchEngineFactory.getSearchEngine(this.indexPath.getPath());
                defineFields(this.engine);
                SimpleIndexer si = this.engine.getSimpleIndexer();

                indexedFiles = indexDocs(si, collectionPath, operation);
                message = "Optimizing...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                setEndTimeOfIndexation(new Date());
                message = "Indexation Time " + this.getIndexationTime() + " milliseconds.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                si.finish();

                this.engine.close();
            }
        } catch (Exception e) {
            message = " caught a " + e.getClass() + "\n with message: " + e.getMessage() + ".";
            OutputMonitor.printStream(message, e);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

            throw new IndexException(message);
        }

        return indexedFiles;

    }

    /**
     * Establecer el tipo de campo y su nombre que se guardará en el índice
     * @param engine
     * @throws com.sun.labs.minion.SearchEngineException
     */
    private void defineFields(com.sun.labs.minion.SearchEngine engine) throws com.sun.labs.minion.SearchEngineException {

        this.engine = engine;

        EnumSet<FieldInfo.Attribute> ia = FieldInfo.getIndexedAttributes();
        EnumSet<FieldInfo.Attribute> enums = ia.clone();
        enums.add(FieldInfo.Attribute.CASE_SENSITIVE);
        enums.add(FieldInfo.Attribute.SAVED);
        enums.add(FieldInfo.Attribute.INDEXED);
        enums.add(FieldInfo.Attribute.TOKENIZED);//da error
        enums.add(FieldInfo.Attribute.TRIMMED);

        //Para todos
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_FILEPATH), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_NAME), enums, FieldInfo.Type.STRING));

        //Para código fuente
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_PACKAGE), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_CLASSES_NAMES), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_VARIABLES_NAMES), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_METHODS_NAMES), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_ALL_COMMENTS), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_ALL_SOURCE), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_CODE_JAVADOCS), enums, FieldInfo.Type.STRING));
        //Para libros y otros docs
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_DOC_TEXT), enums, FieldInfo.Type.STRING));
        this.engine.defineField(new FieldInfo(getDocumentField(FIELD_DOC_BOOK), enums, FieldInfo.Type.STRING));

    }

    /**
     * Verifica si en el directorio hay un indice minion
     * @param dir
     * @return
     */
    private boolean isMinionIndex(File dir) {
        String[] content = dir.list();
        String string;

        for (int i = 0; i < content.length; i++) {
            string = content[i];
            if (string.equalsIgnoreCase("config.xml")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indexa los documentos de diferentes ficheros
     * @param sind
     * @param fileDir
     * @throws IOException
     */
    private int indexDocs(SimpleIndexer simpleInd, List<File> fileDir, int operation) throws IndexException {
        int docCount = 0;
        File file;
        String message;
        for (int i = 0; i < fileDir.size(); i++) {
            file = fileDir.get(i);

            if (file.getName().endsWith(".java") || file.getName().endsWith(".pdf") || file.getName().endsWith(".txt")) {
                {
                    indexFile(simpleInd, file, operation);
                    message = "Adding: " + file;
                    docCount++;
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                }
            } else {
                message = "There are files in the collection that are not: .java, pdf o txt documents" + "\n" + "so, they could not be indexed.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            }
        }

        return docCount;
    }

    /**
     * Indexa los documentos   que se encuentran en un fichero
     * @param sind
     * @param fileDir
     * @throws IOException
     */
    private int indexDocs(SimpleIndexer simpleInd, File fileDir, int operation) throws IndexException {
        int docCount = 0;
        if (fileDir.canRead()) {
            if (fileDir.isDirectory()) {
                String[] files = fileDir.list();
                this.indexedDocsCount = files.length;
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        indexDocs(simpleInd, new File(fileDir, files[i]), operation);
                    }
                }
            } else if (fileDir.getName().endsWith(".java") || fileDir.getName().endsWith(".pdf") || fileDir.getName().endsWith(".txt")) {
                {
                    indexFile(simpleInd, fileDir, operation);
                    String temp = "Adding: " + fileDir;
                    docCount++;
                    OutputMonitor.printLine(temp, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, temp);
                }
            } else {
                String message = "There are files in the collection that are not: .java, pdf o txt documents" + "\n" + "so, they could not be indexed.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            }
        }
        return docCount;
    }

    /**
     * Indexacion por campo de cada documento del repositorio
     * @param simpleInd
     * @param f
     * @throws IOException
     */
    private void indexFile(SimpleIndexer simpleInd, File f, int operation) throws IndexException {

        if (f.isHidden() || !f.exists() || !f.canRead()) {
            return;
        }
        DocumentMinion docm = null;

        try {
            if (f.getPath().endsWith(".pdf")) {

                PdfParser pdfp = new PdfParser();
                pdfp.divideTextforMinion(f, simpleInd);

            } else if (f.getPath().endsWith(".java")) {
                ArrayList<String> comment = new ArrayList<String>();
                ArrayList<String> javadocs = new ArrayList<String>();
                JavaParser jp = new JavaParser();
                jp.AnalyzeDocument(f);
                // Start a new document, using the path as a key                
                docm = new DocumentMinion(simpleInd, f.getPath());

                docm.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                docm.addField(getDocumentField(FIELD_NAME), f.getName());
                docm.addField(getDocumentField(FIELD_CODE_ALL_SOURCE), jp.getAllSource()); //en este campo se guarda todo el codigo del documento

                if (jp.getClassPackage() != null) {
                    docm.addField(getDocumentField(FIELD_CODE_PACKAGE), jp.getClassPackage());
                }

                for (int i = 0; i < jp.getClassNumber(); i++) {
                    docm.addField(getDocumentField(FIELD_CODE_CLASSES_NAMES), jp.getClassesNames(i));
                    if (jp.getClassesComments(i) != null) {
                        comment.add(jp.getClassesComments(i));
                    }
                    if (jp.getClassesJDocs(i) != null) {
                        javadocs.add(jp.getClassesJDocs(i));
                    }

                    for (int l = 0; l < jp.getClassVariableNumber(i); l++) {
                        docm.addField(getDocumentField(FIELD_CODE_VARIABLES_NAMES), jp.getClassesVarName(i, l));
                        if (jp.getClassesCommentVariables(i, l) != null) {
                            comment.add(jp.getClassesCommentVariables(i, l));
                        }
                        if (jp.getVariablesJDocs(i, l) != null) {
                            javadocs.add(jp.getVariablesJDocs(i, l));
                        }
                    }

                    for (int j = 0; j < jp.getClassesMethods(i); j++) {
                        docm.addField(getDocumentField(FIELD_CODE_METHODS_NAMES), jp.getClassesMethodsName(i, j));
                        if (jp.getClassesMethodComment(i, j) != null) {
                            comment.add(jp.getClassesMethodComment(i, j));
                        }
                        if (jp.getClassesMethodJDocs(i, j) != null) {
                            javadocs.add(jp.getClassesMethodJDocs(i, j));
                        }
                    }
                    //unir los comentarios
                    docm.addField(getDocumentField(FIELD_CODE_ALL_COMMENTS), joinData(comment));

                    docm.addField(getDocumentField(FIELD_CODE_JAVADOCS), joinData(javadocs));

                }

                docm.closeDocument();


            } else if (f.getPath().endsWith(".txt")) {

                docm = new DocumentMinion(simpleInd, f.getPath());
                docm.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                docm.addField(getDocumentField(FIELD_NAME), f.getName());
                docm.addField(getDocumentField(FIELD_DOC_TEXT), readFile(f));
                docm.closeDocument();

            }
            if (operation == ADD_INDEX) {
                this.notifyAddedDocument();
            } else if (operation == MAKE_INDEX) {
                this.notifyIndexedDocument();
            }

        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new IndexException(ex.getMessage());
        }

    }

    /**
     * lee el contenido de un file
     * @param f
     * @return
     */
    private String readFile(File f) {
        String result = " ";
        char c;

        FileInputStream in = null;
        try {
            in = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
            OutputMonitor.printStream("", ex);
        }

        int buffer;

        try {
            while ((buffer = in.read()) != -1) {
                c = (char) buffer;
                result = result.concat(String.valueOf(c));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return result;
    }

    /**
     * Une los comentarios de una clase de codigo fuente:
     * comentarios de las variables, metodos, y de la clase
     * y une los javadocs también
     * @param aa
     * @return
     */
    private String joinData(ArrayList<String> aa) {
        String result = " ";
        if (aa.size() != 0) {
            for (int i = 0; i < aa.size(); i++) {
                if (aa.get(i) != null) {
                    result = result.concat(" " + aa.get(i));
                }

            }
        } else {
            result = " "; //empty
        }

        return result;


    }

    /**
     * Guardar resultados de busqueda en forma de Metadocument
     * @param sd
     * @param queryT
     * @return
     */
    private ArrayList<DocumentMetaData> saveResults(List<Result> sd) {
        ArrayList<DocumentMetaData> docsFound = new ArrayList<DocumentMetaData>();
        Result result;
        List fpath, namef;
        String filePath, name, fileType, summary;
        Float scor;
        File fil;
        long fsize;

        for (int i = 0; i < sd.size(); i++) {
            result = sd.get(i);
            fpath = result.getField(getDocumentField(FIELD_FILEPATH));
            filePath = (String) fpath.get(0); //hasta ahora solo se guarda un solo valor por campo

            scor = result.getScore();
            namef = result.getField(getDocumentField(FIELD_NAME));
            name = (String) namef.get(0); //hasta ahora solo se guarda un solo valor por campo

            fileType = getFileExtension(filePath);
            fil = new File(filePath);
            fsize = fil.length();

            summary = getPassage2(result, fileType);

            if (summary == null) {
                summary = " ";
            }

            DocumentMetaData metaDoc = new DocumentMetaData();
            metaDoc.setPath(filePath);
            metaDoc.setName(name);
            metaDoc.setScore(Double.valueOf(String.valueOf(scor)));
            metaDoc.setSize(fsize);
            metaDoc.setIndex(result.getKey().hashCode()); //numero que representa ese doc en los resultados de busqueda
            metaDoc.setType(fileType);
            metaDoc.setSynthesis(summary);
            metaDoc.setSearcher(KeySearchable.MINION_SEARCH_ENGINE);
            docsFound.add(metaDoc);

        }
        this.retrievedDocsCount = docsFound.size();
        return docsFound;
    }

    /**
     * Para la sumarización del documento
     * @param r
     * @param fileType
     * @return
     */
    private String getPassage(Result r, String fileType) {
        String highlighted = " ";
        String field = null;

        if (fileType.equalsIgnoreCase("pdf")) {
            field = getDocumentField(FIELD_DOC_BOOK);
        } else if (fileType.equalsIgnoreCase("java")) {
            field = getDocumentField(FIELD_CODE_ALL_SOURCE);
        } else if (fileType.equalsIgnoreCase("txt")) {
            field = getDocumentField(FIELD_DOC_TEXT);
        }

        this.passageBuilder = r.getPassageBuilder();
        this.passageBuilder.addPassageField(field, Passage.Type.JOIN, -1, 256, true);
        @SuppressWarnings("unchecked")
        Map<String, Object> docMap = getDocumentMap(r.getKey());
        //Gets the highlighted passages that were specified using addPassageField.

        Map<String, List<Passage>> pmap = this.passageBuilder.getPassages(docMap, -1, -1, false);

        if (pmap.get(field) != null) {

            if (!pmap.get(field).isEmpty()) {
                Passage sp = pmap.get(field).get(0); //get the passage of that field to highlight

                if (sp != null) {
//                    this.simpleHighlighter = new SimpleHighlighter("<font color=\"#00ff00\">","</font>", "<b>", "</b>");
                    tHighlighter = new TextHighlighter();
                    highlighted = sp.highlight(tHighlighter, false);

                }

            }
        }

        return highlighted;
    }

    private String getPassage2(Result r, String fileType) {

        String field = null;
        String summary = null;

        if (fileType.equalsIgnoreCase("pdf")) {
            field = getDocumentField(FIELD_DOC_BOOK);
        } else if (fileType.equalsIgnoreCase("java")) {
            field = getDocumentField(FIELD_CODE_ALL_SOURCE);
        } else if (fileType.equalsIgnoreCase("txt")) {
            field = getDocumentField(FIELD_DOC_TEXT);
        }

        if (field != null) {
            String single = (String) r.getSingleFieldValue(field);
            if (single.length() > 200) {
                summary = single.substring(0, 200);
            } else {
                summary = single.substring(0);
            }

        }


        return summary;
    }

    /**
     * Dado un key de un doc obtiene la relacion <field,value> para todos
     * los campos de este doc
     * @param keyDoc
     * @return
     */
    private Map getDocumentMap(String keyDoc) {
        LinkedHashMap<String, String> list2 = new LinkedHashMap<String, String>();
        String keyField, value;
        List valueField;

        if (this.engine != null) {
            for (Iterator<Entry<String, List>> list = this.engine.getDocument(keyDoc).getSavedFields(); list.hasNext();) {
                Entry<String, List> entry = list.next();
                keyField = entry.getKey();
                valueField = entry.getValue();
                for (int i = 0; i < valueField.size(); i++) {
                    value = (String) valueField.get(i);
                    list2.put(keyField, value);
                }
            }
        }
        // this.engine.getSimpleIndexer().endDocument();


        return list2;
    }
}
